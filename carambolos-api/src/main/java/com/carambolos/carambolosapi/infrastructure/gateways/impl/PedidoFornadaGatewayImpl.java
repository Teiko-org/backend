package com.carambolos.carambolosapi.infrastructure.gateways.impl;

import com.carambolos.carambolosapi.application.gateways.PedidoFornadaGateway;
import com.carambolos.carambolosapi.domain.entity.PedidoFornada;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.FornadasMapper;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.PedidoFornadaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PedidoFornadaGatewayImpl implements PedidoFornadaGateway {
    private final PedidoFornadaRepository repository;

    public PedidoFornadaGatewayImpl(PedidoFornadaRepository repository) {
        this.repository = repository;
    }

    @Override
    public PedidoFornada save(PedidoFornada pedidoFornada) {
        var saved = repository.save(FornadasMapper.toEntity(pedidoFornada));
        return FornadasMapper.toDomain(saved);
    }

    @Override
    public Optional<PedidoFornada> findById(Integer id) {
        return repository.findById(id).map(FornadasMapper::toDomain);
    }

    @Override
    public List<PedidoFornada> findAll() {
        return repository.findAll().stream().map(FornadasMapper::toDomain).toList();
    }
}
