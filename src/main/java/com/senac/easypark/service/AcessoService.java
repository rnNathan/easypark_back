package com.senac.easypark.service;

import com.senac.easypark.exception.EstacionamentoException;
import com.senac.easypark.model.dto.AcessoDTO;
import com.senac.easypark.model.entities.Acesso;
import com.senac.easypark.model.repository.AcessoRepository;
import com.senac.easypark.util.AcessoMapper;
import com.senac.easypark.util.validacao.ValidacaoAcesso;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.senac.easypark.model.enums.TipoAcesso;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AcessoService implements UserDetailsService {

    @Autowired
    private AcessoRepository repository;

    public AcessoDTO save(AcessoDTO userDTO) {

        if (!ValidacaoAcesso.isNotEmpty(userDTO.getEmail())) {
            throw new IllegalArgumentException("O e-mail não pode estar nulo!");
        }

        if (!ValidacaoAcesso.isNotEmpty(userDTO.getSenha())) {
            throw new IllegalArgumentException("A senha não pode estar nula!");
        }

        if (!ValidacaoAcesso.isValidLength(userDTO.getSenha(), 8)) {
            throw new IllegalArgumentException("A senha senha não pode conter menos que 8 caracteres!");
        }

        if (repository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Este e-mail já está cadastrado");
        }

        Acesso user = AcessoMapper.toEntity(userDTO);

        Acesso userSalvo = repository.save(user);

        AcessoDTO salvarAcesso = AcessoMapper.toDTO(userSalvo);

        return salvarAcesso;
    }

    public List<AcessoDTO> findAll() throws EstacionamentoException {
        List<Acesso> users = repository.findAll();

        List<AcessoDTO> usersDTO = users.stream()
                .map(AcessoMapper::toDTO)
                .collect(Collectors.toList());

        if (usersDTO.isEmpty()) {
            throw new EstacionamentoException("Nenhum usuário encontrado.");
        }

        return usersDTO;
    }

    public AcessoDTO findById(Integer id) throws EstacionamentoException {
        Optional<Acesso> userOptional = repository.findById(id);

        if (userOptional.isPresent()) {
            Acesso user = userOptional.get();
            return AcessoMapper.toDTO(user);
        } else {
            throw new EstacionamentoException("Usuário com ID " + id + " não encontrado!");
        }
    }

    // quebrado pq da erro de constraint
    public void deleteById(Integer id) {
        Optional<Acesso> userOptional = repository.findById(id);

        if (userOptional.isPresent()) {
            repository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Usuário com ID " + id + " não encontrado.");
        }
    }

    public AcessoDTO update(AcessoDTO userDTO) {
        Optional<Acesso> userOptional = repository.findById(userDTO.getId());

        if (!userOptional.isPresent()) {
            throw new EntityNotFoundException("Usuário com ID " + userDTO.getId() + " não encontrado");
        }

        Acesso userSalvo = userOptional.get();

        // Mapeando o DTO para a entidade User
        Acesso user = AcessoMapper.toEntity(userDTO);

        // Copiando as propriedades do objeto User atualizado, exceto o "id"
        BeanUtils.copyProperties(user, userSalvo, "id");

        // Salvando o usuário atualizado no banco
        userSalvo = repository.save(userSalvo);

        // Retornando o DTO do usuário atualizado
        return AcessoMapper.toDTO(userSalvo);
    }

    public AcessoDTO getByEmail(String email) {
        Optional<Acesso> userOptional = repository.findByEmail(email);

        if (userOptional.isPresent()) {
            return AcessoMapper.toDTO(userOptional.get());
        } else {
            throw new EntityNotFoundException("Usuário com o email " + email + " não encontrado!");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmail(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException("Usuário não encontrado" + username)
                );
    }

    public List<AcessoDTO> findByTipoAcesso(TipoAcesso tipo) {
        List<Acesso> acessos = repository.findByTipoAcesso(tipo);
        return acessos.stream()
                .map(AcessoMapper::toDTO)
                .collect(Collectors.toList());
    }
}