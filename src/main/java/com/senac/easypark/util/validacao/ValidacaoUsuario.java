package com.senac.easypark.util.validacao;


import com.senac.easypark.model.dto.UsuarioDTO;
import com.senac.easypark.model.entities.Usuario;
import com.senac.easypark.exception.EstacionamentoException;
import com.senac.easypark.model.repository.UsuarioRepository;
import com.senac.easypark.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;


@Component
public class ValidacaoUsuario {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    @Lazy
    private UsuarioService usuarioService;

    public void validarSeExisteCampoNoBancoDados(UsuarioDTO dto) throws EstacionamentoException {
        Usuario usuarioExistente = usuarioRepository.findByCpf(dto.getCpf());

        if (usuarioExistente != null) {
            throw new EstacionamentoException("CPF JÁ CADASTRADO");
        }
        //Verifique se já existe um usuário com o mesmo telefone
        Usuario usuarioComMesmoTelefone = usuarioRepository.findByTelefone(dto.getTelefone());
        if (usuarioComMesmoTelefone != null) {
            throw new EstacionamentoException("Número de telefone já existe na base de dados");
        }
    }

    public void validarCamposUsuario(UsuarioDTO usuarioDTO) throws EstacionamentoException {
        if (usuarioDTO.getNome() == null || usuarioDTO.getNome().trim().isEmpty()) {
            throw new EstacionamentoException("Nome do usuário não pode ser vazio");
        }
        if (usuarioDTO.getEmail() == null || !usuarioDTO.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new EstacionamentoException("Email inválido");
        }
        if (usuarioDTO.getCpf() == null) {
            throw new EstacionamentoException("CPF inválido");
        }
        if (usuarioDTO.getTelefone() == null) {
            throw new EstacionamentoException("Número de telefone incorreto");
        }
        if (usuarioDTO.getEndereco() == null) {
            throw new EstacionamentoException("Endereço precisa ser informato.");
        }
    }
}
