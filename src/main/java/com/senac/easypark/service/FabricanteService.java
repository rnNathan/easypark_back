package com.senac.easypark.service;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.senac.easypark.model.dto.FabricanteDTO;
import com.senac.easypark.model.entities.Fabricante;
import com.senac.easypark.model.repository.FabricanteRepository;
import com.senac.easypark.util.FabricanteMapper;

@Service
public class FabricanteService {

    @Autowired
    private FabricanteRepository fabricanteRepository;
    @Autowired
    private FabricanteMapper fabricanteMapper;

    public FabricanteDTO criar(FabricanteDTO fabricanteDTO) {
        validarAno(fabricanteDTO.getAno());
        Fabricante fabricante = new Fabricante();
        fabricante.setModelo(fabricanteDTO.getModelo());
        fabricante.setMarca(fabricanteDTO.getMarca());
        fabricante.setAno(fabricanteDTO.getAno());
        return FabricanteMapper.toDTO(fabricanteRepository.save(fabricante));
    }

    public FabricanteDTO buscarPorId(Integer id) {
        return fabricanteRepository.findById(id)
                .map(FabricanteMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Fabricante não encontrado"));
    }

    public List<FabricanteDTO> listarTodos() {
        return fabricanteRepository.findAll().stream()
                .map(FabricanteMapper::toDTO)
                .collect(Collectors.toList());
    }

    public FabricanteDTO atualizar(Integer id, FabricanteDTO fabricanteDTO) {
        validarAno(fabricanteDTO.getAno());
        Fabricante fabricante = fabricanteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fabricante não encontrado"));
        FabricanteMapper.updateEntityFromDTO(fabricante, fabricanteDTO);
        return FabricanteMapper.toDTO(fabricanteRepository.save(fabricante));
    }

    public void deletar(Integer id) {
        fabricanteRepository.deleteById(id);
    }

    private void validarAno(int ano) {
        if (ano < 1886) {
            throw new IllegalArgumentException("O ano do fabricante deve ser a partir de 1886");
        }
    }
}

