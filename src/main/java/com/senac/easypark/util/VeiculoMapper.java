package com.senac.easypark.util;

import com.senac.easypark.model.dto.FabricanteDTO;
import com.senac.easypark.model.dto.VeiculoDTO;
import com.senac.easypark.model.entities.Fabricante;
import com.senac.easypark.model.entities.Usuario;
import com.senac.easypark.model.entities.Veiculo;

import com.senac.easypark.model.repository.UsuarioRepository;
import com.senac.easypark.service.FabricanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VeiculoMapper {

    private static UsuarioRepository usuarioRepository;
    private static FabricanteService fabricanteService;

    @Autowired
    public VeiculoMapper(UsuarioRepository usuarioRepository, FabricanteService fabricanteService) {
        VeiculoMapper.usuarioRepository = usuarioRepository;
        VeiculoMapper.fabricanteService = fabricanteService;
    }

    public static VeiculoDTO toDTO(Veiculo veiculo) {
        if (veiculo == null) {
            return null;
        }
        VeiculoDTO dto = new VeiculoDTO();
        dto.setId(veiculo.getId());
        dto.setPlaca(veiculo.getPlaca());
        dto.setTipoVeiculo(veiculo.getTipoVeiculo());
        dto.setOcupandoVaga(veiculo.isOcupandoVaga());

        // Seta o ID do usuário se existir
        if (veiculo.getUsuario() != null) {
            dto.setIdUsuarioDTO(veiculo.getUsuario().getId());
        }
        FabricanteDTO fabricanteDTO = new FabricanteDTO();
        if (veiculo.getFabricante() != null) {
            fabricanteDTO.setId(veiculo.getFabricante().getId());
            fabricanteDTO.setModelo(veiculo.getFabricante().getModelo());
            fabricanteDTO.setMarca(veiculo.getFabricante().getMarca());
            fabricanteDTO.setAno(veiculo.getFabricante().getAno());
        }

        dto.setFabricanteDTO(fabricanteDTO);

        return dto;
    }

    public static Veiculo toEntity(VeiculoDTO dto) {
        if (dto == null) {
            return null;
        }

        Veiculo veiculo = new Veiculo();
        veiculo.setId(dto.getId());
        veiculo.setPlaca(dto.getPlaca());
        veiculo.setTipoVeiculo(dto.getTipoVeiculo());
        veiculo.setOcupandoVaga(dto.isOcupandoVaga());

        // Associa o usuário pelo ID
        if (dto.getIdUsuarioDTO() != null) {
            Usuario usuario = usuarioRepository.findById(dto.getIdUsuarioDTO())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + dto.getIdUsuarioDTO()));
            veiculo.setUsuario(usuario);
        }

        Fabricante fabricante = new Fabricante();

        if (dto.getFabricanteDTO() != null) {
            fabricante.setId(dto.getFabricanteDTO().getId());
            fabricante.setModelo(dto.getFabricanteDTO().getModelo());
            fabricante.setMarca(dto.getFabricanteDTO().getMarca());
            fabricante.setAno(dto.getFabricanteDTO().getAno());

        }
        veiculo.setFabricante(fabricante);


        return veiculo;
    }
    // Converte uma lista de Veiculo para uma lista de VeiculoDTO
    public static List<VeiculoDTO> toDtoList(List<Veiculo> veiculos) {
        return veiculos.stream().map(VeiculoMapper::toDTO).toList();
    }

    // Converte uma lista de VeiculoDTO para uma lista de Veiculo
    public static List<Veiculo> toEntityList(List<VeiculoDTO> veiculosDTO) {
        return veiculosDTO.stream().map(VeiculoMapper::toEntity).toList();
    }

    public static void updateVeiculoFromDTO(Veiculo veiculo, VeiculoDTO veiculoDTO) {
        veiculo.setPlaca(veiculoDTO.getPlaca().toUpperCase()); // Garante que a placa seja atualizada em maiúsculas
        veiculo.setTipoVeiculo(veiculoDTO.getTipoVeiculo());
        veiculo.setOcupandoVaga(veiculoDTO.isOcupandoVaga());
        //veiculo.setFabricante(fabricanteRepository.findById(veiculoDTO.getFabricanteDTO().getId());
        //             .orElseThrow(() -> new RuntimeException("Fabricante não encontrado")));
        if (veiculoDTO.getFabricanteDTO() != null) {
            if (veiculo.getFabricante() == null) {
                veiculo.setFabricante(new Fabricante());
            }
            FabricanteDTO fabricanteDTO = veiculoDTO.getFabricanteDTO();
            FabricanteDTO fabricante = fabricanteService.buscarPorId(fabricanteDTO.getId());
            veiculo.setFabricante(FabricanteMapper.toEntity(fabricante));
        }
    }
}


