package com.senac.easypark.model.repository;

import com.senac.easypark.model.entities.Acesso;
import com.senac.easypark.model.enums.TipoAcesso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AcessoRepository extends JpaRepository<Acesso, Integer>, JpaSpecificationExecutor<Acesso> {
    Acesso findByUsername(String username);

    boolean existsByEmail(String email);

    Optional<Acesso> findByEmail(String email);

    List<Acesso> findByTipoAcesso(TipoAcesso tipo);

}