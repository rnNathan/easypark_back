package com.senac.easypark.util;

import com.senac.easypark.model.dto.RelatorioTicketsFechadosDTO;
import com.senac.easypark.model.entities.RelatorioTicketsFechados;
import com.senac.easypark.model.enums.TipoTicket;
import com.senac.easypark.model.enums.TipoVeiculo;

import java.time.LocalDateTime;

public class RelatorioTicketsFechadosMapper {

    public static RelatorioTicketsFechadosDTO toDTO(RelatorioTicketsFechados relatorio) {
        if (relatorio == null) {
            return null;
        }
        return new RelatorioTicketsFechadosDTO(
                relatorio.getId(),
                relatorio.getPlacaVeiculo(),
                relatorio.getHoraEntrada(),
                relatorio.getHoraSaida(),
                relatorio.getTipoVeiculo(),
                relatorio.getTipoTicket(),
                relatorio.getValorTotalPagar()

        );
    }

    public static RelatorioTicketsFechados toEntity(RelatorioTicketsFechadosDTO dto) {
        if (dto == null) {
            return null;
        }
        return new RelatorioTicketsFechados(
                dto.getId(),
                dto.getPlacaVeiculo(),
                dto.getHoraEntrada(),
                dto.getHoraSaida(),
                dto.getTipoVeiculo(),
                dto.getTipoTicket(),
                dto.getValorTotalPagar()
        );
    }
}

