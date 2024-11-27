package com.senac.easypark.controller;


import com.senac.easypark.exception.EstacionamentoException;
import com.senac.easypark.model.dto.RelatorioTicketsFechadosDTO;
import com.senac.easypark.model.enums.TipoVeiculo;
import com.senac.easypark.service.RelatorioTicketsFechadosService; // Você precisará criar este serviço
import com.senac.easypark.util.validacao.ValidarTipoAcesso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/relatorios")
public class RelatorioTicketsFechadosController {

    private final ValidarTipoAcesso validarTipoAcesso;
    private final RelatorioTicketsFechadosService relatorioService;

    @Autowired
    public RelatorioTicketsFechadosController(ValidarTipoAcesso validarTipoAcesso, RelatorioTicketsFechadosService relatorioService) {
        this.validarTipoAcesso = validarTipoAcesso;
        this.relatorioService = relatorioService;
    }

    //Endpoint
    @GetMapping("/tickets-fechados")
    public ResponseEntity<List<RelatorioTicketsFechadosDTO>> listarRelatorios() throws EstacionamentoException {
        validarTipoAcesso.validarAcessoAdmin();
        return ResponseEntity.ok(relatorioService.listarRelatorios()); // Você precisará implementar este método no serviço
    }

    //Endpoint
    @GetMapping("/tickets-fechados/{id}")
    public ResponseEntity<RelatorioTicketsFechadosDTO> buscarRelatorioPorId(@PathVariable Integer id) throws EstacionamentoException {
        validarTipoAcesso.validarAcessoAdmin();
        return ResponseEntity.ok(relatorioService.buscarRelatorioPorId(id)); // Você precisará implementar este método no serviço
    }

    @GetMapping("/tickets-fchado-tipo/{tipoVeiculo}")
    public ResponseEntity <List<RelatorioTicketsFechadosDTO>> listarRelatorioTipo(@PathVariable TipoVeiculo tipoVeiculo) throws EstacionamentoException {
        validarTipoAcesso.validarAcessoAdmin();
        return ResponseEntity.ok(relatorioService.buscarPorTipoVeiculo(tipoVeiculo));
    }
}