package com.carambolos.carambolosapi.controller.response;

import com.carambolos.carambolosapi.model.projection.RecheioExclusivoProjection;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;


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
    public static List<RecheioExclusivoResponseDTO> toRecheioExclusivoResponse(List<RecheioExclusivoProjection> projections) {
        return projections.stream().map(RecheioExclusivoResponseDTO::toRecheioExclusivoResponse).toList();
    }
    public static RecheioExclusivoResponseDTO toRecheioExclusivoResponse(RecheioExclusivoProjection projection) {
        return new RecheioExclusivoResponseDTO(
                projection.getId(),
                projection.getNome(),
                projection.getSabor1(),
                projection.getSabor2()
        );
    }

}
