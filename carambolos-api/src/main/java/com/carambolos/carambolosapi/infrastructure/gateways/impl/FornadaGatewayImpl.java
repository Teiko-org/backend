package com.carambolos.carambolosapi.infrastructure.gateways.impl;

import com.carambolos.carambolosapi.application.gateways.FornadaGateway;
import com.carambolos.carambolosapi.domain.entity.Fornada;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.FornadasMapper;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.FornadaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class FornadaGatewayImpl implements FornadaGateway {
    private final FornadaRepository repository;

    public FornadaGatewayImpl(FornadaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Fornada save(Fornada fornada) {
        var saved = repository.save(FornadasMapper.toEntity(fornada));
        return FornadasMapper.toDomain(saved);
    }

    @Override
    public boolean existsAtivaById(Integer id) {
        return repository.existsByIdAndIsAtivoTrue(id);
    }

    @Override
    public List<Fornada> findAll() {
        return repository.findAll().stream().map(FornadasMapper::toDomain).toList();
    }

    @Override
    public List<Fornada> findAllAtivas() {
        return repository.findAllByIsAtivoTrueOrderByDataInicioAsc().stream().map(FornadasMapper::toDomain).toList();
    }

    @Override
    public List<Fornada> findByDataInicioBetweenOrderByDataInicioAsc(LocalDate inicio, LocalDate fim) {
        return repository.findByDataInicioBetweenOrderByDataInicioAsc(inicio, fim).stream().map(FornadasMapper::toDomain).toList();
    }

    @Override
    public Optional<Fornada> findTop1ByAtivaTrueOrderByDataInicioDesc() {
        return repository.findTop1ByIsAtivoTrueOrderByDataInicioDesc().map(FornadasMapper::toDomain);
    }

    @Override
    public Optional<Fornada> findById(Integer id) {
        return repository.findById(id).map(FornadasMapper::toDomain);
    }

    @Override
    public List<Fornada> findAllByAtivaTrueOrderByDataInicioAsc() {
        return repository.findAllByIsAtivoTrueOrderByDataInicioAsc().stream().map(FornadasMapper::toDomain).toList();
    }
}
