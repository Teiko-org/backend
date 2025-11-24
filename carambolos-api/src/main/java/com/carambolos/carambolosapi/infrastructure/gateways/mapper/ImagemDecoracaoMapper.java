package com.carambolos.carambolosapi.infrastructure.gateways.mapper;

import com.carambolos.carambolosapi.domain.entity.ImagemDecoracao;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.DecoracaoEntity;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.ImagemDecoracaoEntity;

import java.util.List;

public class ImagemDecoracaoMapper {

    public ImagemDecoracaoEntity toEntity(ImagemDecoracao imagemDecoracao, DecoracaoMapper decoracaoMapper) {
        if (imagemDecoracao == null) {
            return null;
        }

        ImagemDecoracaoEntity entity = new ImagemDecoracaoEntity();
        entity.setId(imagemDecoracao.getId());
        entity.setUrl(imagemDecoracao.getUrl());
        entity.setDecoracao(decoracaoMapper.toEntity(imagemDecoracao.getDecoracao(), this));

        return entity;
    }

    public List<ImagemDecoracaoEntity> toEntity(List<ImagemDecoracao> imagensDecoracao, DecoracaoMapper decoracaoMapper) {
        if (imagensDecoracao == null) {
            return null;
        }

        return imagensDecoracao.stream()
                .map(img -> toEntity(img, decoracaoMapper))
                .toList();
    }

    public ImagemDecoracao toDomain(ImagemDecoracaoEntity entity, DecoracaoMapper decoracaoMapper) {
        if (entity == null) {
            return null;
        }

        ImagemDecoracao imagemDecoracao = new ImagemDecoracao();
        imagemDecoracao.setId(entity.getId());
        imagemDecoracao.setUrl(entity.getUrl());
        imagemDecoracao.setDecoracao(decoracaoMapper.toDomain(entity.getDecoracao(), this));

        return imagemDecoracao;
    }

    public List<ImagemDecoracao> toDomain(List<ImagemDecoracaoEntity> entities, DecoracaoMapper decoracaoMapper) {
        if (entities == null) {
            return null;
        }

        return entities.stream()
                .map(entity -> toDomain(entity, decoracaoMapper))
                .toList();
    }

    public ImagemDecoracaoEntity toEntityShallow(ImagemDecoracao imagemDecoracao, DecoracaoEntity decoracaoEntity) {
        if (imagemDecoracao == null) {
            return null;
        }
        ImagemDecoracaoEntity entity = new ImagemDecoracaoEntity();
        entity.setId(imagemDecoracao.getId());
        entity.setUrl(imagemDecoracao.getUrl());
        entity.setDecoracao(decoracaoEntity);
        return entity;
    }

    public List<ImagemDecoracaoEntity> toEntityShallow(List<ImagemDecoracao> imagensDecoracao, DecoracaoEntity decoracaoEntity) {
        if (imagensDecoracao == null) {
            return null;
        }
        return imagensDecoracao.stream()
                .map(imagem -> toEntityShallow(imagem, decoracaoEntity))
                .toList();
    }

    public ImagemDecoracao toDomainShallow(ImagemDecoracaoEntity entity) {
        if (entity == null) {
            return null;
        }
        ImagemDecoracao imagemDecoracao = new ImagemDecoracao();
        imagemDecoracao.setId(entity.getId());
        imagemDecoracao.setUrl(entity.getUrl());

        return imagemDecoracao;
    }

    public List<ImagemDecoracao> toDomainShallow(List<ImagemDecoracaoEntity> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::toDomainShallow)
                .toList();
    }
}
