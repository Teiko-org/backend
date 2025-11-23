package com.carambolos.carambolosapi.infrastructure.gateways.mapper;

import com.carambolos.carambolosapi.domain.entity.Decoracao;
import com.carambolos.carambolosapi.domain.entity.ImagemDecoracao;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.DecoracaoEntity;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.ImagemDecoracaoEntity;
import com.carambolos.carambolosapi.infrastructure.web.response.DecoracaoResponseDTO;

import java.util.List;

public class DecoracaoMapper {

    public List<DecoracaoEntity> toEntity(List<Decoracao> decoracoes, ImagemDecoracaoMapper imagemDecoracaoMapper) {
        if (decoracoes == null) {
            return null;
        }
        return decoracoes.stream()
                .map(decoracao -> toEntity(decoracao, imagemDecoracaoMapper))
                .toList();
    }

    public DecoracaoEntity toEntity(Decoracao decoracao, ImagemDecoracaoMapper imagemDecoracaoMapper) {
        if (decoracao == null) {
            return null;
        }
        DecoracaoEntity entity = new DecoracaoEntity();
        entity.setId(decoracao.getId());
        // Corrigido: passa DecoracaoEntity para o método shallow
        entity.setImagens(imagemDecoracaoMapper.toEntityShallow(decoracao.getImagens(), entity));
        entity.setObservacao(decoracao.getObservacao());
        entity.setNome(decoracao.getNome());
        entity.setAtivo(decoracao.getAtivo());
        entity.setCategoria(decoracao.getCategoria());
        return entity;
    }

    public List<Decoracao> toDomain(List<DecoracaoEntity> entities, ImagemDecoracaoMapper imagemDecoracaoMapper) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(entity -> toDomain(entity, imagemDecoracaoMapper))
                .toList();
    }

    public Decoracao toDomain(DecoracaoEntity entity, ImagemDecoracaoMapper imagemDecoracaoMapper) {
        if (entity == null) {
            return null;
        }
        Decoracao decoracao = new Decoracao();
        decoracao.setId(entity.getId());
        // Usa método superficial para evitar recursão
        decoracao.setImagens(imagemDecoracaoMapper.toDomainShallow(entity.getImagens()));
        decoracao.setObservacao(entity.getObservacao());
        decoracao.setNome(entity.getNome());
        decoracao.setAtivo(entity.getAtivo());
        decoracao.setCategoria(entity.getCategoria());
        return decoracao;
    }

    public DecoracaoResponseDTO toResponse(Decoracao decoracao) {
        List<String> urls = decoracao.getImagens()
                .stream()
                .map(ImagemDecoracao::getUrl)
                .toList();

        return new DecoracaoResponseDTO(
                decoracao.getId(),
                urls,
                decoracao.getObservacao(),
                decoracao.getNome(),
                decoracao.getCategoria()
        );
    }

    public List<DecoracaoResponseDTO> toResponse(List<Decoracao> decoracoes) {
        if (decoracoes == null) {
            return null;
        }

        return decoracoes.stream()
                .map(this::toResponse)
                .toList();
    }
}
