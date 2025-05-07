package com.carambolos.carambolosapi.controller.response;

import com.carambolos.carambolosapi.model.Cobertura;
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
    public static List<CoberturaResponseDTO> toResponse(List<Cobertura> coberturas) {
        return coberturas.stream().map(CoberturaResponseDTO::toResponse).toList();
    }
    public static CoberturaResponseDTO toResponse(Cobertura cobertura) {
        if (cobertura == null) {
            return null;
        }

        CoberturaResponseDTO response = new CoberturaResponseDTO(
                cobertura.getId(),
                cobertura.getCor(),
                cobertura.getDescricao()
        );

        return response;
    }
}
