package com.carambolos.carambolosapi.infrastructure.persistence.jpa;

import com.carambolos.carambolosapi.infrastructure.persistence.entity.AdicionalDecoracaoEntity;
import com.carambolos.carambolosapi.infrastructure.persistence.projection.AdicionalDecoracaoProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AdicionalDecoracaoRepository extends JpaRepository<AdicionalDecoracaoEntity, Integer> {

    @Query(value = """
            SELECT
            	d.id AS decoracao_id,
                d.nome AS nome_decoracao,
                GROUP_CONCAT(a.descricao ORDER BY a.descricao SEPARATOR ', ') AS adicionais,
                GROUP_CONCAT(a.id ORDER BY a.descricao SEPARATOR ', ') AS adicionais_ids
            FROM teiko.decoracao d
            LEFT JOIN teiko.adicional_decoracao ad ON ad.decoracao_id = d.id
            LEFT JOIN teiko.adicional a ON a.id = ad.adicional_id
            GROUP BY d.id, d.nome;\s
            """, nativeQuery = true)
    List<AdicionalDecoracaoProjection> findAllAdicionaisByDecoracao();

    @Query(value = """
            SELECT
            	d.id AS decoracao_id,
                d.nome AS nome_decoracao,
                GROUP_CONCAT(a.descricao ORDER BY a.descricao SEPARATOR ', ') AS adicionais,
                GROUP_CONCAT(a.id ORDER BY a.descricao SEPARATOR ', ') AS adicionais_ids
            FROM teiko.decoracao d
            LEFT JOIN teiko.adicional_decoracao ad ON ad.decoracao_id = d.id
            LEFT JOIN teiko.adicional a ON a.id = ad.adicional_id
            WHERE d.id = :decoracaoId
            GROUP BY d.id, d.nome;\s
            """, nativeQuery = true)
    Optional<AdicionalDecoracaoProjection> findAdicionaisByDecoracaoId(@Param("decoracaoId") Integer decoracaoId);
}
