package com.carambolos.carambolosapi.infrastructure.gateways.impl;

import com.carambolos.carambolosapi.application.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.application.gateways.RecheioUnitarioGateway;
import com.carambolos.carambolosapi.domain.entity.RecheioUnitario;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.RecheioUnitarioMapper;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.RecheioUnitarioEntity;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.RecheioUnitarioRepository;

import java.util.List;
import java.util.Optional;

public class RecheioUnitarioGatewayImpl implements RecheioUnitarioGateway {
    private final RecheioUnitarioRepository repository;
    private final RecheioUnitarioMapper mapper;

    public RecheioUnitarioGatewayImpl(RecheioUnitarioRepository repository, RecheioUnitarioMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public int countBySaborIgnoreCaseAndIsAtivo(String sabor, Boolean isAtivo) {
        return repository.countBySaborIgnoreCaseAndIsAtivo(sabor, isAtivo);
    }

    @Override
    public RecheioUnitario save(RecheioUnitario recheioUnitario) {
        RecheioUnitarioEntity entity = mapper.toEntity(recheioUnitario);
        RecheioUnitarioEntity entidadeSalva = repository.save(entity);

        return mapper.toDomain(entidadeSalva);
    }

    @Override
    public List<RecheioUnitario> findAll() {
        return mapper.toDomain(repository.findAll());
    }

    @Override
    public RecheioUnitario findById(Integer id) {
        RecheioUnitarioEntity entity = repository.findById(id)
                .filter(RecheioUnitarioEntity::getAtivo)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Recheio com o id %d não encontrado".formatted(id)));

        return mapper.toDomain(entity);
    }

    @Override
    public int countBySaborIgnoreCaseAndIdNotAndIsAtivo(String sabor, Integer id, Boolean isAtivo) {
        return repository.countBySaborIgnoreCaseAndIdNotAndIsAtivo(sabor, id, isAtivo);
    }

    @Override
    public void deletarRecheioUnitario(Integer id) {
        Optional<RecheioUnitarioEntity> possivelRecheio = repository.findById(id)
                .filter(RecheioUnitarioEntity::getAtivo);

        if (possivelRecheio.isEmpty()) {
            throw new EntidadeNaoEncontradaException("Recheio com id %d não existe".formatted(id));
        }
        RecheioUnitario recheio = mapper.toDomain(possivelRecheio.get());
        recheio.setAtivo(false);
        save(recheio);
    }

    @Override
    public Boolean existsByIdAndIsAtivoTrue(Integer id) {
        return repository.existsByIdAndIsAtivoTrue(id);
    }
}
