package com.carambolos.carambolosapi.infrastructure.web.request;

import com.carambolos.carambolosapi.infrastructure.persistence.entity.PedidoFornada;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "DTO para atualizar um pedido de fornada")
public record PedidoFornadaUpdateRequestDTO(

        @Schema(description = "Quantidade de produtos no pedido", example = "50")
        Integer quantidade,

        @Schema(description = "Data prevista para entrega do pedido", example = "2025-05-10")
        LocalDate dataPrevisaoEntrega

) {

    public PedidoFornada toEntity() {
        PedidoFornada pedidoFornada = new PedidoFornada();
        pedidoFornada.setQuantidade(quantidade);
        pedidoFornada.setDataPrevisaoEntrega(dataPrevisaoEntrega);
        return pedidoFornada;
    }
}


