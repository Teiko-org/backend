package com.carambolos.carambolosapi.infrastructure.gateways.mapper;

import com.carambolos.carambolosapi.domain.entity.ImagemDecoracao;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.ImagemDecoracaoEntity;

import java.util.List;

public class ImagemDecoracaoMapper {
    private final DecoracaoMapper decoracaoMapper;

    public ImagemDecoracaoMapper(DecoracaoMapper decoracaoMapper) {
        this.decoracaoMapper = decoracaoMapper;
    }

    public ImagemDecoracaoEntity toEntity(ImagemDecoracao imagemDecoracao) {
        if (imagemDecoracao == null) {
            return null;
        }

        ImagemDecoracaoEntity entity = new ImagemDecoracaoEntity();
        entity.setId(imagemDecoracao.getId());
        entity.setUrl(imagemDecoracao.getUrl());
        entity.setDecoracao(decoracaoMapper.toEntity(imagemDecoracao.getDecoracao()));

        return entity;
    }

    public List<ImagemDecoracaoEntity> toEntity(List<ImagemDecoracao> imagensDecoracao) {
        if (imagensDecoracao == null) {
            return null;
        }

        return imagensDecoracao.stream()
                .map(this::toEntity)
                .toList();
    }

    public ImagemDecoracao toDomain(ImagemDecoracaoEntity entity) {
        if (entity == null) {
            return null;
        }

        ImagemDecoracao imagemDecoracao = new ImagemDecoracao();
        imagemDecoracao.setId(entity.getId());
        imagemDecoracao.setUrl(entity.getUrl());
        imagemDecoracao.setDecoracao(decoracaoMapper.toDomain(entity.getDecoracao()));

        return imagemDecoracao;
    }

    public List<ImagemDecoracao> toDomain(List<ImagemDecoracaoEntity> entities) {
        if (entities == null) {
            return null;
        }

        return entities.stream()
                .map(this::toDomain)
                .toList();
    }
}
