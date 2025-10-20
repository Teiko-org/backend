package com.carambolos.carambolosapi.infrastructure.gateways.impl;

import com.carambolos.carambolosapi.application.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.application.gateways.PedidoBoloGateway;
import com.carambolos.carambolosapi.domain.entity.PedidoBolo;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.PedidoBoloMapper;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.PedidoBoloEntity;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.PedidoBoloRepository;

import java.util.List;

public class PedidoBoloGatewayImpl implements PedidoBoloGateway {
    private final PedidoBoloRepository repository;
    private final PedidoBoloMapper mapper;

    public PedidoBoloGatewayImpl(PedidoBoloRepository repository, PedidoBoloMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<PedidoBolo> findAll() {
        List<PedidoBoloEntity> entities = repository.findAll().stream().filter(PedidoBoloEntity::getAtivo).toList();
        return mapper.toDomain(entities);
    }

    @Override
    public PedidoBolo findById(Integer id) {
        PedidoBoloEntity pedidoBolo = repository.findById(id)
                .filter(PedidoBoloEntity::getAtivo)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Pedido com o id %d não encontrado".formatted(id)));

        return mapper.toDomain(pedidoBolo);
    }

    @Override
    public Boolean existsByIdAndIsAtivoTrue(Integer id) {
        return repository.existsByIdAndIsAtivoTrue(id);
    }

    @Override
    public PedidoBolo save(PedidoBolo pedidoBolo) {
        PedidoBoloEntity entity = mapper.toEntity(pedidoBolo);
        PedidoBoloEntity entidadeSalva = repository.save(entity);

        return mapper.toDomain(entidadeSalva);
    }
}
