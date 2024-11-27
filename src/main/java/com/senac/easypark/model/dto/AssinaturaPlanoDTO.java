package com.senac.easypark.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssinaturaPlanoDTO {
    private Integer id;
    // Adicionando campos para IDs
    // Campos existentes para retorno de informações completas
    private UsuarioDTO usuarioDTO;
    private PlanoDTO planoDTO;
    private LocalDateTime dataPagamento;
    private LocalDateTime dataVencimento;
    private boolean ativo;
}



