package com.senac.easypark.model.dto;

import com.senac.easypark.model.enums.TipoAcesso;

public record RegisterDTO (String email, String senha, String nome, TipoAcesso tipoAcesso) {

}