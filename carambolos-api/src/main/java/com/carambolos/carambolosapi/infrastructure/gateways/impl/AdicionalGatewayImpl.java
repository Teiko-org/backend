package com.carambolos.carambolosapi.infrastructure.gateways.impl;

import com.carambolos.carambolosapi.application.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.application.gateways.AdicionalGateway;
import com.carambolos.carambolosapi.domain.entity.Adicional;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.AdicionalMapper;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.AdicionalEntity;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.AdicionalRepository;

import java.util.List;

public class AdicionalGatewayImpl implements AdicionalGateway {
    private final AdicionalRepository repository;
    private final AdicionalMapper mapper;

    public AdicionalGatewayImpl(AdicionalRepository repository, AdicionalMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }


    @Override
    public List<Adicional> listarAdicionais() {
        List<AdicionalEntity> entities = repository.findAll().stream().filter(AdicionalEntity::getAtivo).toList();
        if (entities.isEmpty()) {
            throw new EntidadeNaoEncontradaException("Adicionais não encontrados.");
        }
        return mapper.toDomain(entities);
    }
}
