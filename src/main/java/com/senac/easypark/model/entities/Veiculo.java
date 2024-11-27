package com.senac.easypark.model.entities;


import com.senac.easypark.model.enums.TipoVeiculo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String placa;

    @Enumerated(EnumType.STRING)
    private TipoVeiculo tipoVeiculo;

    private boolean ocupandoVaga;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    //@JsonIgnore
    private Usuario usuario;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "fabricante_id")
    private Fabricante fabricante;
}
