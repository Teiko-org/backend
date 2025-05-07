package com.carambolos.carambolosapi.controller.response;

import com.carambolos.carambolosapi.model.projection.RecheioPedidoProjection;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

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
    public static List<RecheioPedidoResponseDTO> toResponse(List<RecheioPedidoProjection> projections) {
        return projections.stream().map(RecheioPedidoResponseDTO::toResponse).toList();
    }

    public static RecheioPedidoResponseDTO toResponse(RecheioPedidoProjection projection) {
        return new RecheioPedidoResponseDTO(
                projection.getId(),
                projection.getSabor1(),
                projection.getSabor2(),
                projection.getValor()
        );
    }
}
