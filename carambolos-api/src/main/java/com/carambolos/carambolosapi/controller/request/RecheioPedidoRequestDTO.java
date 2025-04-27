package com.carambolos.carambolosapi.controller.request;

import com.carambolos.carambolosapi.model.RecheioPedido;

public record RecheioPedidoRequestDTO(
    Integer idExclusivo,
    Integer idUnitario1,
    Integer idUnitario2
){
    public static RecheioPedido toRecheioPedido(RecheioPedidoRequestDTO request) {
        if(request == null) {
            return null;
        }

        RecheioPedido recheioPedido = new RecheioPedido();
        recheioPedido.setRecheioExclusivo(request.idExclusivo());
        recheioPedido.setRecheioUnitarioId1(request.idUnitario1());
        recheioPedido.setRecheioUnitarioId2(request.idUnitario2);

        return recheioPedido;
    }
}
