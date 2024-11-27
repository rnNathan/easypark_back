package com.senac.easypark.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.senac.easypark.model.entities.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer>, JpaSpecificationExecutor<Usuario> {

    Usuario findByCpf(String cpf);

    List<Usuario> findByEmail(String email);

    Usuario findByTelefone(String telefone);


}
