package com.carambolos.carambolosapi.controller.request;

import com.carambolos.carambolosapi.model.PedidoFornada;

import java.time.LocalDate;

public record PedidoFornadaUpdateRequestDTO(
        Integer quantidade,
        LocalDate dataPrevisaoEntrega
) {
    public PedidoFornada toEntity() {
        PedidoFornada pedidoFornada = new PedidoFornada();
        pedidoFornada.setQuantidade(quantidade);
        pedidoFornada.setDataPrevisaoEntrega(dataPrevisaoEntrega);
        return pedidoFornada;
    }
}
