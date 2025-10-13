package com.carambolos.carambolosapi.infrastructure.web.request;

import com.carambolos.carambolosapi.infrastructure.persistence.entity.RecheioUnitarioEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO para requisição de criação ou atualização de Recheio Unitário")
public record RecheioUnitarioRequestDTO(

        @NotBlank
        @Schema(description = "Sabor do recheio unitário", example = "Chocolate ao leite", required = true)
        String sabor,

        @Schema(description = "Descrição adicional do recheio", example = "Recheio cremoso de chocolate belga")
        String descricao,

        @NotNull
        @DecimalMin("0.0")
        @Schema(description = "Valor do recheio unitário", example = "5.50", required = true)
        Double valor
) {
}
