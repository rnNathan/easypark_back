package com.senac.easypark.service;



import com.senac.easypark.model.dto.AssinaturaPlanoDTO;
import com.senac.easypark.model.entities.AssinaturaPlano;
import com.senac.easypark.model.entities.Plano;
import com.senac.easypark.model.entities.Usuario;
import com.senac.easypark.model.enums.TipoPlano;
import com.senac.easypark.exception.EstacionamentoException;
import com.senac.easypark.model.repository.AssinaturaPlanoRepository;
import com.senac.easypark.model.repository.PlanoRepository;
import com.senac.easypark.model.repository.UsuarioRepository;
import com.senac.easypark.util.AssinaturaPlanoMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AssinaturaPlanoService {

    private final AssinaturaPlanoRepository assinaturaPlanoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PlanoRepository planoRepository;

    @Transactional(readOnly = true)
    public List<AssinaturaPlanoDTO> findAll() {
        return AssinaturaPlanoMapper.toDTOList(assinaturaPlanoRepository.findAll());
    }

    @Transactional(readOnly = true)
    public AssinaturaPlanoDTO findById(Integer id) {
        return AssinaturaPlanoMapper.toDTO(assinaturaPlanoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Assinatura não encontrada")));
    }

    @Transactional
    public AssinaturaPlanoDTO create(AssinaturaPlanoDTO dto) throws EstacionamentoException {

        //VERIFICAR UMA ASSINATURA ATIVA PARA O USUARIO
        if (assinaturaPlanoRepository.existsByUsuarioIdAndPlanoId(dto.getUsuarioDTO().getId(), dto.getPlanoDTO().getId())) {
            throw new EstacionamentoException("O usuário já possui uma assinatura ativa para este plano.");
        }

        //VARIAVEL PARA OBTER INFORMAÇOES
        AssinaturaPlano entity = AssinaturaPlanoMapper.toEntity(dto);

        //Busca e valida o usuário
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioDTO().getId())
                .orElseThrow(() -> new EstacionamentoException("Usuário não encontrado"));
        entity.setUsuario(usuario);

        //Busca e valida o plano
        Plano plano = planoRepository.findById(dto.getPlanoDTO().getId())
                .orElseThrow(() -> new EntityNotFoundException("Plano não encontrado"));
        entity.setPlano(plano);

        //Define a data atual como data de pagamento
        LocalDateTime dataPagamento = LocalDateTime.now();
        entity.setDataPagamento(dataPagamento);

        //Define a data de vencimento como 30 dias após o pagamento
        LocalDateTime dataVencimento = dataPagamento.plusDays(30);
        entity.setDataVencimento(dataVencimento);

        //Marca a assinatura como ativa
        entity.setAtivo(true);

        //Valida o tipo do plano
        if (!isTipoPlanoValido(plano.getTipoPlano())) {
            throw new IllegalArgumentException("Tipo de plano inválido. Deve ser INTEGRAL, MANHA ou TARDE");
        }

        return AssinaturaPlanoMapper.toDTO(assinaturaPlanoRepository.save(entity));
    }

    private boolean isTipoPlanoValido(TipoPlano tipoPlano) {
        return  tipoPlano == TipoPlano.MENSAL ||
                tipoPlano == TipoPlano.INTEGRAL ||
                tipoPlano == TipoPlano.MANHA ||
                tipoPlano == TipoPlano.TARDE ||
                tipoPlano == TipoPlano.NOITE;
    }


    @Transactional
    public AssinaturaPlanoDTO update(Integer id, AssinaturaPlanoDTO dto) {
        AssinaturaPlano entity = assinaturaPlanoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Assinatura não encontrada"));

        AssinaturaPlanoMapper.updateEntityFromDTO(entity, dto);

        if (dto.getUsuarioDTO() != null && dto.getUsuarioDTO().getId() != null) {
            Usuario usuario = usuarioRepository.findById(dto.getUsuarioDTO().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
            entity.setUsuario(usuario);
        }

        if (dto.getPlanoDTO() != null && dto.getPlanoDTO().getId() != null) {
            Plano plano = planoRepository.findById(dto.getPlanoDTO().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Plano não encontrado"));
            entity.setPlano(plano);
        }

        return AssinaturaPlanoMapper.toDTO(assinaturaPlanoRepository.save(entity));
    }

    @Transactional
    public void delete(Integer id) throws EstacionamentoException {
        if (!assinaturaPlanoRepository.existsById(id)) {
            throw new EntityNotFoundException("Assinatura não encontrada");
        }

        Optional<AssinaturaPlano> assinaturaPlano = assinaturaPlanoRepository.findById(id);
        if (assinaturaPlano.isPresent() && assinaturaPlano.get().isAtivo()) {
            throw new EstacionamentoException("Nao pode excluir assinatura antes de desativala");
        }

        assinaturaPlanoRepository.deleteById(id);
    }
}
