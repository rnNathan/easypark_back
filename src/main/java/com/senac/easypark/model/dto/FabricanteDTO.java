package com.senac.easypark.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FabricanteDTO {
    private Integer id;
    private String modelo;
    private String marca;
    private int ano;
}