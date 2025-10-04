package com.carambolos.carambolosapi.infrastructure.gateways.impl;

import com.carambolos.carambolosapi.application.exception.EntidadeJaExisteException;
import com.carambolos.carambolosapi.application.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.application.gateways.MassaGateway;
import com.carambolos.carambolosapi.domain.entity.Massa;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.MassaMapper;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.MassaEntity;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.MassaRepository;

import java.util.List;
import java.util.Optional;

public class MassaGatewayImpl implements MassaGateway {
    private final MassaRepository repository;
    private final MassaMapper mapper;

    public MassaGatewayImpl(MassaRepository massaRepository, MassaMapper mapper) {
        this.repository = massaRepository;
        this.mapper = mapper;
    }

    @Override
    public Massa save(Massa massa) {
        MassaEntity entity = mapper.toEntity(massa);
        MassaEntity massaSalva = repository.save(entity);
        return mapper.toDomain(massaSalva);
    }

    @Override
    public Boolean existsByIdAndIsAtivo(Integer id, Boolean isAtivo) {
        return repository.existsByIdAndIsAtivo(id, isAtivo);
    }

    @Override
    public Integer countBySaborAndIdNotAndIsAtivo(String sabor, Integer id, Boolean isAtivo) {
        return repository.countBySaborAndIdNotAndIsAtivo(sabor, id, isAtivo);
    }

    @Override
    public Massa atualizarMassa(Massa massa, Integer id) {
        MassaEntity entity = mapper.toEntity(massa);
        entity.setId(id);
        MassaEntity massaSalva = repository.save(entity);

        return mapper.toDomain(massaSalva);
    }

    @Override
    public List<Massa> listarMassas() {
        List<MassaEntity> massasEntities = repository.findAll().stream().filter(MassaEntity::getAtivo).toList();
        return mapper.toDomain(massasEntities);
    }

    @Override
    public Massa findById(Integer id) {
        Optional<MassaEntity> possivelMassa = repository.findById(id)
                .filter(MassaEntity::getAtivo);

        if (possivelMassa.isEmpty()) {
            throw new EntidadeNaoEncontradaException("Massa com id %d não encontrada".formatted(id));
        }

        return mapper.toDomain(possivelMassa.get());
    }

    @Override
    public void deletarMassa(Integer id) {
        Optional<MassaEntity> possivelMassa = repository.findById(id)
                .filter(MassaEntity::getAtivo);
        if (possivelMassa.isEmpty()) {
            throw new EntidadeNaoEncontradaException("Massa com id %d não encontrada".formatted(id));
        }
        MassaEntity massaEntity = possivelMassa.get();
        massaEntity.setAtivo(false);
        repository.save(massaEntity);
    }

    @Override
    public int countBySaborAndIsAtivo(String sabor, Boolean isAtivo) {
        return repository.countBySaborAndIsAtivo(sabor, isAtivo);
    }
}
