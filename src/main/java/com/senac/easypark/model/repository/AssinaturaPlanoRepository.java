package com.senac.easypark.model.repository;

import com.senac.easypark.model.entities.AssinaturaPlano;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AssinaturaPlanoRepository extends JpaRepository<AssinaturaPlano, Integer> {

    @Query("SELECT a FROM AssinaturaPlano a WHERE a.plano.id = :planoId")
    Optional<List<AssinaturaPlano>> findByPlanoId(@Param("planoId") Integer planoId);

    boolean existsByUsuarioIdAndPlanoId(Integer usuarioId, Integer planoId);


}