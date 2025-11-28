package com.carambolos.carambolosapi.infrastructure.gateways.impl;

import com.carambolos.carambolosapi.application.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.application.gateways.RecheioPedidoGateway;
import com.carambolos.carambolosapi.domain.entity.RecheioPedido;
import com.carambolos.carambolosapi.infrastructure.persistence.projection.RecheioPedidoProjection;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.RecheioPedidoMapper;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.RecheioPedidoEntity;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.RecheioPedidoRepository;

import java.util.List;

public class RecheioPedidoGatewayImpl implements RecheioPedidoGateway {
    private final RecheioPedidoRepository repository;
    private final RecheioPedidoMapper mapper;

    public RecheioPedidoGatewayImpl(RecheioPedidoRepository repository, RecheioPedidoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public RecheioPedido save(RecheioPedido recheioPedido) {
        RecheioPedidoEntity entity = mapper.toEntity(recheioPedido);
        RecheioPedidoEntity entidadeSalva = repository.save(entity);
        return mapper.toDomain(entidadeSalva);
    }

    @Override
    public RecheioPedidoProjection buscarRecheioPedidoExclusivoPorId(Integer id) {
        return repository.buscarRecheioPedidoExclusivoPorId(id);
    }

    @Override
    public RecheioPedidoProjection buscarRecheioPedidoUnitariosPorId(Integer id) {
        return repository.buscarRecheioPedidoUnitariosPorId(id);
    }

    @Override
    public Boolean existsById(Integer id) {
        return repository.existsById(id);
    }

    @Override
    public RecheioPedido findById(Integer id) {
        RecheioPedidoEntity entity = repository.findById(id)
                .filter(RecheioPedidoEntity::getAtivo)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Recheio com o id %d não encontrado".formatted(id)));

        return mapper.toDomain(entity);
    }

    @Override
    public List<RecheioPedidoProjection> listarRecheiosPedido() {
        return repository.listarRecheiosPedido().stream()
                .filter(recheioPedido -> Boolean.TRUE.equals(recheioPedido.getIsAtivo()))
                .toList();
    }

    @Override
    public Boolean existsByIdAndIsAtivoTrue(Integer id) {
        return repository.existsByIdAndIsAtivoTrue(id);
    }
}
