package com.carambolos.carambolosapi.controller.response;

import com.carambolos.carambolosapi.model.Cobertura;

public record CoberturaResponseDTO(
        Integer id,
        String cor,
        String descricao
) {
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
