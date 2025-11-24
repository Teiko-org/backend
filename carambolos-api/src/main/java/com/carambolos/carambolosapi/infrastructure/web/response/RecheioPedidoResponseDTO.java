package com.carambolos.carambolosapi.infrastructure.web.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de resposta para dados do Recheio Pedido")
public record RecheioPedidoResponseDTO(
        @Schema(description = "Identificador único do recheio pedido", example = "1")
        Integer id,

        @Schema(description = "Sabor do primeiro recheio", example = "Brigadeiro")
        String sabor1,

        @Schema(description = "Sabor do segundo recheio", example = "Beijinho")
        String sabor2,

        @Schema(description = "Valor total do recheio pedido", example = "10.50")
        Double valor
) {

}
