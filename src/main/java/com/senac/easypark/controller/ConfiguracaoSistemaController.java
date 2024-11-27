package com.senac.easypark.controller;

import java.util.List;

import com.senac.easypark.exception.EstacionamentoException;
import com.senac.easypark.util.validacao.ValidarTipoAcesso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.senac.easypark.model.dto.ConfiguracaoSistemaDTO;
import com.senac.easypark.service.ConfiguracaoSistemaService;

@RestController
@RequestMapping("/configuracoes")
public class ConfiguracaoSistemaController {

    @Autowired
    private ConfiguracaoSistemaService configuracaoSistemaService;

    @Autowired
    private ValidarTipoAcesso validarTipoAcesso;

    @PostMapping
    public ResponseEntity<ConfiguracaoSistemaDTO> criarConfiguracao(@RequestBody ConfiguracaoSistemaDTO configuracaoDTO) throws EstacionamentoException {
        validarTipoAcesso.validarAcessoAdmin();
        ConfiguracaoSistemaDTO novaConfiguracao = configuracaoSistemaService.criarConfiguracao(configuracaoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaConfiguracao);
    }

    @GetMapping("/atual")
    public ResponseEntity<ConfiguracaoSistemaDTO> buscarConfiguracaoAtual() throws EstacionamentoException {
        validarTipoAcesso.validarSeExisteUsuario();
        ConfiguracaoSistemaDTO configuracao = configuracaoSistemaService.buscarConfiguracaoAtual();
        return ResponseEntity.ok(configuracao);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConfiguracaoSistemaDTO> atualizarConfiguracao(@PathVariable Integer id, @RequestBody ConfiguracaoSistemaDTO configuracaoDTO) throws EstacionamentoException {
        validarTipoAcesso.validarAcessoAdmin();
        ConfiguracaoSistemaDTO configuracaoAtualizada = configuracaoSistemaService.atualizarConfiguracao(id, configuracaoDTO);
        return ResponseEntity.ok(configuracaoAtualizada);
    }

    @GetMapping
    public ResponseEntity<List<ConfiguracaoSistemaDTO>> listarTodasConfiguracoes() {
        List<ConfiguracaoSistemaDTO> configuracoes = configuracaoSistemaService.listarTodasConfiguracoes();
        return ResponseEntity.ok(configuracoes);
    }

    @PostMapping("/ativar-contagem")
    public boolean ativarContagemEMostrar(@RequestParam boolean mostrar) {
        boolean retorno = false;
        if (mostrar) {
            configuracaoSistemaService.ativarContagemEMostrar(mostrar);
            retorno = true;
        }
        return retorno;
    }
}
