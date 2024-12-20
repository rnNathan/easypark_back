package com.senac.easypark.model.dto;

import java.time.LocalDateTime;

import com.senac.easypark.model.enums.TipoTicket;
import com.senac.easypark.model.enums.TipoVeiculo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioTicketsFechadosDTO {

    private Integer id;
    private String placaVeiculo;
    private LocalDateTime horaEntrada;
    private LocalDateTime horaSaida;
    private TipoVeiculo tipoVeiculo;
    private TipoTicket tipoTicket;
    private double valorTotalPagar;
}