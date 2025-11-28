package com.carambolos.carambolosapi.infrastructure.web.response;

import com.carambolos.carambolosapi.infrastructure.persistence.entity.Fornada;
import com.carambolos.carambolosapi.infrastructure.persistence.projection.ProdutoFornadaDaVezProjection;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "Fornada com seus itens (fornada da vez) sem imagens")
public record FornadaComItensResponse(
        @Schema(description = "ID da fornada") Integer fornadaId,
        @Schema(description = "Data de início") LocalDate dataInicio,
        @Schema(description = "Data de fim") LocalDate dataFim,
        @Schema(description = "Itens dessa fornada") List<ProdutoFornadaDaVezResponse> itens
) {
    public static FornadaComItensResponse of(Fornada fornada, List<ProdutoFornadaDaVezProjection> itensProjecao) {
        return new FornadaComItensResponse(
                fornada.getId(),
                fornada.getDataInicio(),
                fornada.getDataFim(),
                ProdutoFornadaDaVezResponse.toProdutoFornadaDaVezResonse(itensProjecao)
        );
    }
}


