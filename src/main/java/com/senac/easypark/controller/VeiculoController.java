package com.senac.easypark.controller;

import java.util.List;

import com.senac.easypark.exception.EstacionamentoException;
import com.senac.easypark.util.validacao.ValidarTipoAcesso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.senac.easypark.model.dto.VeiculoDTO;
import com.senac.easypark.service.VeiculoService;

@RestController
@RequestMapping("/veiculos")
public class VeiculoController {

    @Autowired
    private VeiculoService veiculoService;

    @Autowired
    private ValidarTipoAcesso validarTipoAcesso;

    @PostMapping
    public ResponseEntity<VeiculoDTO> criarVeiculo(@RequestBody VeiculoDTO veiculoDTO) throws EstacionamentoException {
        validarTipoAcesso.validarSeExisteUsuario();
        // Lógica para criar um novo veículo
        VeiculoDTO novoVeiculo = veiculoService.criarVeiculo(veiculoDTO);
        return ResponseEntity.ok(novoVeiculo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VeiculoDTO> buscarVeiculoPorId(@PathVariable Integer id) throws EstacionamentoException {
        // Lógica para buscar um veículo por ID
        validarTipoAcesso.validarSeExisteUsuario();
        VeiculoDTO veiculo = veiculoService.buscarVeiculoPorId(id);
        return ResponseEntity.ok(veiculo);
    }

    @GetMapping
    public ResponseEntity<List<VeiculoDTO>> listarVeiculos() throws EstacionamentoException {
        validarTipoAcesso.validarSeExisteUsuario();
        // Chama o serviço para listar todos os veículos e converte para DTOs
        List<VeiculoDTO> veiculos = veiculoService.listarVeiculos();
        return ResponseEntity.ok(veiculos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VeiculoDTO> atualizarVeiculo(@PathVariable Integer id, @RequestBody VeiculoDTO veiculoDTO) throws EstacionamentoException {
        // Lógica para atualizar um veículo
        validarTipoAcesso.validarSeExisteUsuario();
        VeiculoDTO veiculoAtualizado = veiculoService.atualizarVeiculo(veiculoDTO);
        if (veiculoAtualizado != null) {
            return ResponseEntity.ok(veiculoAtualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/ocupados")
    public ResponseEntity<List<VeiculoDTO>> listarVeiculosOcupandoVaga() throws EstacionamentoException {
        validarTipoAcesso.validarSeExisteUsuario();
        // Lógica para listar veículos ocupando vaga
        List<VeiculoDTO> veiculosOcupados = veiculoService.listarVeiculosOcupandoVaga();
        return ResponseEntity.ok(veiculosOcupados);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
