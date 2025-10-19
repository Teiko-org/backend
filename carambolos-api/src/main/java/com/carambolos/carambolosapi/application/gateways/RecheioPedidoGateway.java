package com.carambolos.carambolosapi.application.gateways;

import com.carambolos.carambolosapi.domain.entity.RecheioPedido;
import com.carambolos.carambolosapi.domain.projection.RecheioPedidoProjection;

import java.util.List;

public interface RecheioPedidoGateway {
    RecheioPedido save(RecheioPedido recheioPedido);
    RecheioPedidoProjection buscarRecheioPedidoExclusivoPorId(Integer id);
    RecheioPedidoProjection buscarRecheioPedidoUnitariosPorId(Integer id);
    Boolean existsById(Integer id);
    RecheioPedido findById(Integer id);
    List<RecheioPedidoProjection> listarRecheiosPedido();
}
