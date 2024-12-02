package com.senac.easypark.controller;



import java.util.List;


import com.senac.easypark.exception.EstacionamentoException;
import com.senac.easypark.util.validacao.ValidarTipoAcesso;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.senac.easypark.model.dto.UsuarioDTO;
import com.senac.easypark.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ValidarTipoAcesso validarTipoAcesso;

    @PostMapping
    public ResponseEntity<UsuarioDTO> criarUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) throws EstacionamentoException {
        validarTipoAcesso.validarSeExisteUsuario();
        UsuarioDTO novoUsuario = usuarioService.criarUsuario(usuarioDTO);
        return new ResponseEntity<>(novoUsuario, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> buscarUsuarioPorId(@PathVariable Integer id) throws EstacionamentoException {
        validarTipoAcesso.validarSeExisteUsuario();
        UsuarioDTO usuario = usuarioService.buscarUsuarioPorId(id);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios() throws EstacionamentoException {
        validarTipoAcesso.validarSeExisteUsuario();
        List<UsuarioDTO> usuarios = usuarioService.listarUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<UsuarioDTO> atualizarUsuario(@Valid @PathVariable Integer id, @RequestBody UsuarioDTO usuarioDTO) throws EstacionamentoException {
        validarTipoAcesso.validarSeExisteUsuario();
        UsuarioDTO usuarioAtualizado = usuarioService.atualizarUsuario(id, usuarioDTO);
        return ResponseEntity.ok(usuarioAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Integer id) throws EstacionamentoException {
        validarTipoAcesso.validarSeExisteUsuario();
        usuarioService.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<UsuarioDTO> buscarUsuarioPorCpf(@Valid @PathVariable String cpf) throws EstacionamentoException {
        validarTipoAcesso.validarSeExisteUsuario();
        UsuarioDTO usuarios = usuarioService.buscarUsuarioPorCpf(cpf);
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<List<UsuarioDTO>> buscarUsuarioPorEmail(@PathVariable String email) throws EstacionamentoException {
        validarTipoAcesso.validarSeExisteUsuario();
        List<UsuarioDTO> usuarios = usuarioService.buscarUsuarioPorEmail(email);
        return ResponseEntity.ok(usuarios);
    }

   // @PostMapping("/filtro")
   // public List<UsuarioDTO> pesquisarComSeletor(@RequestBody UsuarioSeletor seletor) throws EstacionamentoException {
    //    validarTipoAcesso.validarSeExisteUsuario();
   //     return usuarioService.pesquisarComSeletor(seletor);
   // }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }


}
