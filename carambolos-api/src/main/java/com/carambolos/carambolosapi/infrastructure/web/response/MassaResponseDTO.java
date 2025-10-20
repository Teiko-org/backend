package com.carambolos.carambolosapi.infrastructure.web.response;

import com.carambolos.carambolosapi.infrastructure.persistence.entity.MassaEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "DTO de resposta para dados da Massa")
public record MassaResponseDTO (
    @Schema(description = "Identificador único da massa", example = "1")
    Integer id,

    @Schema(description = "Sabor da massa", example = "Chocolate")
    String sabor,

    @Schema(description = "Valor da massa", example = "15.90")
    Double valor
) {

}
