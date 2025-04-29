package com.carambolos.carambolosapi.controller.dto;

import com.carambolos.carambolosapi.model.RecheioUnitario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO para requisição de criação ou atualização de Recheio Unitário")
public record RecheioUnitarioRequestDTO(

        @NotBlank
        @Schema(description = "Sabor do recheio unitário", example = "Chocolate ao leite", required = true)
        String sabor,

        @Schema(description = "Descrição adicional do recheio", example = "Recheio cremoso de chocolate belga")
        String descricao,

        @NotNull
        @DecimalMin("0.0")
        @Schema(description = "Valor do recheio unitário", example = "5.50", required = true)
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
