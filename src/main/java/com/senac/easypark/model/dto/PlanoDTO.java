package com.senac.easypark.model.dto;

import com.senac.easypark.model.enums.TipoPlano;
import com.senac.easypark.model.enums.TipoVeiculo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanoDTO {
    private Integer id;
    private TipoPlano tipoPlano;
    private TipoVeiculo tipoVeiculo;
    /*private LocalDateTime dataPagamento;
    private LocalDateTime dataVencimento;
    private boolean status;*/
    private double valorPlano;

}
