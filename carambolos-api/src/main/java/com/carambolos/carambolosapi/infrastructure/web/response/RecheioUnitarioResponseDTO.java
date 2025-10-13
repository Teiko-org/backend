package com.carambolos.carambolosapi.infrastructure.web.response;

import com.carambolos.carambolosapi.infrastructure.persistence.entity.RecheioUnitarioEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "DTO de resposta para dados do Recheio Unitário")
public record RecheioUnitarioResponseDTO(
        @Schema(description = "Identificador único do recheio unitário", example = "1")
        Integer id,

        @Schema(description = "Sabor do recheio unitário", example = "Chocolate")
        String sabor,

        @Schema(description = "Descrição adicional do recheio unitário", example = "Chocolate meio amargo")
        String descricao,

        @Schema(description = "Valor do recheio unitário", example = "5.75")
        Double valor
) {

}
