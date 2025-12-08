package com.carambolos.carambolosapi.infrastructure.gateways.mapper;

import com.carambolos.carambolosapi.domain.entity.AdicionalDecoracao;
import com.carambolos.carambolosapi.domain.entity.AdicionalDecoracaoSummary;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.AdicionalDecoracaoEntity;
import com.carambolos.carambolosapi.infrastructure.persistence.projection.AdicionalDecoracaoProjection;

import java.util.List;

public class AdicionalDecoracaoMapper {
    public AdicionalDecoracaoSummary toDomain(AdicionalDecoracaoProjection projection) {
        if (projection == null) {
            return null;
        }

        AdicionalDecoracaoSummary adicionalDecoracaoSummary = new AdicionalDecoracaoSummary();

        adicionalDecoracaoSummary.setDecoracaoId(projection.getDecoracaoId());
        adicionalDecoracaoSummary.setNomeDecoracao(projection.getNomeDecoracao());

        // Tratar caso quando getAdicionais() retorna null (quando decoração não tem adicionais)
        String adicionaisStr = projection.getAdicionais();
        if (adicionaisStr == null || adicionaisStr.trim().isEmpty()) {
            adicionalDecoracaoSummary.setAdicionaisPossiveis(List.of());
        } else {
            var adicionaisPossiveis = List.of(adicionaisStr.split(","));
            var adicionaisPossiveisTrimmed = adicionaisPossiveis.stream()
                    .map(String::trim)
                    .filter(s -> !s.isEmpty()) // Remover strings vazias após trim
                    .toList();

            adicionalDecoracaoSummary.setAdicionaisPossiveis(adicionaisPossiveisTrimmed);
        }


        return adicionalDecoracaoSummary;
    }

    public List<AdicionalDecoracaoSummary> toDomain(List<AdicionalDecoracaoProjection> projections) {
        return projections.stream()
                .map(this::toDomain)
                .toList();
    }

    public AdicionalDecoracao toDomain(AdicionalDecoracaoEntity entity) {
        if (entity == null) {
            return null;
        }

        AdicionalDecoracao adicionalDecoracao = new AdicionalDecoracao();

        adicionalDecoracao.setId(entity.getId());
        adicionalDecoracao.setDecoracaoId(entity.getDecoracaoId());
        adicionalDecoracao.setAdicionalId(entity.getAdicionalId());

        return adicionalDecoracao;
    }
}
