package com.senac.easypark.service;



import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.senac.easypark.model.entities.Ticket;
import com.senac.easypark.model.enums.TipoVeiculo;
import com.senac.easypark.model.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.senac.easypark.model.dto.ConfiguracaoSistemaDTO;
import com.senac.easypark.model.entities.ConfiguracaoSistema;
import com.senac.easypark.model.repository.ConfiguracaoSistemaRepository;
import com.senac.easypark.util.ConfiguracaoSistemaMapper;

@Service
public class ConfiguracaoSistemaService {

    private final ConfiguracaoSistemaRepository configuracaoSistemaRepository;
    private final ConfiguracaoSistemaMapper configuracaoSistemaMapper;
    private final TicketRepository ticketRepository;

    @Autowired
    public ConfiguracaoSistemaService(ConfiguracaoSistemaRepository configuracaoSistemaRepository,
                                      ConfiguracaoSistemaMapper configuracaoSistemaMapper, TicketRepository ticketRepository) {
        this.configuracaoSistemaRepository = configuracaoSistemaRepository;
        this.configuracaoSistemaMapper = configuracaoSistemaMapper;
        this.ticketRepository = ticketRepository;
    }

    public ConfiguracaoSistemaDTO criarConfiguracao(ConfiguracaoSistemaDTO configuracaoDTO) {
        ConfiguracaoSistema configuracao = configuracaoSistemaMapper.toEntity(configuracaoDTO);
        ConfiguracaoSistema savedConfiguracao = configuracaoSistemaRepository.save(configuracao);
        return configuracaoSistemaMapper.toDTO(savedConfiguracao);
    }

    public ConfiguracaoSistemaDTO buscarConfiguracaoAtual() {
        ConfiguracaoSistema configuracao = configuracaoSistemaRepository.findTopByOrderByIdDesc()
                .orElseThrow(() -> new RuntimeException("Nenhuma configuração encontrada"));
        return configuracaoSistemaMapper.toDTO(configuracao);
    }

    public ConfiguracaoSistemaDTO atualizarConfiguracao(Integer id, ConfiguracaoSistemaDTO configuracaoDTO) {
        ConfiguracaoSistema configuracao = configuracaoSistemaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Configuração não encontrada"));
        configuracaoSistemaMapper.updateEntityFromDTO(configuracao, configuracaoDTO);
        ConfiguracaoSistema updatedConfiguracao = configuracaoSistemaRepository.save(configuracao);
        return configuracaoSistemaMapper.toDTO(updatedConfiguracao);
    }

    public List<ConfiguracaoSistemaDTO> listarTodasConfiguracoes() {
        return configuracaoSistemaRepository.findAll().stream()
                .map(configuracaoSistemaMapper::toDTO)
                .collect(Collectors.toList());
    }

    public BigDecimal getValorPorHora() {
        ConfiguracaoSistema configuracao = configuracaoSistemaRepository.findTopByOrderByIdDesc()
                .orElseThrow(() -> new RuntimeException("Configuração do sistema não encontrada"));
        return BigDecimal.valueOf(configuracao.getValorHoraCarro());
    }


    //METODO PARA ATIVAR O BOTÃO DOS STATUS
    public void ativarContagemEMostrar(boolean mostrar) {
        // Busca a configuração do sistema
        ConfiguracaoSistema configuracao = configuracaoSistemaRepository.findTopByOrderByIdDesc()
                .orElseThrow(() -> new RuntimeException("Configuração do sistema não encontrada"));

        // Atualiza a configuração para ativar ou desativar a contagem de veículos
        configuracao.setMostrar(mostrar);
        configuracaoSistemaRepository.save(configuracao); // Salva a configuração atualizada

        // Se "mostrar" for verdadeiro, inicializa contadores com os valores definidos na configuração
        if (mostrar) {
            int qtdCarro = configuracao.getQtdCarro();
            int qtdMoto = configuracao.getQtdMoto();

            // Busca todos os tickets ativos
            List<Ticket> ticketsAtivos = ticketRepository.findAll(); // Supondo que todos os tickets são considerados ativos

            // Conta os veículos por tipo
            for (Ticket ticket : ticketsAtivos) {
                if (ticket.getTipoVeiculo() == TipoVeiculo.CARRO) {
                    qtdCarro--;
                } else if (ticket.getTipoVeiculo() == TipoVeiculo.MOTO) {
                    qtdMoto--;
                }
            }

            // Atualiza a configuração com as novas quantidades
            configuracao.setQtdCarro(qtdCarro);
            configuracao.setQtdMoto(qtdMoto);
            configuracaoSistemaRepository.save(configuracao); // Salva a configuração atualizada
        }
    }
}
