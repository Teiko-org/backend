package com.carambolos.carambolosapi.infrastructure.gateways.impl;

import com.carambolos.carambolosapi.application.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.application.gateways.DecoracaoGateway;
import com.carambolos.carambolosapi.domain.entity.Decoracao;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.DecoracaoMapper;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.ImagemDecoracaoMapper;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.DecoracaoEntity;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.DecoracaoRepository;

import java.util.List;
import java.util.stream.Collectors;

public class DecoracaoGatewayImpl implements DecoracaoGateway {
    private final DecoracaoRepository decoracaoRepository;
    private final DecoracaoMapper decoracaoMapper;
    private final ImagemDecoracaoMapper imagemDecoracaoMapper;

    public DecoracaoGatewayImpl(DecoracaoRepository decoracaoRepository, DecoracaoMapper decoracaoMapper, ImagemDecoracaoMapper imagemDecoracaoMapper) {
        this.decoracaoRepository = decoracaoRepository;
        this.decoracaoMapper = decoracaoMapper;
        this.imagemDecoracaoMapper = imagemDecoracaoMapper;
    }

    @Override
    public Decoracao save(Decoracao decoracao) {
        DecoracaoEntity decoracaoEntity = decoracaoMapper.toEntity(decoracao, imagemDecoracaoMapper);
        DecoracaoEntity savedEntity = decoracaoRepository.save(decoracaoEntity);

        return decoracaoMapper.toDomain(savedEntity, imagemDecoracaoMapper);
    }

    @Override
    public List<Decoracao> findByIsAtivoTrue() {
        List<DecoracaoEntity> entities = decoracaoRepository.findByIsAtivoTrue();

        return decoracaoMapper.toDomain(entities, imagemDecoracaoMapper);
    }

    @Override
    public List<Decoracao> findByIsAtivoTrueAndCategoriaIsNotNull() {
        List<DecoracaoEntity> entities = decoracaoRepository.findByIsAtivoTrueAndCategoriaIsNotNull();
        return decoracaoMapper.toDomain(entities, imagemDecoracaoMapper);
    }

    @Override
    public Decoracao findById(Integer id) {
        DecoracaoEntity entity = decoracaoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Decoração não encontrada"));

        return decoracaoMapper.toDomain(entity, imagemDecoracaoMapper);
    }
}
