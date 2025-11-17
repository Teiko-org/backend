package com.carambolos.carambolosapi.infrastructure.gateways.mapper;

import com.carambolos.carambolosapi.domain.entity.Decoracao;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.DecoracaoEntity;

import java.util.List;

public class DecoracaoMapper {
    private final ImagemDecoracaoMapper imagemDecoracaoMapper;

    public DecoracaoMapper(ImagemDecoracaoMapper imagemDecoracaoMapper) {
        this.imagemDecoracaoMapper = imagemDecoracaoMapper;
    }

    public List<DecoracaoEntity> toEntity(List<Decoracao> decoracoes) {
        if (decoracoes == null) {
            return null;
        }

        return decoracoes.stream()
                .map(this::toEntity)
                .toList();
    }

    public DecoracaoEntity toEntity(Decoracao decoracao) {
        if (decoracao == null) {
            return null;
        }

        DecoracaoEntity entity = new DecoracaoEntity();
        entity.setId(decoracao.getId());
        entity.setImagens(imagemDecoracaoMapper.toEntity(decoracao.getImagens()));
        entity.setObservacao(decoracao.getObservacao());
        entity.setNome(decoracao.getNome());
        entity.setAtivo(decoracao.getAtivo());
        entity.setCategoria(decoracao.getCategoria());

        return entity;
    }

    public List<Decoracao> toDomain(List<DecoracaoEntity> entities) {
        if (entities == null) {
            return null;
        }

        return entities.stream()
                .map(this::toDomain)
                .toList();
    }

    public Decoracao toDomain(DecoracaoEntity entity) {
        if (entity == null) {
            return null;
        }

        Decoracao decoracao = new Decoracao();
        decoracao.setId(entity.getId());
        decoracao.setImagens(imagemDecoracaoMapper.toDomain(entity.getImagens()));
        decoracao.setObservacao(entity.getObservacao());
        decoracao.setNome(entity.getNome());
        decoracao.setAtivo(entity.getAtivo());
        decoracao.setCategoria(entity.getCategoria());

        return decoracao;
    }
}
