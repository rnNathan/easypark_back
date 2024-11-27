package com.senac.easypark.controller;

import com.senac.easypark.exception.EstacionamentoException;
import com.senac.easypark.model.dto.AcessoDTO;
import com.senac.easypark.model.enums.TipoAcesso;
import com.senac.easypark.service.AcessoService;
import com.senac.easypark.util.validacao.ValidarTipoAcesso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/acesso")
public class AcessoController {


    @Autowired
    private AcessoService acessoService;

    @Autowired
    private ValidarTipoAcesso validarTipoAcesso;

    @GetMapping("/by-email")
    public ResponseEntity<AcessoDTO> getUserByEmail(@RequestParam String email) throws EstacionamentoException {
        validarTipoAcesso.validarAcessoAdmin();
        AcessoDTO acessoDTO = acessoService.getByEmail(email);
        return ResponseEntity.ok(acessoDTO);
    }

    @PostMapping
    public ResponseEntity<AcessoDTO> save(@RequestBody AcessoDTO userDTO) throws EstacionamentoException {
        validarTipoAcesso.validarAcessoAdmin();
        AcessoDTO acessoDTO = acessoService.save(userDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(acessoDTO);
    }

    @GetMapping
    public ResponseEntity<List<AcessoDTO>> findAll() throws EstacionamentoException {
        validarTipoAcesso.validarAcessoAdmin();
        List<AcessoDTO> acessoDTOS = acessoService.findAll();

        return ResponseEntity.ok(acessoDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AcessoDTO> findById(@PathVariable Integer id) throws EstacionamentoException {
        validarTipoAcesso.validarAcessoAdmin();
        AcessoDTO acessoDTO = acessoService.findById(id);
        return ResponseEntity.ok(acessoDTO);
    }

    @PutMapping
    public ResponseEntity<AcessoDTO> update(@RequestBody AcessoDTO userDTO) throws EstacionamentoException {
        validarTipoAcesso.validarAcessoAdmin();
        AcessoDTO acessoDtoUpdate = acessoService.update(userDTO);
        return ResponseEntity.ok(acessoDtoUpdate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) throws EstacionamentoException {
        validarTipoAcesso.validarAcessoAdmin();
        acessoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tipo")
    public ResponseEntity<List<AcessoDTO>> findByTipoAcesso(@RequestParam TipoAcesso tipo) throws EstacionamentoException {
        validarTipoAcesso.validarAcessoAdmin();
        List<AcessoDTO> acessos = acessoService.findByTipoAcesso(tipo);
        return ResponseEntity.ok(acessos);
    }

}