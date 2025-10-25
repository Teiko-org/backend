package com.carambolos.carambolosapi.infrastructure.web.request;

import com.carambolos.carambolosapi.infrastructure.persistence.entity.ResumoPedidoEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record ResumoPedidoRequestDTO (
        @Schema(description = "Data prevista para entrega", example = "2024-12-25T15:00:00")
        LocalDateTime dataEntrega,

        Integer pedidoFornadaId,

        Integer pedidoBoloId

) {

}
