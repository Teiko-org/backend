package com.carambolos.carambolosapi.infrastructure.gateways.impl;

import com.carambolos.carambolosapi.application.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.application.gateways.AdicionalDecoracaoGateway;
import com.carambolos.carambolosapi.domain.entity.AdicionalDecoracao;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.AdicionalDecoracaoMapper;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.AdicionalDecoracaoRepository;
import com.carambolos.carambolosapi.infrastructure.persistence.projection.AdicionalDecoracaoProjection;

import java.util.List;

public class AdicionalDecoracaoGatewayImpl implements AdicionalDecoracaoGateway {
    private final AdicionalDecoracaoRepository repository;
    private final AdicionalDecoracaoMapper mapper;

    public AdicionalDecoracaoGatewayImpl(AdicionalDecoracaoRepository repository, AdicionalDecoracaoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<AdicionalDecoracao> buscarTodosAdicionaisPorDecoracao() {
        List<AdicionalDecoracaoProjection> projections = repository.findAllAdicionaisByDecoracao();

        if (projections.isEmpty()) {
        throw new EntidadeNaoEncontradaException("Adicionais de decoração não encontrados.");
        }

        return mapper.toDomain(projections);
    }
}
