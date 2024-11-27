package com.senac.easypark.service;


import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.senac.easypark.model.entities.*;
import com.senac.easypark.model.enums.TipoTicket;
import com.senac.easypark.model.enums.TipoVeiculo;
import com.senac.easypark.exception.EstacionamentoException;
import com.senac.easypark.model.repository.ConfiguracaoSistemaRepository;
import com.senac.easypark.model.repository.RelatorioTicketsFechadosRepository;
import com.senac.easypark.model.repository.VeiculoRepository;
import com.senac.easypark.util.validacao.ValidarVeiculo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.senac.easypark.model.dto.TicketDTO;
import com.senac.easypark.model.repository.TicketRepository;
import com.senac.easypark.util.TicketMapper;
// import com.backend.EasyPark.service.ConfiguracaoSistemaService;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final ValidarVeiculo validarVeiculo;
    private final VeiculoRepository veiculoRepository;
    private final ConfiguracaoSistemaRepository configuracaoSistemaRepository;
    private final RelatorioTicketsFechadosRepository relatorioTicketsFechadosRepository;

    @Autowired
    public TicketService(TicketRepository ticketRepository,
                         ValidarVeiculo validarVeiculo,
                         VeiculoRepository veiculoRepository, ConfiguracaoSistemaRepository configuracaoSistemaRepository, RelatorioTicketsFechadosRepository relatorioTicketsFechadosRepository) {
        this.ticketRepository = ticketRepository;
        this.validarVeiculo = validarVeiculo;
        this.veiculoRepository = veiculoRepository;
        this.configuracaoSistemaRepository = configuracaoSistemaRepository;
        this.relatorioTicketsFechadosRepository = relatorioTicketsFechadosRepository;
    }
    public TicketDTO criarTicket(TicketDTO ticket) throws EstacionamentoException {
        // Verifica se os dados básicos são válidos
        if (ticket == null || ticket.getPlacaVeiculo() == null) {
            throw new EstacionamentoException("Ticket ou placa do veículo não podem ser nulos");
        }

        //Validar se a placa está no formato correto.
        this.validarPlaca(ticket.getPlacaVeiculo());

        //Só vai poder criar um ticket se a configuração estiver criad.
        ConfiguracaoSistema configuracao = configuracaoSistemaRepository.findTopByOrderByIdDesc()
                .orElseThrow(() -> new RuntimeException("Configuração do sistema não encontrada," +
                        " portando não é possivel criar um ticket"));


        //Buscar um veiculo pela placa.
        Optional<Ticket> ticketExistente = ticketRepository.findByPlacaVeiculo(ticket.getPlacaVeiculo());

        //valida se tem um carro no estacionamento.
        if (ticketExistente.isPresent()) {
            throw new EstacionamentoException("Já existe um veículo estacionado com esta placa: " + ticket.getPlacaVeiculo());
        }

        //hora tual
        LocalTime horaAtual = LocalTime.now();

        //Busca os veículos com assinatura válida
        List<Veiculo> veiculos = veiculoRepository.buscarVeiculoComAssinaturaValida(ticket.getPlacaVeiculo());

        //Criando um ticket manualmente
        Ticket ticketEntity = new Ticket();
        ticketEntity.setPlacaVeiculo(ticket.getPlacaVeiculo());
        ticketEntity.setTipoVeiculo(ticket.getTipoVeiculo());
        ticketEntity.setHoraChegada(LocalDateTime.now());

        //Se encontrar veículos com assinatura válida
        if (!veiculos.isEmpty()) {
            Veiculo veiculo = veiculos.get(0);  //Considerando que pegamos o primeiro veículo válido

            //Verifica as assinaturas do usuário para encontrar um plano válido
            for (AssinaturaPlano assinatura : veiculo.getUsuario().getAssinaturas()) {
//                Ele busca o tipo do plano e pega o horario definido no enum e compara com a hora atual
//                Só continua se bater o horario
                if (assinatura.getPlano().getTipoPlano().contemHorario(horaAtual)) {
                    //Se encontrar um plano válido, define o ticket como mensalista
                    ticketEntity.setTipoTicket(TipoTicket.TICKET_MENSALISTA);
                    veiculo.setOcupandoVaga(true);
                    veiculoRepository.save(veiculo);  //Salva o estado do veículo
                    //Para o loop ao encontrar o primeiro plano válido
                }else {
                    ticketEntity.setTipoTicket(TipoTicket.TICKET_AVULSO);
                }

            }
        } else {
            //Se nenhum veículo com assinatura válida for encontrado, cria ticket avulso
            ticketEntity.setTipoTicket(TipoTicket.TICKET_AVULSO);
        }

        // Tenta salvar o ticket no banco de dados
        try {
            return TicketMapper.toDTO(ticketRepository.save(ticketEntity));
        } catch (Exception e) {
            throw new EstacionamentoException("Erro ao criar o ticket: " + e.getMessage());
        }
    }


    public TicketDTO buscarTicketPorId(Integer id) {
        return ticketRepository.findById(id)
                .map(TicketMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Ticket não encontrado"));
    }

    public List<TicketDTO> listarTicketsPorTipo(TipoTicket tipoTicket) {
        return ticketRepository.findByTipoTicket(tipoTicket).stream()
                .map(TicketMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<TicketDTO> listarTickets() {
        return ticketRepository.findAll().stream()
                .map(TicketMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TicketDTO finalizarTicket(Integer id) throws EstacionamentoException {
        //Finaliza um ticket, calculando o tempo total e o valor a pagar
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket não encontrado"));

        ticket.setHoraSaida(LocalDateTime.now());
        ticket.setTotalHoras(Duration.between(ticket.getHoraChegada(), ticket.getHoraSaida()));

        BigDecimal valorTotal = calcularValorTicket(TicketMapper.toDTO(ticket));
        ticket.setValorTotalPagar(valorTotal.doubleValue());

        RelatorioTicketsFechados relatorio = new RelatorioTicketsFechados();
        relatorio.setHoraEntrada(ticket.getHoraChegada());
        relatorio.setHoraSaida(ticket.getHoraSaida());
        relatorio.setTipoVeiculo(ticket.getTipoVeiculo());
        relatorio.setValorTotalPagar(ticket.getValorTotalPagar());

        // Salva o ticket com o valor calculado
        Ticket updatedTicket = ticketRepository.save(ticket);

        // Caso for mensalista ou avulso, trocamos o status de ocupação do veículo
        if (ticket.getTipoTicket().equals(TipoTicket.TICKET_MENSALISTA)) {
            // Quando for mensalista, obrigatório ter um veículo associado ao usuário
            Optional<Veiculo> veiculo = veiculoRepository.findByPlaca(ticket.getPlacaVeiculo());
            if (veiculo.isPresent()) {
                veiculo.get().setOcupandoVaga(false);
                veiculoRepository.save(veiculo.get());
            }
        }
        //pegando a configuração para aumentar a quantidade de vagas no estacionamento pelo tipo
        ConfiguracaoSistema configuracao = configuracaoSistemaRepository.findTopByOrderByIdDesc()
                .orElseThrow(() -> new RuntimeException("Configuração do sistema não encontrada"));

        //Validar o tipo do veiculo do plano tbm, pois o plano necessario precisa colocar o tipo do veiculo

        if (configuracao.isMostrar()) {
            if (ticket.getTipoVeiculo() == TipoVeiculo.CARRO) {
                if (configuracao.getQtdCarro() <= 0) {
                    throw new EstacionamentoException("Não há vagas disponíveis para carros.");
                }
                configuracao.setQtdCarro(configuracao.getQtdCarro() + 1);
            } else if (ticket.getTipoVeiculo() == TipoVeiculo.MOTO) {
                if (configuracao.getQtdMoto() <= 0) {
                    throw new EstacionamentoException("Não há vagas disponíveis para motos.");
                }
                configuracao.setQtdMoto(configuracao.getQtdMoto() + 1);
            }
            configuracaoSistemaRepository.save(configuracao); // Atualiza a configuração
        }

        // Salva o relatório no banco de dados
        relatorioTicketsFechadosRepository.save(relatorio);

        ticketRepository.delete(ticket);

        //Exclui o ticket após finalização para evitar o acúmulo de dado
        //Retorna o DTO do ticket finalizado, com os dados atualizados
        return TicketMapper.toDTO(updatedTicket);
    }


    public BigDecimal calcularValorTicket(TicketDTO ticket) {
        // 1. Verifica se o ticket é de um cliente mensalista ou se a permanência foi inferior a 15 minutos
        boolean isMenosDe15Minutos = ticket.getTotalHoras().toMinutes() < 15;
        if (isMenosDe15Minutos || ticket.getTipoTicket().equals(TipoTicket.TICKET_MENSALISTA)) {
            return BigDecimal.ZERO; // Isenção de pagamento
        }
        //Se for ticket avulso, procura a placa e determina o tipo de veículo
        BigDecimal valorHora = BigDecimal.ZERO;

        if (ticket.getTipoTicket().equals(TipoTicket.TICKET_AVULSO)) {
            //PARA O TICKET AVULSO, NAO PRECISA BUSCAR NO VEICULO, AVULSO NAO CADASTRA VEICULOS
            //VERIFICAR SE EXISTE UMA CONFIGURACAO NO SISTEMA
            ConfiguracaoSistema configuracao = configuracaoSistemaRepository.findTopByOrderByIdDesc()
                    .orElseThrow(() -> new RuntimeException("Configuração do sistema não encontrada"));

            //AQUI TBM DETERMINAR O VALOR DA HORA POR TIPO DO VEICULO
            if (ticket.getTipoVeiculo() == TipoVeiculo.MOTO) {
                valorHora = BigDecimal.valueOf(configuracao.getValorHoraMoto());
            } else if (ticket.getTipoVeiculo() == TipoVeiculo.CARRO) {
                valorHora = BigDecimal.valueOf(configuracao.getValorHoraCarro());
            }
        } else {
            //PARA O TICKET MENSALISTA PESQUISAR O TIPO PARA VERIFICAR AS VALIDAÇOES
            String placaVeiculo = ticket.getPlacaVeiculo();

            if (placaVeiculo != null && !placaVeiculo.isEmpty()) {

                Veiculo veiculo = veiculoRepository.findByPlaca(placaVeiculo)
                        .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));

                TipoVeiculo tipoVeiculo = veiculo.getTipoVeiculo();

                List<AssinaturaPlano> tipoPlano = veiculo.getUsuario().getAssinaturas();  // Assumindo que existe um plano ativo

                for (AssinaturaPlano assinaturaPlano : tipoPlano) {

                    ConfiguracaoSistema configuracao = configuracaoSistemaRepository.findTopByOrderByIdDesc()
                            .orElseThrow(() -> new RuntimeException("Configuração do sistema não encontrada"));

                    //DETERMINAR O VALOR DO VEICULO PELO TIPOVEICULO
                    if (tipoVeiculo == TipoVeiculo.MOTO) {
                        valorHora = BigDecimal.valueOf(configuracao.getValorHoraMoto());
                    } else if (tipoVeiculo == TipoVeiculo.CARRO) {
                        valorHora = BigDecimal.valueOf(configuracao.getValorHoraCarro());
                    }

                    //VERIFICAR PARA VER SE PASSOU O HORARIO DO PLANO
                    LocalTime horaLimitePlano = assinaturaPlano.getPlano().getTipoPlano().getHoraFim();  // Hora final do plano

                    if (ticket.getHoraSaida().toLocalTime().isAfter(horaLimitePlano)) {
                        //AQUI, SE PASSAR DO HORARIO DO PLANO ELE PAGA A DIFERENÇA POR VEICULO
                        Duration horasExtras = Duration.between(horaLimitePlano, ticket.getHoraSaida().toLocalTime());
                        long horasExtrasCalculadas = horasExtras.toHours();

                        //METODO PARA HORAS QUANDO TIVER EM MINUTOS
                        if (horasExtras.toMinutesPart() > 0) {
                            horasExtrasCalculadas++;
                        }

                        //Adiciona o valor das horas extras ao valor do ticket
                        return valorHora.multiply(BigDecimal.valueOf(horasExtrasCalculadas));
                    }
                }
            } else {
                //Se o ticket não tem placa e não é avulso, consideramos erro
                throw new RuntimeException("Ticket mensalista precisa de um veículo cadastrado.");
            }
        }

        //Se for ticket avulso e dentro do horário, calcula o valor total a ser pago
        long horasEstacionado = ticket.getTotalHoras().toHours();

        //Arredonda para cima qualquer fração de hora
        if (ticket.getTotalHoras().toMinutesPart() > 0) {
            horasEstacionado++;
        }

        //Calcula o valor total a ser pago
        return valorHora.multiply(BigDecimal.valueOf(horasEstacionado));
    }

    public void validarPlaca(String placa) throws EstacionamentoException {
        // Valida o campo 'placa'
        if (placa == null || placa.isEmpty()) {
            throw new EstacionamentoException("A placa do veículo é obrigatória.");
        } else if (placa.length() != 7 || !placa.matches("[A-Z]{3}[0-9][A-Z0-9][0-9]{2}")) {
            throw new EstacionamentoException("A placa deve conter 7 caracteres e estar no formato padrão (exemplo: ABC1234 ou ABC1D23).");
        }

    }



}


