package com.senac.easypark.model.repository;

import com.senac.easypark.model.entities.Plano;
import com.senac.easypark.model.enums.TipoVeiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanoRepository extends JpaRepository<Plano, Integer> {
    List<Plano>findByTipoVeiculo(TipoVeiculo tipoVeiculo);
}

