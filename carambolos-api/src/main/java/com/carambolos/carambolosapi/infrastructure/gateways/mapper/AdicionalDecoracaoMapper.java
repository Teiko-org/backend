package com.carambolos.carambolosapi.infrastructure.gateways.mapper;

import com.carambolos.carambolosapi.domain.entity.AdicionalDecoracao;
import com.carambolos.carambolosapi.domain.entity.AdicionalDecoracaoSummary;
import com.carambolos.carambolosapi.domain.entity.AdicionalItem;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.AdicionalDecoracaoEntity;
import com.carambolos.carambolosapi.infrastructure.persistence.projection.AdicionalDecoracaoProjection;

import java.util.ArrayList;
import java.util.List;

public class AdicionalDecoracaoMapper {
    public AdicionalDecoracaoSummary toDomain(AdicionalDecoracaoProjection projection) {
        if (projection == null) {
            return null;
        }

        AdicionalDecoracaoSummary adicionalDecoracaoSummary = new AdicionalDecoracaoSummary();

        adicionalDecoracaoSummary.setDecoracaoId(projection.getDecoracaoId());
        adicionalDecoracaoSummary.setNomeDecoracao(projection.getNomeDecoracao());

        // Handle null adicionais - return empty list instead of throwing NPE
        List<AdicionalItem> adicionaisPossiveisTrimmed;
        if (projection.getAdicionais() != null && !projection.getAdicionais().isBlank()) {
            var adicionaisDescricoes = List.of(projection.getAdicionais().split(","));
            var adicionaisIds = projection.getAdicionaisIds() != null ? 
                List.of(projection.getAdicionaisIds().split(",")) : 
                new ArrayList<String>();

            adicionaisPossiveisTrimmed = new ArrayList<>();
            for (int i = 0; i < adicionaisDescricoes.size(); i++) {
                String descricao = adicionaisDescricoes.get(i).trim();
                Integer id = null;
                
                if (i < adicionaisIds.size()) {
                    try {
                        id = Integer.parseInt(adicionaisIds.get(i).trim());
                    } catch (NumberFormatException e) {
                        id = null;
                    }
                }
                
                adicionaisPossiveisTrimmed.add(new AdicionalItem(id, descricao));
            }
        } else {
            adicionaisPossiveisTrimmed = List.of();
        }

        adicionalDecoracaoSummary.setAdicionaisPossiveis(
                adicionaisPossiveisTrimmed
        );


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
