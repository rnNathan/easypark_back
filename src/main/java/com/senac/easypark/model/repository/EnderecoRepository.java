package com.senac.easypark.model.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.senac.easypark.model.entities.Endereco;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Integer> {

    List<Endereco> findByCep(String cep);
}