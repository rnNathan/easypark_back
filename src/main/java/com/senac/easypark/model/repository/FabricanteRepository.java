package com.senac.easypark.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.senac.easypark.model.entities.Fabricante;

@Repository
public interface FabricanteRepository extends JpaRepository<Fabricante, Integer> {

    Fabricante findByMarca(String marca);

    List<Fabricante> findByModeloContainingIgnoreCase(String modelo);

    List<Fabricante> findByMarcaContainingIgnoreCase(String marca);

    List<Fabricante> findByAno(Integer ano);

    List<Fabricante> findByMarcaAndModelo(String marca, String modelo);

}