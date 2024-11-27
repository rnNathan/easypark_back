package com.senac.easypark.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private Integer id;
    private String nome;
    private String email;
    private String telefone;
    private String cpf;
    private EnderecoDTO endereco;          //Representação simplificada do Endereco
    private List<VeiculoDTO> veiculosDTO; // Pode ser o DTO de Veiculo, ou uma lista simples com dados relevantes
    /* private PlanoDTO planoDTO; */
    private List<AssinaturaPlanoDTO> assinaturas;
}
