package com.carambolos.carambolosapi.infrastructure.web.response;

import com.carambolos.carambolosapi.infrastructure.persistence.entity.CoberturaEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "DTO de resposta para dados de Cobertura")
public record CoberturaResponseDTO(

        @Schema(description = "Identificador único da cobertura", example = "1")
        Integer id,

        @Schema(description = "Cor da cobertura", example = "Vermelho")
        String cor,

        @Schema(description = "Descrição da cobertura", example = "Cobertura de morango com brilho")
        String descricao
) {

}
