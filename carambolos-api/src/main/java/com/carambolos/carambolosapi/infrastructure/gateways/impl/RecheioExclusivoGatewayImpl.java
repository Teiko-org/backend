package com.carambolos.carambolosapi.infrastructure.gateways.impl;

import com.carambolos.carambolosapi.application.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.application.gateways.RecheioExclusivoGateway;
import com.carambolos.carambolosapi.domain.entity.RecheioExclusivo;
import com.carambolos.carambolosapi.infrastructure.persistence.projection.RecheioExclusivoProjection;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.RecheioExclusivoMapper;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.RecheioExclusivoEntity;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.RecheioExclusivoRepository;

import java.util.List;

public class RecheioExclusivoGatewayImpl implements RecheioExclusivoGateway {
    private final RecheioExclusivoRepository repository;
    private final RecheioExclusivoMapper mapper;

    public RecheioExclusivoGatewayImpl(RecheioExclusivoRepository repository, RecheioExclusivoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public int countByRecheioUnitarioIds(int recheioUnitarioId1, int recheioUnitarioId2) {
        return repository.countByRecheioUnitarioIds(recheioUnitarioId1, recheioUnitarioId2);
    }

    @Override
    public RecheioExclusivo save(RecheioExclusivo recheioExclusivo) {
        RecheioExclusivoEntity entity = mapper.toEntity(recheioExclusivo);

        RecheioExclusivoEntity entidadeSalva = repository.save(entity);
        return mapper.toDomain(entidadeSalva);
    }

    @Override
    public RecheioExclusivoProjection buscarRecheioExclusivoPorId(int id) {
        RecheioExclusivoProjection projection = repository.buscarRecheioExclusivoPorId(id);

        return projection;
    }

    @Override
    public List<RecheioExclusivoProjection> listarRecheiosExclusivos() {
        return repository.listarRecheiosExclusivos().stream()
                .filter(recheioExclusivo -> recheioExclusivo.getIsAtivo() != null && recheioExclusivo.getIsAtivo() == 1)
                .toList();
    }

    @Override
    public RecheioExclusivo findById(int id) {
        RecheioExclusivoEntity entity = repository.findById(id)
                .filter(RecheioExclusivoEntity::getAtivo)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Recheio com o id %d não encontrado".formatted(id)));;

        return mapper.toDomain(entity);
    }
}
