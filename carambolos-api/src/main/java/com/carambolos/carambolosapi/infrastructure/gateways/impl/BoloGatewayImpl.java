package com.carambolos.carambolosapi.infrastructure.gateways.impl;

import com.carambolos.carambolosapi.application.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.application.gateways.BoloGateway;
import com.carambolos.carambolosapi.domain.entity.Bolo;
import com.carambolos.carambolosapi.infrastructure.persistence.projection.DetalheBoloProjection;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.BoloMapper;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.BoloEntity;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.BoloRepository;

import java.util.List;

public class BoloGatewayImpl implements BoloGateway {
    private final BoloRepository repository;
    private final BoloMapper mapper;

    public BoloGatewayImpl(BoloRepository repository, BoloMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<Bolo> findByCategoriaIn(List<String> categorias) {
        List<BoloEntity> entities = repository.findByCategoriaIn(categorias)
                .stream()
                .filter(BoloEntity::getAtivo)
                .toList();

        return mapper.toDomain(entities);
    }

    @Override
    public List<Bolo> findAll() {
        List<BoloEntity> entities = repository.findAll()
                .stream()
                .filter(BoloEntity::getAtivo)
                .toList();

        return mapper.toDomain(entities);
    }

    @Override
    public List<DetalheBoloProjection> listarDetalheBolo() {
        return repository.listarDetalheBolo();
    }

    @Override
    public Bolo findById(Integer id) {
        BoloEntity entity = repository.findById(id)
                .filter(BoloEntity::getAtivo)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Bolo com o id %d não encontrado".formatted(id)));

        return mapper.toDomain(entity);
    }

    @Override
    public Boolean existsByIdAndIsAtivoTrue(Integer id) {
        return repository.existsByIdAndIsAtivoTrue(id);
    }

    @Override
    public Bolo save(Bolo bolo) {
        BoloEntity entity = mapper.toEntity(bolo);
        BoloEntity savedEntity = repository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Boolean existsByIdAndIdNotAndIsAtivoTrue(Integer id, Integer excludeId) {
        return repository.existsByIdAndIdNotAndIsAtivoTrue(id, excludeId);
    }

    @Override
    public Boolean existsById(Integer id) {
        return repository.existsById(id);
    }

    @Override
    public void atualizarStatusBolo(Integer status, Integer id) {
        repository.atualizarStatusBolo(status, id);
    }
}
