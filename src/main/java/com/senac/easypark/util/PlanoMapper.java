package com.senac.easypark.util;


import com.senac.easypark.model.dto.PlanoDTO;
import com.senac.easypark.model.entities.Plano;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlanoMapper {

    // Converte de Plano para PlanoDTO
    public static Plano convertToEntity(PlanoDTO planoDTO) {
        if (planoDTO == null) {
            return null;
        }
        Plano plano = new Plano();
        plano.setId(planoDTO.getId());
        plano.setTipoPlano(planoDTO.getTipoPlano());
        plano.setTipoVeiculo(planoDTO.getTipoVeiculo());
        plano.setValorPlano(planoDTO.getValorPlano());

        return plano;
    }

    // Converte de Plano para PlanoDTO
    public static PlanoDTO convertToDTO(Plano plano) {
        if (plano == null) {
            return null;
        }
        PlanoDTO planoDTO = new PlanoDTO();
        planoDTO.setId(plano.getId());
        planoDTO.setTipoPlano(plano.getTipoPlano());
        planoDTO.setTipoVeiculo(plano.getTipoVeiculo());
        planoDTO.setValorPlano(plano.getValorPlano());


        return planoDTO;
    }

    public static List<PlanoDTO> toDTOList(List<Plano> planos) {
        return planos.stream().map(PlanoMapper::convertToDTO).toList();
    }

    public static List<Plano> toPlanoList(List<PlanoDTO> planoDTOs) {
        return planoDTOs.stream().map(PlanoMapper::convertToEntity).toList();
    }

    // Método para atualizar dados de um plano
    public static void updatePlanoFromDTO(Plano plano, PlanoDTO planoDTO) {
        if (planoDTO == null || plano == null) {
            return;
        }
        plano.setTipoPlano(planoDTO.getTipoPlano());
        plano.setTipoVeiculo(planoDTO.getTipoVeiculo());
        plano.setValorPlano(planoDTO.getValorPlano());

    }


}

