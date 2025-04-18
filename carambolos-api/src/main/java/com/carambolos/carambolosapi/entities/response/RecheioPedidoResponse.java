package com.carambolos.carambolosapi.entities.response;

import com.carambolos.carambolosapi.model.projection.RecheioPedidoProjection;

public record RecheioPedidoResponse(
        Integer id,
        String sabor1,
        String sabor2,
        Double valor
) {
    public static RecheioPedidoResponse toResponse(RecheioPedidoProjection projection) {
        return new RecheioPedidoResponse(
                projection.getId(),
                projection.getSabor1(),
                projection.getSabor2(),
                projection.getValor()
        );
    }
}
