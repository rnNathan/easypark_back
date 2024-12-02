package com.senac.easypark.controller;

import com.senac.easypark.model.dto.AssinaturaPlanoDTO;
import com.senac.easypark.exception.EstacionamentoException;
import com.senac.easypark.service.AssinaturaPlanoService;
import com.senac.easypark.util.validacao.ValidarTipoAcesso;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/assinaturas")
@RequiredArgsConstructor
public class AssinaturaPlanoController {

    private final AssinaturaPlanoService assinaturaPlanoService;

    @Autowired
    private ValidarTipoAcesso validarTipoAcesso; //Puxando o util para validar o acesso

    @GetMapping
    public ResponseEntity<List<AssinaturaPlanoDTO>> findAll() throws EstacionamentoException {
        validarTipoAcesso.validarSeExisteUsuario();
        return ResponseEntity.ok(assinaturaPlanoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssinaturaPlanoDTO> findById(@PathVariable Integer id) throws EstacionamentoException {
        validarTipoAcesso.validarSeExisteUsuario();
        return ResponseEntity.ok(assinaturaPlanoService.findById(id));
    }

    @PostMapping("/assinaturas")
    public ResponseEntity<AssinaturaPlanoDTO> create(@RequestBody AssinaturaPlanoDTO dto) throws EstacionamentoException {
        validarTipoAcesso.validarSeExisteUsuario(); //VALIDA SE TEM UM TOKEN, SÓ CONTINUA SE HOUVER UM TOKEN
        AssinaturaPlanoDTO created = assinaturaPlanoService.create(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(uri).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssinaturaPlanoDTO> update(@PathVariable Integer id, @RequestBody AssinaturaPlanoDTO dto) throws EstacionamentoException {
        validarTipoAcesso.validarAcessoAdmin();  // AQUI SÓ VAI ATUALIZAR SE FOR ADMIN
        return ResponseEntity.ok(assinaturaPlanoService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws EstacionamentoException {
        validarTipoAcesso.validarAcessoAdmin();
        assinaturaPlanoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}