package com.carambolos.carambolosapi.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Par de mês e ano disponível para filtro de fornadas")
public record MesAnoResponse(
        @Schema(description = "Ano", example = "2025") Integer ano,
        @Schema(description = "Mês", example = "5") Integer mes
) {}


