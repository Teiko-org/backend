package com.carambolos.carambolosapi.application.gateways;

import com.carambolos.carambolosapi.infrastructure.persistence.entity.FornadaDaVez;
import com.carambolos.carambolosapi.domain.projection.ProdutoFornadaDaVezProjection;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FornadaDaVezGateway {
    Optional<FornadaDaVez> findById(Integer id);
    FornadaDaVez save(FornadaDaVez fornadaDaVez);
    List<FornadaDaVez> findAll();
    List<ProdutoFornadaDaVezProjection> findProductsByFornada(LocalDate dataInicio, LocalDate dataFim);
    List<ProdutoFornadaDaVezProjection> findResumoKpiByFornadaId(Integer fornadaId);
    FornadaDaVez findByFornadaAndProdutoFornadaAndIsAtivoTrue(Integer fornadaId, Integer produtoFornadaId);
    FornadaDaVez saveSummingIfExists(Integer fornadaId, Integer produtoFornadaId, Integer quantidade);
}


