package com.senac.easypark.model.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoDTO {
    private Integer id;
    private String cidade;
    private String estado;
    private String cep;
}