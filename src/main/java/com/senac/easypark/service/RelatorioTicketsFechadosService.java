package com.senac.easypark.service;


import com.senac.easypark.model.dto.RelatorioTicketsFechadosDTO;
import com.senac.easypark.model.entities.RelatorioTicketsFechados;
import com.senac.easypark.model.enums.TipoVeiculo;
import com.senac.easypark.model.repository.RelatorioTicketsFechadosRepository;
import com.senac.easypark.util.RelatorioTicketsFechadosMapper; // Certifique-se de ter o mapper
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RelatorioTicketsFechadosService {

    private final RelatorioTicketsFechadosRepository relatorioRepository;

    @Autowired
    public RelatorioTicketsFechadosService(RelatorioTicketsFechadosRepository relatorioRepository) {
        this.relatorioRepository = relatorioRepository;
    }

    public List<RelatorioTicketsFechadosDTO> listarRelatorios() {
        return relatorioRepository.findAll().stream()
                .map(RelatorioTicketsFechadosMapper::toDTO)
                .collect(Collectors.toList());
    }

    public RelatorioTicketsFechadosDTO buscarRelatorioPorId(Integer id) {
        RelatorioTicketsFechados relatorio = relatorioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Relatório não encontrado"));
        return RelatorioTicketsFechadosMapper.toDTO(relatorio);
    }

    public List<RelatorioTicketsFechadosDTO> buscarPorTipoVeiculo(TipoVeiculo tipoVeiculo){
        return relatorioRepository.findByTipoVeiculo(tipoVeiculo).stream()
                .map(RelatorioTicketsFechadosMapper::toDTO)
                .collect(Collectors.toList());
    }
}
