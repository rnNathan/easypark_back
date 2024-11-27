package com.senac.easypark.model.entities;

import com.senac.easypark.model.enums.TipoVeiculo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioTicketsFechados {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime horaEntrada;
    private LocalDateTime horaSaida;

    @Enumerated(EnumType.STRING)
    private TipoVeiculo tipoVeiculo;

    private double valorTotalPagar;

    // Você pode adicionar outros campos se necessário
}