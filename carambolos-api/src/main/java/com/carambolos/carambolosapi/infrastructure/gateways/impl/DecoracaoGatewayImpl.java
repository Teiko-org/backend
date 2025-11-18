package com.carambolos.carambolosapi.infrastructure.gateways.impl;

import com.carambolos.carambolosapi.application.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.application.gateways.DecoracaoGateway;
import com.carambolos.carambolosapi.domain.entity.Decoracao;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.DecoracaoMapper;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.DecoracaoEntity;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.DecoracaoRepository;

import java.util.List;
import java.util.stream.Collectors;

public class DecoracaoGatewayImpl implements DecoracaoGateway {
    private final DecoracaoRepository decoracaoRepository;
    private final DecoracaoMapper decoracaoMapper;

    public DecoracaoGatewayImpl(DecoracaoRepository decoracaoRepository, DecoracaoMapper decoracaoMapper) {
        this.decoracaoRepository = decoracaoRepository;
        this.decoracaoMapper = decoracaoMapper;
    }

    @Override
    public Decoracao save(Decoracao decoracao) {
        DecoracaoEntity decoracaoEntity = decoracaoMapper.toEntity(decoracao);
        DecoracaoEntity savedEntity = decoracaoRepository.save(decoracaoEntity);

        return decoracaoMapper.toDomain(savedEntity);
    }

    @Override
    public List<Decoracao> findByIsAtivoTrue() {
        List<DecoracaoEntity> entities = decoracaoRepository.findByIsAtivoTrue();

        return decoracaoMapper.toDomain(entities);
    }

    @Override
    public List<Decoracao> findByIsAtivoTrueAndCategoriaIsNotNull() {
        List<DecoracaoEntity> entities = decoracaoRepository.findByIsAtivoTrueAndCategoriaIsNotNull();
        return decoracaoMapper.toDomain(entities);
    }

    @Override
    public Decoracao findById(Integer id) {
        DecoracaoEntity entity = decoracaoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Decoração não encontrada"));

        return decoracaoMapper.toDomain(entity);
    }
}
