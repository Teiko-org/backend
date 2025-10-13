package com.carambolos.carambolosapi.application.gateways;

import com.carambolos.carambolosapi.domain.entity.ProdutoFornada;

import java.util.Optional;

public interface ProdutoFornadaGateway {
    Optional<ProdutoFornada> findById(Integer id);
}


