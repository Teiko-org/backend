package com.carambolos.carambolosapi.repository;

import com.carambolos.carambolosapi.model.ProdutoFornada;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoFornadaRepository extends JpaRepository<ProdutoFornada, Integer> {
    boolean existsByProduto(String produto);
}
