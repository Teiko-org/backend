package com.carambolos.carambolosapi.controller.request;

import com.carambolos.carambolosapi.model.Massa;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO para requisições de criação ou atualização de Massa")
public record MassaRequestDTO(

        @NotBlank
        @Schema(description = "Sabor da massa", example = "Baunilha")
        String sabor,

        @DecimalMin("0.0")
        @Schema(description = "Valor da massa", example = "12.50")
        Double valor
) {
    public static Massa toMassa(MassaRequestDTO request) {
        Massa massa = new Massa();
        massa.setSabor(request.sabor);
        massa.setValor(request.valor);
        return massa;
    }
}
