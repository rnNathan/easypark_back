package com.senac.easypark.util;

import com.senac.easypark.model.dto.RelatorioTicketsFechadosDTO;
import com.senac.easypark.model.entities.RelatorioTicketsFechados;

public class RelatorioTicketsFechadosMapper {

    public static RelatorioTicketsFechadosDTO toDTO(RelatorioTicketsFechados relatorio) {
        if (relatorio == null) {
            return null;
        }
        return new RelatorioTicketsFechadosDTO(
                relatorio.getId(),
                relatorio.getHoraEntrada(),
                relatorio.getHoraSaida(),
                relatorio.getTipoVeiculo(),
                relatorio.getValorTotalPagar()
        );
    }

    public static RelatorioTicketsFechados toEntity(RelatorioTicketsFechadosDTO dto) {
        if (dto == null) {
            return null;
        }
        return new RelatorioTicketsFechados(
                dto.getId(),
                dto.getHoraEntrada(),
                dto.getHoraSaida(),
                dto.getTipoVeiculo(),
                dto.getValorTotalPagar()
        );
    }
}
