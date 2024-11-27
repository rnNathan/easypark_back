package com.senac.easypark.util;

import com.senac.easypark.model.dto.AssinaturaPlanoDTO;
import com.senac.easypark.model.dto.PlanoDTO;
import com.senac.easypark.model.dto.UsuarioDTO;
import com.senac.easypark.model.entities.AssinaturaPlano;
import com.senac.easypark.model.entities.Plano;
import com.senac.easypark.model.entities.Usuario;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AssinaturaPlanoMapper {
    public static AssinaturaPlanoDTO toDTO(AssinaturaPlano entity) {
        if (entity == null) {
            return null;
        }

        AssinaturaPlanoDTO dto = new AssinaturaPlanoDTO();
        dto.setId(entity.getId());
        dto.setDataPagamento(entity.getDataPagamento());
        dto.setDataVencimento(entity.getDataVencimento());
        dto.setAtivo(entity.isAtivo());

        // Setando informações do usuário
        if (entity.getUsuario() != null) {
            UsuarioDTO usuarioDTO = new UsuarioDTO();
            usuarioDTO.setId(entity.getUsuario().getId());
            usuarioDTO.setNome(entity.getUsuario().getNome());
            usuarioDTO.setEmail(entity.getUsuario().getEmail());
            usuarioDTO.setTelefone(entity.getUsuario().getTelefone());
            usuarioDTO.setCpf(entity.getUsuario().getCpf());
            dto.setUsuarioDTO(usuarioDTO);
        }

        // Setando informações do plano
        if (entity.getPlano() != null) {
            PlanoDTO planoDTO = new PlanoDTO();
            planoDTO.setId(entity.getPlano().getId());
            planoDTO.setValorPlano(entity.getPlano().getValorPlano());
            planoDTO.setTipoPlano(entity.getPlano().getTipoPlano());
            planoDTO.setTipoVeiculo(entity.getPlano().getTipoVeiculo());
            dto.setPlanoDTO(planoDTO);
        }

        return dto;
    }

    public static AssinaturaPlano toEntity(AssinaturaPlanoDTO dto) {
        if (dto == null) {
            return null;
        }
        AssinaturaPlano entity = new AssinaturaPlano();
        entity.setId(dto.getId());

        // Criando objetos com apenas os IDs para relacionamentos
        if (dto.getUsuarioDTO() != null) {
            Usuario usuario = new Usuario();
            usuario.setId(dto.getUsuarioDTO().getId());
            entity.setUsuario(usuario);
        }

        if (dto.getPlanoDTO()!= null) {
            Plano plano = new Plano();
            plano.setId(dto.getPlanoDTO().getId());
            entity.setPlano(plano);
        }

        entity.setDataPagamento(dto.getDataPagamento());
        entity.setDataVencimento(dto.getDataVencimento());
        entity.setAtivo(dto.isAtivo());

        return entity;
    }

    public static List<AssinaturaPlanoDTO> toDTOList(List<AssinaturaPlano> entities) {
        return entities.stream()
                .map(AssinaturaPlanoMapper::toDTO)
                .toList();
    }

    public static List<AssinaturaPlano> toEntityList(List<AssinaturaPlanoDTO> dtos) {
        return dtos.stream()
                .map(AssinaturaPlanoMapper::toEntity)
                .toList();
    }

    public static void updateEntityFromDTO(AssinaturaPlano entity, AssinaturaPlanoDTO dto) {
        if (dto == null || entity == null) {
            return;
        }

        if (dto.getUsuarioDTO() != null) {
            Usuario usuario = new Usuario();
            usuario.setId(dto.getUsuarioDTO().getId());
            entity.setUsuario(usuario);
        }

        if (dto.getPlanoDTO() != null) {
            Plano plano = new Plano();
            plano.setId(dto.getPlanoDTO().getId());
            entity.setPlano(plano);
        }

        entity.setDataPagamento(dto.getDataPagamento());
        entity.setDataVencimento(dto.getDataVencimento());
        entity.setAtivo(dto.isAtivo());
    }
}
