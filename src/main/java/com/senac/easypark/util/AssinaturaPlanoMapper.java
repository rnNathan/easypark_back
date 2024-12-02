package com.senac.easypark.util;

import com.senac.easypark.model.dto.AssinaturaPlanoDTO;
import com.senac.easypark.model.entities.AssinaturaPlano;
import com.senac.easypark.model.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AssinaturaPlanoMapper {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public AssinaturaPlanoMapper(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public AssinaturaPlanoDTO toDTO(AssinaturaPlano entity) {
        // Your existing mapping logic...
    }

    public AssinaturaPlano toEntity(AssinaturaPlanoDTO dto) {
        // Your existing mapping logic...
    }
}
