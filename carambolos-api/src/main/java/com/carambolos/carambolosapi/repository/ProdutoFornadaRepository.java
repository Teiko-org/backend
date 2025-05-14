package com.carambolos.carambolosapi.repository;

import com.carambolos.carambolosapi.model.ProdutoFornada;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdutoFornadaRepository extends JpaRepository<ProdutoFornada, Integer> {
    boolean existsByProduto(String produto);
    List<ProdutoFornada> findByCategoriaIn(List<String> categorias);
    boolean existsByProdutoAndIsAtivoTrue(String produto);
    boolean existsByProdutoAndIsAtivoTrueAndIdNot(String produto, Integer id);
}
