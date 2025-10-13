package com.carambolos.carambolosapi.infrastructure.gateways.impl;

import com.carambolos.carambolosapi.application.gateways.ProdutoFornadaGateway;
import com.carambolos.carambolosapi.domain.entity.ProdutoFornada;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.ProdutoFornadaRepository;

import java.util.Optional;

public class ProdutoFornadaGatewayImpl implements ProdutoFornadaGateway {
    private final ProdutoFornadaRepository repository;

    public ProdutoFornadaGatewayImpl(ProdutoFornadaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<ProdutoFornada> findById(Integer id) {
        return repository.findById(id);
    }
}


