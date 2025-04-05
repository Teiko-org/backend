package com.carambolos.carambolosapi.entities.request;

import com.carambolos.carambolosapi.model.RecheioUnitario;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RecheioUnitarioRequest(
    @NotBlank
    String sabor,
    @NotBlank
    String descricao,
    @NotNull
    @DecimalMin("0.0")
    Double valor
) {
    public static RecheioUnitario toRecheioUnitario(RecheioUnitarioRequest request) {
        RecheioUnitario recheioUnitario = new RecheioUnitario();
        recheioUnitario.setSabor(request.sabor);
        recheioUnitario.setDescricao(request.descricao);
        recheioUnitario.setValor(request.valor);

        return recheioUnitario;
    }
}
