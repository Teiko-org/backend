package com.carambolos.carambolosapi.application.gateways;

import com.carambolos.carambolosapi.infrastructure.persistence.entity.FornadaDaVez;
import com.carambolos.carambolosapi.infrastructure.persistence.projection.ProdutoFornadaDaVezProjection;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FornadaDaVezGateway {
    Optional<FornadaDaVez> findById(Integer id);
    FornadaDaVez save(FornadaDaVez fornadaDaVez);
    List<FornadaDaVez> findAll();
    // Os métodos abaixo DEVEM retornar ProdutoFornadaDaVezProjection com o campo quantidadeTotal preenchido corretamente,
    // representando a quantidade original planejada do produto na fornada.
    List<ProdutoFornadaDaVezProjection> findProductsByFornada(LocalDate dataInicio, LocalDate dataFim);
    List<ProdutoFornadaDaVezProjection> findResumoKpiByFornadaId(Integer fornadaId);
    FornadaDaVez findByFornadaAndProdutoFornadaAndIsAtivoTrue(Integer fornadaId, Integer produtoFornadaId);
    FornadaDaVez saveSummingIfExists(Integer fornadaId, Integer produtoFornadaId, Integer quantidade);
}