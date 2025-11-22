package com.carambolos.carambolosapi.infrastructure.web.response;

import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "DTO de resposta para dados do Recheio Exclusivo")
public record RecheioExclusivoResponseDTO(

        @Schema(description = "Identificador único do recheio exclusivo", example = "1")
        Integer id,

        @Schema(description = "Nome do recheio exclusivo", example = "Doce dos Sonhos")
        String nome,

        @Schema(description = "Sabor do primeiro recheio unitário", example = "Brigadeiro")
        String sabor1,

        @Schema(description = "Sabor do segundo recheio unitário", example = "Beijinho")
        String sabor2
) {
}
