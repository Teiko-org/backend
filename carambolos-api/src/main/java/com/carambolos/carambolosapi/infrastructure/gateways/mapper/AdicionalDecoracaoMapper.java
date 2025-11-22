package com.carambolos.carambolosapi.infrastructure.gateways.mapper;

import com.carambolos.carambolosapi.domain.entity.AdicionalDecoracao;
import com.carambolos.carambolosapi.infrastructure.persistence.projection.AdicionalDecoracaoProjection;

import java.util.List;

public class AdicionalDecoracaoMapper {
    public AdicionalDecoracao toDomain(AdicionalDecoracaoProjection projection) {
        if (projection == null) {
            return null;
        }

        AdicionalDecoracao adicionalDecoracao = new AdicionalDecoracao();

        adicionalDecoracao.setDecoracaoId(projection.getDecoracaoId());
        adicionalDecoracao.setNomeDecoracao(projection.getNomeDecoracao());

        var adicionaisPossiveis = List.of(projection.getAdicionais().split(","));
        var adicionaisPossiveisTrimmed = adicionaisPossiveis.stream()
                .map(String::trim)
                .toList();

        adicionalDecoracao.setAdicionaisPossiveis(
                adicionaisPossiveisTrimmed
        );


        return adicionalDecoracao;
    }

    public List<AdicionalDecoracao> toDomain(List<AdicionalDecoracaoProjection> projections) {
        return projections.stream()
                .map(this::toDomain)
                .toList();
    }
}
