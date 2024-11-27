package com.senac.easypark.controller;



import java.util.List;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.senac.easypark.model.dto.EnderecoDTO;
import com.senac.easypark.service.EnderecoService;

@RestController
@RequestMapping("/enderecos")
public class EnderecoController {

    @Autowired
    private EnderecoService enderecoService;

    @GetMapping
    public ResponseEntity<List<EnderecoDTO>> findAll() {
        List<EnderecoDTO> enderecos = enderecoService.findAll();
        return ResponseEntity.ok(enderecos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnderecoDTO> findById(@PathVariable Integer id) {
        EnderecoDTO endereco = enderecoService.findById(id);
        return ResponseEntity.ok(endereco);
    }

    @GetMapping("/cep/{cep}")
    public ResponseEntity<List<EnderecoDTO>> findByCep(@PathVariable String cep) {
        List<EnderecoDTO> enderecos = enderecoService.findByCep(cep);
        return ResponseEntity.ok(enderecos);
    }

    @PostMapping
    public ResponseEntity<EnderecoDTO> create(@Valid @RequestBody EnderecoDTO enderecoDTO) {
        EnderecoDTO novoEndereco = enderecoService.create(enderecoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoEndereco);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnderecoDTO> update(@Valid @PathVariable Integer id, @RequestBody EnderecoDTO enderecoDTO) {
        EnderecoDTO enderecoAtualizado = enderecoService.update(id, enderecoDTO);
        return ResponseEntity.ok(enderecoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        enderecoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

