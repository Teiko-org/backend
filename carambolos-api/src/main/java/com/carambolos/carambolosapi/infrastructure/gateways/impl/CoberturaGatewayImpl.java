package com.carambolos.carambolosapi.infrastructure.gateways.impl;

import com.carambolos.carambolosapi.application.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.application.gateways.CoberturaGateway;
import com.carambolos.carambolosapi.domain.entity.Cobertura;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.CoberturaMapper;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.CoberturaEntity;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.CoberturaRepository;

import java.util.List;
import java.util.Optional;

public class CoberturaGatewayImpl implements CoberturaGateway {
    private final CoberturaRepository repository;
    private final CoberturaMapper mapper;

    public CoberturaGatewayImpl(CoberturaRepository repository, CoberturaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public int countByCorAndDescricaoIgnoreCaseAndIsAtivoTrue(String cor, String descricao) {
        return repository.countByCorAndDescricaoIgnoreCaseAndIsAtivoTrue(cor, descricao);
    }

    @Override
    public Cobertura save(Cobertura cobertura) {
        CoberturaEntity entity = mapper.toEntity(cobertura);
        CoberturaEntity coberturaSalva = repository.save(entity);
        return mapper.toDomain(coberturaSalva);
    }

    @Override
    public Cobertura findById(Integer id) {
        Optional<CoberturaEntity> coberturaEncontrada = repository.findById(id);
        if (coberturaEncontrada.isPresent() && coberturaEncontrada.get().getAtivo()) {
            CoberturaEntity entity = coberturaEncontrada.get();
            return mapper.toDomain(entity);
        }

        throw new EntidadeNaoEncontradaException("Cobertura com id %d não encontrada".formatted(id));
    }

    @Override
    public int countByCorAndDescricaoAndIdNotAndIsAtivoTrue(String cor, String descricao, Integer id) {
        return repository.countByCorAndDescricaoAndIdNotAndIsAtivoTrue(cor, descricao, id);
    }

    @Override
    public List<Cobertura> findAll() {
        List<CoberturaEntity> entities = repository.findAll();
        return mapper.toDomain(entities);
    }
}
