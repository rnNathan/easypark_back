package com.senac.easypark.model.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.senac.easypark.model.entities.Veiculo;

@Repository
public interface VeiculoRepository extends JpaRepository<Veiculo, Integer> {

    List<Veiculo> findByOcupandoVagaTrue();
    // Método adicional que pode ser útil
    List<Veiculo> findByOcupandoVagaFalse();

    List<Veiculo> findByTipoVeiculo(String tipoVeiculo);

    Optional<Veiculo> findByPlaca(String placaVeiculo);

    List<Veiculo> findByUsuarioId(Integer usuarioId);

    @Query(value = """
     SELECT v.* 
     FROM veiculo v
     INNER JOIN usuario u ON v.usuario_id = u.id
     INNER JOIN assinatura_plano ap ON ap.usuario_id = u.id
     INNER JOIN plano p ON ap.plano_id = p.id
     WHERE v.placa = :placa
     AND ap.ativo = true
     AND ap.data_vencimento >= NOW()
     AND p.tipo_veiculo = v.tipo_veiculo
     """, nativeQuery = true)
    List<Veiculo> buscarVeiculoComAssinaturaValida(@Param("placa") String placa);
}
