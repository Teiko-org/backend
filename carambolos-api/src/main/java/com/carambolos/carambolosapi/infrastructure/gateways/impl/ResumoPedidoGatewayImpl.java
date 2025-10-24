package com.carambolos.carambolosapi.infrastructure.gateways.impl;

import com.carambolos.carambolosapi.application.gateways.ResumoPedidoGateway;
import com.carambolos.carambolosapi.domain.entity.ResumoPedido;
import com.carambolos.carambolosapi.domain.enums.StatusEnum;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.ResumoPedidoMapper;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.ResumoPedidoEntity;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.ResumoPedidoRepository;

import java.time.LocalDateTime;
import java.util.List;

public class ResumoPedidoGatewayImpl implements ResumoPedidoGateway {
    private final ResumoPedidoRepository repository;
    private final ResumoPedidoMapper mapper;

    public ResumoPedidoGatewayImpl(ResumoPedidoRepository repository, ResumoPedidoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<ResumoPedido> findAllByIsAtivoTrue() {
        List<ResumoPedidoEntity> entities = repository.findAllByIsAtivoTrue();
        return mapper.toDomain(entities);
    }

    @Override
    public ResumoPedido findByIdAndIsAtivoTrue(Integer id) {
        ResumoPedidoEntity entity = repository.findByIdAndIsAtivoTrue(id)
                .orElseThrow(() -> new RuntimeException("Resumo de pedido não encontrado"));

        return mapper.toDomain(entity);
    }

    @Override
    public List<ResumoPedido> findByDataPedidoBetweenAndIsAtivoTrue(LocalDateTime comecoData, LocalDateTime fimData) {

        List<ResumoPedidoEntity> entities = repository.findByDataPedidoBetweenAndIsAtivoTrue(comecoData, fimData);
        return mapper.toDomain(entities);
    }

    @Override
    public List<ResumoPedido> findByStatusAndIsAtivoTrue(StatusEnum status) {
        List<ResumoPedidoEntity> entities = repository.findByStatusAndIsAtivoTrue(status);
        return mapper.toDomain(entities);
    }

    @Override
    public ResumoPedido save(ResumoPedido resumoPedido) {

        return repository.save(mapper.toEntity(resumoPedido));
    }

    @Override
    public boolean existsByIdAndIsAtivoTrue(Integer id) {
        return false;
    }
}
