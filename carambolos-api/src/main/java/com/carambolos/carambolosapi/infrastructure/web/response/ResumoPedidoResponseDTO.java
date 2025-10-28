package com.carambolos.carambolosapi.infrastructure.web.response;

import com.carambolos.carambolosapi.domain.entity.ResumoPedido;
import com.carambolos.carambolosapi.domain.enums.StatusEnum;
import org.springframework.data.domain.Page;

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

    public static Page<ResumoPedidoResponseDTO> toResumoPedidoResponse(Page<ResumoPedido> page) {
        return page.map(ResumoPedidoResponseDTO::toResumoPedidoResponse);
    }
}