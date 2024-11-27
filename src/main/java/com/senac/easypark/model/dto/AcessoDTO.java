package com.senac.easypark.model.dto;

import com.senac.easypark.model.enums.TipoAcesso;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcessoDTO {
    private Integer id;
    private String username;
    private String senha;
    private String email;
    private TipoAcesso tipoAcesso;
    // Construtores, getters e setters

}