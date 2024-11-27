package com.senac.easypark.service;



import java.util.List;
import java.util.stream.Collectors;

import com.senac.easypark.util.EnderecoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.senac.easypark.model.dto.EnderecoDTO;
import com.senac.easypark.model.entities.Endereco;
import com.senac.easypark.model.repository.EnderecoRepository;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Transactional(readOnly = true)
    public List<EnderecoDTO> findAll() {
        List<Endereco> enderecos = enderecoRepository.findAll();
        return enderecos.stream().map(EnderecoMapper::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EnderecoDTO findById(Integer id) {
        Endereco endereco = enderecoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado"));
        return EnderecoMapper.toDTO(endereco);
    }

    @Transactional(readOnly = true)
    public List<EnderecoDTO> findByCep(String cep) {
        List<Endereco> enderecos = enderecoRepository.findByCep(cep);
        return enderecos.stream().map(EnderecoMapper::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public EnderecoDTO create(EnderecoDTO enderecoDTO) {
        validarEndereco(enderecoDTO);
        Endereco endereco = EnderecoMapper.toEntity(enderecoDTO);
        endereco = enderecoRepository.save(endereco);
        return EnderecoMapper.toDTO(endereco);
    }

    @Transactional
    public EnderecoDTO update(Integer id, EnderecoDTO enderecoDTO) {
        Endereco endereco = enderecoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado"));
        EnderecoMapper.updateEnderecoFromDTO(endereco, enderecoDTO);
        endereco = enderecoRepository.save(endereco);
        return EnderecoMapper.toDTO(endereco);
    }

    @Transactional
    public void delete(Integer id) {
        enderecoRepository.deleteById(id);
    }


    public void updateEnderecoFromDTO(Endereco endereco, EnderecoDTO enderecoDTO) {
        endereco.setCidade(enderecoDTO.getCidade());
        endereco.setEstado(enderecoDTO.getEstado());
        endereco.setCep(enderecoDTO.getCep());
    }

    private void validarEndereco(EnderecoDTO enderecoDTO) {
        if (enderecoDTO.getCidade() == null || enderecoDTO.getCidade().trim().isEmpty()) {
            throw new IllegalArgumentException("Cidade não pode ser vazia");
        }
        if (enderecoDTO.getEstado() == null || enderecoDTO.getEstado().trim().isEmpty()) {
            throw new IllegalArgumentException("Estado não pode ser vazio");
        }
        if (enderecoDTO.getCep() == null || !enderecoDTO.getCep().matches("^\\d{5}-\\d{3}$")) {
            throw new IllegalArgumentException("CEP inválido. Use o formato: XXXXX-XXX");
        }
    }
}

