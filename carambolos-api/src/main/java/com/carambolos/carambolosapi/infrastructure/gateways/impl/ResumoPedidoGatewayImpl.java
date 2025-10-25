package com.carambolos.carambolosapi.infrastructure.gateways.impl;

import com.carambolos.carambolosapi.application.exception.EntidadeNaoEncontradaException;
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
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Resumo de pedido não encontrado"));

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
        ResumoPedidoEntity entity = mapper.toEntity(resumoPedido);
        ResumoPedidoEntity entidadeSalva = repository.save(entity);
        return mapper.toDomain(entidadeSalva);
    }


    @Override
    public boolean existsByIdAndIsAtivoTrue(Integer id) {
        return false;
    }

    @Override
    public List<ResumoPedido> findByPedidoBoloIdIsNotNullAndIsAtivoTrue() {
        List<ResumoPedidoEntity> entities = repository.findByPedidoBoloIdIsNotNullAndIsAtivoTrue();

        return mapper.toDomain(entities);
    }

    @Override
    public List<ResumoPedido> findByPedidoFornadaIdIsNotNullAndIsAtivoTrue() {
        List<ResumoPedidoEntity> entities = repository.findByPedidoFornadaIdIsNotNullAndIsAtivoTrue();
        return mapper.toDomain(entities);
    }

    @Override
    public ResumoPedido findTop1ByPedidoBoloIdAndIsAtivoTrueOrderByDataPedidoDesc(Integer id) {
        ResumoPedidoEntity entity = repository.findTop1ByPedidoBoloIdAndIsAtivoTrueOrderByDataPedidoDesc(id)
                .orElseThrow(() -> new RuntimeException("Resumo de pedido (bolo) não encontrado"));
        return mapper.toDomain(entity);
    }

    @Override
    public ResumoPedido findTop1ByPedidoFornadaIdAndIsAtivoTrueOrderByDataPedidoDesc(Integer id) {
        ResumoPedidoEntity entity = repository.findTop1ByPedidoFornadaIdAndIsAtivoTrueOrderByDataPedidoDesc(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Resumo de pedido (fornada) não encontrado"));

        return mapper.toDomain(entity);
    }


}
