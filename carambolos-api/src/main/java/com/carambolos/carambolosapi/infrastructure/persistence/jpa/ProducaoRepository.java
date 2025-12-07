package com.carambolos.carambolosapi.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface ProducaoRepository extends JpaRepository<Object, Integer> {
    
    @Query(value = """
        SELECT 
            m.sabor AS nome,
            COUNT(DISTINCT rp.id) AS quantidade,
            GROUP_CONCAT(DISTINCT rp.id ORDER BY rp.id SEPARATOR ',') AS pedidos
        FROM resumo_pedido rp
        INNER JOIN pedido_bolo pb ON rp.pedido_bolo_id = pb.id AND pb.is_ativo = 1
        INNER JOIN bolo b ON pb.bolo_id = b.id AND b.is_ativo = 1
        INNER JOIN massa m ON b.massa_id = m.id
        WHERE rp.status = 'PENDENTE' 
          AND rp.is_ativo = 1
          AND rp.pedido_bolo_id IS NOT NULL
        GROUP BY m.id, m.sabor
        ORDER BY quantidade DESC
        """, nativeQuery = true)
    List<Map<String, Object>> findPedidosPendentesPorMassa();

    @Query(value = """
        SELECT 
            CASE 
                WHEN re.nome IS NOT NULL THEN CONCAT(re.nome, ' (', reu1.sabor, ' + ', reu2.sabor, ')')
                WHEN ru1.sabor IS NOT NULL AND ru2.sabor IS NOT NULL THEN CONCAT(ru1.sabor, ' + ', ru2.sabor)
                WHEN ru1.sabor IS NOT NULL THEN ru1.sabor
                WHEN ru2.sabor IS NOT NULL THEN ru2.sabor
                ELSE 'Não especificado'
            END AS nome,
            COUNT(DISTINCT rp.id) AS quantidade,
            GROUP_CONCAT(DISTINCT rp.id ORDER BY rp.id SEPARATOR ',') AS pedidos
        FROM resumo_pedido rp
        INNER JOIN pedido_bolo pb ON rp.pedido_bolo_id = pb.id AND pb.is_ativo = 1
        INNER JOIN bolo b ON pb.bolo_id = b.id AND b.is_ativo = 1
        LEFT JOIN recheio_pedido rp_recheio ON b.recheio_pedido_id = rp_recheio.id
        LEFT JOIN recheio_exclusivo re ON rp_recheio.recheio_exclusivo = re.id
        LEFT JOIN recheio_unitario reu1 ON re.recheio_unitario_id1 = reu1.id
        LEFT JOIN recheio_unitario reu2 ON re.recheio_unitario_id2 = reu2.id
        LEFT JOIN recheio_unitario ru1 ON rp_recheio.recheio_unitario_id1 = ru1.id
        LEFT JOIN recheio_unitario ru2 ON rp_recheio.recheio_unitario_id2 = ru2.id
        WHERE rp.status = 'PENDENTE' 
          AND rp.is_ativo = 1
          AND rp.pedido_bolo_id IS NOT NULL
        GROUP BY 
            CASE 
                WHEN re.nome IS NOT NULL THEN CONCAT(re.nome, ' (', reu1.sabor, ' + ', reu2.sabor, ')')
                WHEN ru1.sabor IS NOT NULL AND ru2.sabor IS NOT NULL THEN CONCAT(ru1.sabor, ' + ', ru2.sabor)
                WHEN ru1.sabor IS NOT NULL THEN ru1.sabor
                WHEN ru2.sabor IS NOT NULL THEN ru2.sabor
                ELSE 'Não especificado'
            END
        ORDER BY quantidade DESC
        """, nativeQuery = true)
    List<Map<String, Object>> findPedidosPendentesPorRecheio();

    @Query(value = """
        SELECT 
            MONTH(rp.data_pedido) AS mes,
            m.sabor AS massa,
            COUNT(*) AS quantidade
        FROM resumo_pedido rp
        INNER JOIN pedido_bolo pb ON rp.pedido_bolo_id = pb.id AND pb.is_ativo = 1
        INNER JOIN bolo b ON pb.bolo_id = b.id AND b.is_ativo = 1
        INNER JOIN massa m ON b.massa_id = m.id
        WHERE YEAR(rp.data_pedido) = :ano
          AND rp.status != 'CANCELADO'
          AND rp.is_ativo = 1
          AND rp.pedido_bolo_id IS NOT NULL
        GROUP BY MONTH(rp.data_pedido), m.id, m.sabor
        ORDER BY mes, quantidade DESC
        """, nativeQuery = true)
    List<Map<String, Object>> findMassasMaisPedidasPorMes(@Param("ano") int ano);
}
