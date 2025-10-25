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

}