package com.carambolos.carambolosapi.controller.response;

import com.carambolos.carambolosapi.model.ResumoPedido;
import com.carambolos.carambolosapi.model.enums.StatusEnum;

import java.time.LocalDateTime;
import java.util.List;

public record ResumoPedidoResponseDTO (
        Integer id,
        StatusEnum status,
        Double valor,
        LocalDateTime dataPedido,
        LocalDateTime dataEntrega,
        Integer pedidoFornadaId,
        Integer pedidoBoloId
)
{
    public static ResumoPedidoResponseDTO toResumoPedidoResponse(ResumoPedido pedido) {
        return new ResumoPedidoResponseDTO(
                pedido.getId(),
                pedido.getStatus(),
                pedido.getValor(),
                pedido.getDataPedido(),
                pedido.getDataEntrega(),
                pedido.getPedidoFornadaId(),
                pedido.getPedidoBoloId()
        );
    }

    public static List<ResumoPedidoResponseDTO> toResumoPedidoResponse(List<ResumoPedido> resumoPedidos) {
        return resumoPedidos.stream()
                .map(ResumoPedidoResponseDTO::toResumoPedidoResponse)
                .toList();
    }
}