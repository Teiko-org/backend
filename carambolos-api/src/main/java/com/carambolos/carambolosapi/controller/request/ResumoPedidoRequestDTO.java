package com.carambolos.carambolosapi.controller.request;

import com.carambolos.carambolosapi.model.ResumoPedido;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ResumoPedidoRequestDTO (
        @Schema(description = "Valor total do pedido", example = "120.50")
        @NotNull
        Double valor,

        @Schema(description = "Data prevista para entrega", example = "2024-12-25T15:00:00")
        LocalDateTime dataEntrega,

        Integer pedidoFornadaId,

        Integer pedidoBoloId

) {
    public static ResumoPedido toResumoPedido(ResumoPedidoRequestDTO request) {
        if (request == null) {
            return null;
        }

        ResumoPedido resumoPedido = new ResumoPedido();
        resumoPedido.setValor(request.valor);
        resumoPedido.setDataEntrega(request.dataEntrega);
        resumoPedido.setPedidoFornadaId(request.pedidoFornadaId);
        resumoPedido.setPedidoBoloId(request.pedidoBoloId);

        return resumoPedido;
    }

}
