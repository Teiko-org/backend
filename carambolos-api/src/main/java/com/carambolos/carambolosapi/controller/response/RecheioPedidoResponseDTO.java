package com.carambolos.carambolosapi.controller.response;

import com.carambolos.carambolosapi.model.projection.RecheioPedidoProjection;

import java.util.List;

public record RecheioPedidoResponseDTO(
        Integer id,
        String sabor1,
        String sabor2,
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
