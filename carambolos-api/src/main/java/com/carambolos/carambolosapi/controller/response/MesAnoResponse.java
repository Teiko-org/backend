package com.carambolos.carambolosapi.controller.response;

import com.carambolos.carambolosapi.model.projection.MesAnoProjection;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Par de mês e ano disponível para filtro de fornadas")
public record MesAnoResponse(
        @Schema(description = "Ano", example = "2025") Integer ano,
        @Schema(description = "Mês", example = "5") Integer mes
) {
    public static MesAnoResponse fromProjection(MesAnoProjection projection) {
        return new MesAnoResponse(projection.getAno(), projection.getMes());
    }

    public static List<MesAnoResponse> fromProjection(List<MesAnoProjection> projections) {
        return projections.stream().map(MesAnoResponse::fromProjection).toList();
    }
}


