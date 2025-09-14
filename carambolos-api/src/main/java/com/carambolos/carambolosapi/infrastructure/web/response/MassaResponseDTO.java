package com.carambolos.carambolosapi.infrastructure.web.response;

import com.carambolos.carambolosapi.domain.entity.Massa;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "DTO de resposta para dados da Massa")
public record MassaResponseDTO(

        @Schema(description = "Identificador único da massa", example = "1")
        Integer id,

        @Schema(description = "Sabor da massa", example = "Chocolate")
        String sabor,

        @Schema(description = "Valor da massa", example = "15.90")
        Double valor
) {
    public static List<MassaResponseDTO> toMassaResponse(List<Massa> massas) {
        return massas.stream().map(MassaResponseDTO::toMassaResponse).toList();
    }
    public static MassaResponseDTO toMassaResponse(Massa massa) {
        return new MassaResponseDTO(
                massa.id,
                massa.sabor,
                massa.valor
        );
    }
}
