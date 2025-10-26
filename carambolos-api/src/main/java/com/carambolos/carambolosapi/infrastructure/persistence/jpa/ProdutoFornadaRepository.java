package com.carambolos.carambolosapi.infrastructure.persistence.jpa;

import com.carambolos.carambolosapi.infrastructure.persistence.entity.ProdutoFornada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProdutoFornadaRepository extends JpaRepository<ProdutoFornada, Integer> {
    boolean existsByProduto(String produto);
    List<ProdutoFornada> findByCategoriaIn(List<String> categorias);
    boolean existsByProdutoAndIsAtivoTrue(String produto);
    boolean existsByProdutoAndIsAtivoTrueAndIdNot(String produto, Integer id);

    @Transactional
    @Modifying
    @Query(
            value = "UPDATE produto_fornada SET is_ativo = ?1 WHERE id = ?2",
            nativeQuery = true
    )
    void updateStatus(Integer status, Integer id);
}
