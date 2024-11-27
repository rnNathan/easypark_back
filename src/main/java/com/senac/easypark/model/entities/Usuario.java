package com.senac.easypark.model.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;


import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @NotNull
    private String nome;

    @NotNull
    @Email(message = "E-mail deve ser valído! formato: exemplo@dominio.com, user.name@empresa.org, usuario+teste@exemplo.net")
    private String email;

    @NotNull
    private String telefone;

    @NotNull
    @CPF(message = "CPF não existe ou digitado incorretamente")
    private String cpf;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<AssinaturaPlano> assinaturas;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Veiculo> veiculos; // Relacionamento de Usuario com Veiculo


}

