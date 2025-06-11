package com.carambolos.carambolosapi.repository;

import com.carambolos.carambolosapi.model.Bolo;
import com.carambolos.carambolosapi.model.ImagemDecoracao;
import com.carambolos.carambolosapi.model.projection.DetalheBoloProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BoloRepository extends JpaRepository<Bolo, Integer> {
    Boolean existsByIdAndIsAtivoTrue(Integer id);

    Boolean existsByIdAndIdNotAndIsAtivoTrue(Integer id, Integer id2);

    List<Bolo> findByCategoriaIn(List<String> categoria);

    @Query(value = """
            SELECT
              b.id AS bolo_id,
              d.nome AS produto,
              b.categoria,
              m.sabor AS sabor_massa,
              COALESCE(CONCAT(ru1.sabor, ' com ', ru2.sabor) , CONCAT(reu1.sabor, ' com ', reu2.sabor)) AS sabor_recheio,
              c.cor AS cor_cobertura,
              b.formato,
              b.tamanho,
              (m.valor + COALESCE(ru1.valor + ru2.valor, reu1.valor + reu2.valor)) AS preco_total,
              b.decoracao_id AS decoracao_id,   
              b.is_ativo AS status
            FROM bolo b
            JOIN massa m ON b.massa_id = m.id
            JOIN recheio_pedido rp ON b.recheio_pedido_id = rp.id
            JOIN cobertura c on b.cobertura_id = c.id
            LEFT JOIN decoracao d ON b.decoracao_id = d.id
            
            -- Caso o recheio seja dois unitários
            LEFT JOIN recheio_unitario ru1 ON rp.recheio_unitario_id1 = ru1.id
            LEFT JOIN recheio_unitario ru2 ON rp.recheio_unitario_id2 = ru2.id
            
            -- Caso o recheio seja um exclusivo (feito de dois unitários)
            LEFT JOIN recheio_exclusivo re ON rp.recheio_exclusivo = re.id
            LEFT JOIN recheio_unitario reu1 ON re.recheio_unitario_id1 = reu1.id
            LEFT JOIN recheio_unitario reu2 ON re.recheio_unitario_id2 = reu2.id
            """, nativeQuery = true)
    List<DetalheBoloProjection> listarDetalheBolo();

    @Transactional
    @Modifying
    @Query(value = """
            UPDATE bolo SET is_ativo = ?1 where id = ?2
            """, nativeQuery = true)
    void atualizarStatusBolo(Integer status, Integer id);

    @Query(value = "SELECT * FROM imagem_decoracao WHERE decoracao_id = ?1", nativeQuery = true)
    ImagemDecoracao findImagemByBolo(Integer idDecoracao);

}
