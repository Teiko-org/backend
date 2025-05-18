package com.carambolos.carambolosapi.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para criação ou atualização de uma decoração")
public record DecoracaoRequestDTO(
        @Schema(description = "Observações da decoração", example = "Tema Frozen, tons de azul")
        @Size(max = 70)
        String observacao
) {}
