package com.carambolos.carambolosapi.controller.dto;

import com.carambolos.carambolosapi.model.Massa;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

public record MassaRequestDTO(
        @NotBlank
        String sabor,
        @DecimalMin("0.0")
        Double valor
) {
    public static Massa toMassa(MassaRequestDTO request) {
        Massa massa = new Massa();

        massa.setSabor(request.sabor);
        massa.setValor(request.valor);

        return massa;
    }
}
