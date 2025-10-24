package com.carambolos.carambolosapi.infrastructure.web.response;

import com.carambolos.carambolosapi.infrastructure.persistence.entity.ResumoPedidoEntity;
import com.carambolos.carambolosapi.domain.enums.StatusEnum;

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
    public static ResumoPedidoResponseDTO toResumoPedidoResponse(ResumoPedidoEntity pedido) {
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

    public static List<ResumoPedidoResponseDTO> toResumoPedidoResponse(List<ResumoPedidoEntity> resumoPedidoEntities) {
        return resumoPedidoEntities.stream()
                .map(ResumoPedidoResponseDTO::toResumoPedidoResponse)
                .toList();
    }
}