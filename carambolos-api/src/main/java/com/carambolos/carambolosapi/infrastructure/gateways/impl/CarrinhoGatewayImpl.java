package com.carambolos.carambolosapi.infrastructure.gateways.impl;

import com.carambolos.carambolosapi.application.gateways.CarrinhoGateway;
import com.carambolos.carambolosapi.domain.entity.Carrinho;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.CarrinhoMapper;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.CarrinhoRepository;

import java.util.Optional;

public class CarrinhoGatewayImpl implements CarrinhoGateway {
    private final CarrinhoRepository repository;

    public CarrinhoGatewayImpl(CarrinhoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Carrinho> findByUsuarioId(Integer usuarioId) {
        return repository.findByUsuarioId(usuarioId).map(CarrinhoMapper::toDomain);
    }

    @Override
    public Carrinho save(Carrinho carrinho) {
        var saved = repository.save(CarrinhoMapper.toEntity(carrinho));
        return CarrinhoMapper.toDomain(saved);
    }

    @Override
    public void deleteByUsuarioId(Integer usuarioId) {
        repository.deleteByUsuarioId(usuarioId);
    }
}