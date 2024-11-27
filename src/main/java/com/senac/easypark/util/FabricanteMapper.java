package com.senac.easypark.util;


import org.springframework.stereotype.Component;

import com.senac.easypark.model.dto.FabricanteDTO;
import com.senac.easypark.model.entities.Fabricante;

@Component
public class FabricanteMapper {

    public static Fabricante toEntity(FabricanteDTO dto) {
        return new Fabricante(dto.getId(), dto.getModelo(), dto.getMarca(),
                dto.getAno());
    }

    public static FabricanteDTO toDTO(Fabricante entity) {
        return new FabricanteDTO(entity.getId(), entity.getModelo()
                , entity.getMarca(), entity.getAno());
    }

    public static void updateEntityFromDTO(Fabricante fabricante, FabricanteDTO dto) {
        if (dto == null) {
            return;
        }
        fabricante.setModelo(dto.getModelo());
        fabricante.setMarca(dto.getMarca());
        fabricante.setAno(dto.getAno());
    }

}
