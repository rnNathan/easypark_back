package com.senac.easypark.service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


import com.senac.easypark.model.entities.AssinaturaPlano;
import com.senac.easypark.model.enums.TipoVeiculo;
import com.senac.easypark.exception.EstacionamentoException;
import com.senac.easypark.model.repository.AssinaturaPlanoRepository;
import com.senac.easypark.util.PlanoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.senac.easypark.model.dto.PlanoDTO;
import com.senac.easypark.model.entities.Plano;
import com.senac.easypark.model.repository.PlanoRepository;

@Service
public class PlanoService {

    @Autowired
    private PlanoRepository planoRepository;
    @Autowired
    private AssinaturaPlanoRepository assinaturaPlanoRepository;

    // Criar um novo plano
    @Transactional
    public PlanoDTO criarPlano(PlanoDTO planoDTO) {
        validarPlano(planoDTO);
        Plano plano = PlanoMapper.convertToEntity(planoDTO);
        Plano planoSalvo = planoRepository.save(plano);
        return PlanoMapper.convertToDTO(planoSalvo);
    }

    // Buscar plano pelo ID
    public PlanoDTO buscarPlanoPorId(Integer id) {
        return planoRepository.findById(id)
                .map(PlanoMapper::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Plano não encontrado"));
    }

    // Listar todos os planos
    public List<PlanoDTO> listarPlanos() {
        return planoRepository.findAll().stream()
                .map(PlanoMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    // Atualizar um plano
    @Transactional
    public PlanoDTO atualizarPlano(Integer id, PlanoDTO planoDTO) {
        validarPlano(planoDTO);
        return planoRepository.findById(id)
                .map(plano -> {
                    PlanoMapper.updatePlanoFromDTO(plano, planoDTO);
                    return PlanoMapper.convertToDTO(planoRepository.save(plano));
                })
                .orElseThrow(() -> new RuntimeException("Plano não encontrado"));
    }

    // Deletar um plano
    @Transactional
    public boolean deletarPlano(Integer id) throws EstacionamentoException {
        boolean retorno = false;
        Optional<List<AssinaturaPlano>> assinantes = assinaturaPlanoRepository.findByPlanoId(id);

        Plano plano = planoRepository.findById(id).orElseThrow(() -> new EstacionamentoException("Plano nao encontrado"));

        if (assinantes.get().isEmpty()) {
            planoRepository.deleteById(id);
            retorno = true;
        } else{
            throw new EstacionamentoException("Nao pode excluir um plano, pois tem assinantes associado.");
        }

        return retorno;
    }

    public List<PlanoDTO> listarPorTipoVeiculo(TipoVeiculo tipoVeiculo){
        List<Plano> planos = planoRepository.findByTipoVeiculo(tipoVeiculo);
        return planos.stream().map(PlanoMapper::convertToDTO).collect(Collectors.toList());
    }

    // Validação básica de plano
    private void validarPlano(PlanoDTO planoDTO) {
        if (planoDTO.getTipoPlano() == null) {
            throw new IllegalArgumentException("Tipo de plano não pode ser nulo");
        }
        if (planoDTO.getTipoVeiculo() == null) {
            throw new IllegalArgumentException("Tipo de veículo não pode ser nulo");
        }
        if (planoDTO.getValorPlano() <= 0) {
            throw new IllegalArgumentException("Valor do plano deve ser maior que 0");
        }

    }

}

