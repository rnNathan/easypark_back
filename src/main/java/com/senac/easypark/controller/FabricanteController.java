package com.senac.easypark.controller;

import com.senac.easypark.model.dto.FabricanteDTO;
import com.senac.easypark.service.FabricanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fabricantes")
public class FabricanteController {

    @Autowired
    private FabricanteService fabricanteService;

    @PostMapping
    public ResponseEntity<FabricanteDTO> criar(@RequestBody FabricanteDTO fabricanteDTO) {
        return ResponseEntity.ok(fabricanteService.criar(fabricanteDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FabricanteDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(fabricanteService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<FabricanteDTO>> listarTodos() {
        return ResponseEntity.ok(fabricanteService.listarTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<FabricanteDTO> atualizar(@PathVariable Integer id, @RequestBody FabricanteDTO fabricanteDTO) {
        return ResponseEntity.ok(fabricanteService.atualizar(id, fabricanteDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        fabricanteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

