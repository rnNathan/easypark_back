package com.senac.easypark.model.repository;

import com.senac.easypark.model.entities.RelatorioTicketsFechados;
import com.senac.easypark.model.enums.TipoVeiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RelatorioTicketsFechadosRepository extends JpaRepository<RelatorioTicketsFechados, Integer> {
    List<RelatorioTicketsFechados> findByTipoVeiculo(TipoVeiculo tipoVeiculo);
    // Você pode adicionar métodos personalizados aqui, se necessário
}