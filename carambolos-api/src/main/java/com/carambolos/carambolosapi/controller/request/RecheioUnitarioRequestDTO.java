package com.carambolos.carambolosapi.controller.request;

import com.carambolos.carambolosapi.model.RecheioUnitario;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RecheioUnitarioRequestDTO(
    @NotBlank
    String sabor,
    String descricao,
    @NotNull
    @DecimalMin("0.0")
    Double valor
) {
    public static RecheioUnitario toRecheioUnitario(RecheioUnitarioRequestDTO request) {
        RecheioUnitario recheioUnitario = new RecheioUnitario();
        recheioUnitario.setSabor(request.sabor);
        recheioUnitario.setDescricao(request.descricao);
        recheioUnitario.setValor(request.valor);

        return recheioUnitario;
    }
}
