package com.carambolos.carambolosapi.controller.response;

import com.carambolos.carambolosapi.model.Massa;

import java.util.List;

public record MassaResponseDTO(
        Integer id,
        String sabor,
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
