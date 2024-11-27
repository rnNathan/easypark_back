package com.senac.easypark.util;

import org.springframework.stereotype.Component;

import com.senac.easypark.model.dto.EnderecoDTO;
import com.senac.easypark.model.entities.Endereco;

@Component
public class EnderecoMapper {

    public static Endereco toEntity(EnderecoDTO dto) {
        Endereco endereco = new Endereco(dto.getId(), dto.getCidade(), dto.getEstado(), dto.getCep());
        return endereco;
    }

    public static EnderecoDTO toDTO(Endereco endereco) {
        EnderecoDTO enderecoDTO = new EnderecoDTO(endereco.getId(), endereco.getCidade(), endereco.getEstado(), endereco.getCep());
        return enderecoDTO;
    }

    public static void updateEnderecoFromDTO(Endereco endereco, EnderecoDTO dto) {
        if (dto == null) {
            return;
        }
        endereco.setCidade(dto.getCidade());
        endereco.setEstado(dto.getEstado());
        endereco.setCep(dto.getCep());
    }
}
