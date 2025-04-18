package com.carambolos.carambolosapi.entities.request;

import com.carambolos.carambolosapi.exception.EntidadeImprocessavelException;
import com.carambolos.carambolosapi.model.RecheioPedido;

public record RecheioPedidoRequest (
    Integer idExclusivo,
    Integer idUnitario1,
    Integer idUnitario2
){
    public static RecheioPedido toRecheioPedido(RecheioPedidoRequest request) {
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
