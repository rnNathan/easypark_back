package com.senac.easypark.util;


import org.springframework.stereotype.Component;

import com.senac.easypark.model.dto.TicketDTO;
import com.senac.easypark.model.entities.Ticket;

import java.util.List;

@Component
public class TicketMapper {

    public static Ticket toEntity(TicketDTO dto) {
        return new Ticket(dto.getId(), dto.getPlacaVeiculo(), dto.getHoraChegada(),
                dto.getHoraSaida(), dto.getTipoTicket(), dto.getTipoVeiculo(), dto.getTotalHoras(), dto.getValorTotalPagar());
    }

    public static TicketDTO toDTO(Ticket entity) {
        return new TicketDTO(entity.getId(), entity.getPlacaVeiculo(), entity.getHoraChegada(), entity.getHoraSaida()
                , entity.getTotalHoras(), entity.getTipoTicket(),entity.getTipoVeiculo(), entity.getValorTotalPagar());
    }

    public static List<TicketDTO> toDtoList(List<Ticket> tickets) {
        return tickets.stream().map(TicketMapper::toDTO).toList();
    }

    public static List<Ticket> toEntityList(List<TicketDTO> dtos) {
        return dtos.stream().map(TicketMapper::toEntity).toList();
    }

    public void updateEntityFromDTO(Ticket ticket, TicketDTO dto) {
        if (dto == null) {
            return;
        }
        ticket.setPlacaVeiculo(dto.getPlacaVeiculo());
        ticket.setHoraChegada(dto.getHoraChegada());
        ticket.setHoraSaida(dto.getHoraSaida());
        ticket.setTotalHoras(dto.getTotalHoras());
        ticket.setValorTotalPagar(dto.getValorTotalPagar());
    }


}
