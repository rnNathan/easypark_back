package com.senac.easypark.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.senac.easypark.exception.EstacionamentoException;
import com.senac.easypark.model.repository.PlanoRepository;
import com.senac.easypark.util.EnderecoMapper;
import com.senac.easypark.util.UsuarioMapper;
import com.senac.easypark.util.validacao.ValidacaoUsuario;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.senac.easypark.model.dto.UsuarioDTO;
import com.senac.easypark.model.entities.Usuario;
import com.senac.easypark.model.entities.Endereco;
import com.senac.easypark.model.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private final ValidacaoUsuario validacaoUsuario;

    @Autowired
    private PlanoRepository planoRepository;


    public UsuarioService(ValidacaoUsuario validacaoUsuario) {
        this.validacaoUsuario = validacaoUsuario;
    }


    // Criar um novo usuário
    @Transactional
    public UsuarioDTO criarUsuario(UsuarioDTO usuarioDTO) throws EstacionamentoException {
        validacaoUsuario.validarCamposUsuario(usuarioDTO);
        validacaoUsuario.validarSeExisteCampoNoBancoDados(usuarioDTO);
        Usuario usuario = UsuarioMapper.toEntity(usuarioDTO);
        Usuario savedUsuario = usuarioRepository.save(usuario);
        return UsuarioMapper.toDTO(savedUsuario);
    }

    // Buscar um usuário pelo ID
    public UsuarioDTO buscarUsuarioPorId(Integer id) {
        return usuarioRepository.findById(id)
                .map(UsuarioMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    // Listar todos os usuários
    public List<UsuarioDTO> listarUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(UsuarioMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Atualizar um usuário

    public UsuarioDTO atualizarUsuario(Integer id, UsuarioDTO usuarioDTO) throws EstacionamentoException {
        validacaoUsuario.validarCamposUsuario(usuarioDTO);
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);

        if (!usuarioOpt.isPresent()) {
            throw new RuntimeException("Usuário não encontrado");
        }

        Usuario usuario = usuarioOpt.get();

        // Copia as propriedades do DTO para o usuário existente, exceto o id
        BeanUtils.copyProperties(usuarioDTO, usuario, "id");
        // Atualiza o Endereco, se presente no DTO
        if (usuarioDTO.getEndereco() != null) {
            Endereco enderecoExistente = usuario.getEndereco();
            if (enderecoExistente != null && enderecoExistente.getId() != null) {
                //Atualiza as propriedades do Endereco sem modificar o id
                enderecoExistente.setCep(usuarioDTO.getEndereco().getCep());
                enderecoExistente.setCidade(usuarioDTO.getEndereco().getCidade());
                enderecoExistente.setEstado(usuarioDTO.getEndereco().getEstado());
            } else {
                //Caso o Endereco não exista ou o id seja nulo, cria um novo
                usuario.setEndereco(EnderecoMapper.toEntity(usuarioDTO.getEndereco()));
            }
        }

        // Salva o usuário atualizado
        return UsuarioMapper.toDTO(usuarioRepository.save(usuario));
    }

    // Deletar um usuário
    public void deletarUsuario(Integer id) {
        usuarioRepository.deleteById(id);
    }


    // Buscar usuários por CPF
    public UsuarioDTO buscarUsuarioPorCpf(String cpf) throws EstacionamentoException {
        Usuario usuario = usuarioRepository.findByCpf(cpf);
        if (usuario == null) {
            throw new EstacionamentoException("Não foi encontrado usuario com cpf:" + cpf);
        }
        return UsuarioMapper.toDTO(usuario);
    }

    //Buscar usuários por Email
    public List<UsuarioDTO> buscarUsuarioPorEmail(String email) {
        List<Usuario> usuarios = usuarioRepository.findByEmail(email);
        List<UsuarioDTO> usuariosDTO = new ArrayList<>();

        for (Usuario usuario : usuarios) {
            UsuarioDTO usuarioDTO = UsuarioMapper.toDTO(usuario);
            usuariosDTO.add(usuarioDTO);
        }

        return usuariosDTO;
    }
    // public List<UsuarioDTO> pesquisarComSeletor(UsuarioSeletor seletor) {
        // if(seletor != null && seletor.temPaginacao()) {
           // int pageNumber = seletor.getPagina();
          //  int pageSize = seletor.getLimite();

            //Ler com atenção a documentação:
            // @param pageNumber zero-based page number, must not be negative.
            // @param pageSize the size of the page to be returned, must be greater than 0.
        //    PageRequest pagina = PageRequest.of(pageNumber - 1, pageSize);
      //      return UsuarioMapper.toDTOList(usuarioRepository.findAll(seletor, pagina).toList());
     //   }

        //https://www.baeldung.com/spring-data-jpa-query-by-example
   //     return UsuarioMapper.toDTOList(usuarioRepository.findAll(seletor));
 //   }


}

