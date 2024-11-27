package com.senac.easypark.controller;


import com.senac.easypark.model.dto.PlanoDTO;
import com.senac.easypark.model.enums.TipoVeiculo;
import com.senac.easypark.exception.EstacionamentoException;
import com.senac.easypark.service.PlanoService;
import com.senac.easypark.util.validacao.ValidarTipoAcesso;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/planos")
public class PlanoController {

    @Autowired
    private PlanoService planoService;
    @Autowired
    private ValidarTipoAcesso validarTipoAcesso;

    @PostMapping
    public ResponseEntity<PlanoDTO> criarPlano(@Valid @RequestBody PlanoDTO planoDTO) throws EstacionamentoException {
        validarTipoAcesso.validarAcessoAdmin();
        PlanoDTO novoPlano = planoService.criarPlano(planoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoPlano);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanoDTO> buscarPlanoPorId(@PathVariable Integer id) throws EstacionamentoException {
        validarTipoAcesso.validarSeExisteUsuario();
        PlanoDTO plano = planoService.buscarPlanoPorId(id);
        return ResponseEntity.ok(plano);
    }

    @GetMapping
    public ResponseEntity<List<PlanoDTO>> listarPlanos() throws EstacionamentoException {
        validarTipoAcesso.validarSeExisteUsuario();
        List<PlanoDTO> planos = planoService.listarPlanos();
        return ResponseEntity.ok(planos);
    }

    @PutMapping("{id}")
    public ResponseEntity<PlanoDTO> atualizarPlano(@Valid @PathVariable Integer id, @RequestBody PlanoDTO planoDTO) throws EstacionamentoException {
        validarTipoAcesso.validarAcessoAdmin();
        return ResponseEntity.ok(planoService.atualizarPlano(id, planoDTO));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Boolean> removerPlano(@PathVariable Integer id) throws EstacionamentoException {
        validarTipoAcesso.validarAcessoAdmin();
        return ResponseEntity.ok(planoService.deletarPlano(id));
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity <List<PlanoDTO>> buscarPlanoPorTipo(@PathVariable TipoVeiculo tipo) throws EstacionamentoException {
        validarTipoAcesso.validarSeExisteUsuario();
        List<PlanoDTO> planos = planoService.listarPorTipoVeiculo(tipo);
        return ResponseEntity.ok(planos);
    }
}

