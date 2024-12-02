package com.senac.easypark.model.entities;

import com.senac.easypark.model.enums.TipoVeiculo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import com.senac.easypark.model.enums.TipoPlano;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Plano {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @Enumerated(EnumType.STRING)
        private TipoPlano tipoPlano;

        @Enumerated(EnumType.STRING)
        private TipoVeiculo tipoVeiculo;

        @NotNull
        private double valorPlano;

        @OneToMany(mappedBy = "plano", cascade = CascadeType.ALL)
        @JsonIgnore
        private List<AssinaturaPlano> assinaturas;





}