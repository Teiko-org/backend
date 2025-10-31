package com.carambolos.carambolosapi.application.gateways;

import com.carambolos.carambolosapi.domain.entity.ProdutoFornada;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.List;

public interface ProdutoFornadaGateway {
    Optional<ProdutoFornada> findById(Integer id);
    ProdutoFornada save(ProdutoFornada produto);
    List<ProdutoFornada> findAll();
    List<ProdutoFornada> findByCategoriaIn(List<String> categorias);
    boolean existsByProdutoAndIsAtivoTrue(String produto);
    boolean existsByProdutoAndIsAtivoTrueAndIdNot(String produto, Integer id);
    void updateStatus(Boolean status, Integer id);

    Page<ProdutoFornada> findAtivos(Pageable pageable);
    Page<ProdutoFornada> findAtivosByCategoriaIn(Pageable pageable, List<String> categorias);
}


