package com.senac.easypark.util;

import com.senac.easypark.model.dto.UsuarioDTO;
import com.senac.easypark.model.entities.Usuario;
import java.util.List;

public class UsuarioMapper {

    public static Usuario toEntity(UsuarioDTO dto) {
        if (dto == null) {
            return null;
        }
        Usuario usuario = new Usuario();
        usuario.setId(dto.getId());
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setTelefone(dto.getTelefone());
        usuario.setCpf(dto.getCpf());

        // Converte o Endereco
        if (dto.getEndereco() != null) {
            usuario.setEndereco(EnderecoMapper.toEntity(dto.getEndereco()));
        }
        /*if (dto.getPlanoDTO() != null) {
            usuario.setPlano(PlanoService.convertToEntity(dto.getPlanoDTO()));
        }*/
        return usuario;
    }

    // Converte uma entidade Usuario para UsuarioDTO
    public static UsuarioDTO toDTO(Usuario entity) {
        if (entity == null) {
            return null;
        }
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setEmail(entity.getEmail());
        dto.setTelefone(entity.getTelefone());
        dto.setCpf(entity.getCpf());

        // Converte o Endereco
        if (entity.getEndereco() != null) {
            dto.setEndereco(EnderecoMapper.toDTO(entity.getEndereco()));
        }

        if (entity.getAssinaturas() != null) {
            dto.setAssinaturas(AssinaturaPlanoMapper.toDTOList(entity.getAssinaturas()));
        }

        if (entity.getVeiculos() != null && !entity.getVeiculos().isEmpty()) {
            dto.setVeiculosDTO(VeiculoMapper.toDtoList(entity.getVeiculos()));
        }

        return dto;
    }

    public static List<UsuarioDTO> toDTOList(List<Usuario> usuarios) {
        return usuarios.stream().map(UsuarioMapper::toDTO).toList();
    }

    // Converte uma lista de UsuarioDTO para uma lista de Usuario (entidade)
    public static List<Usuario> toEntityList(List<UsuarioDTO> usuarioDtos) {
        return usuarioDtos.stream().map(UsuarioMapper::toEntity).toList();
    }

}

