package com.carambolos.carambolosapi.application.gateways;

import com.carambolos.carambolosapi.domain.entity.RecheioPedido;
import com.carambolos.carambolosapi.domain.projection.RecheioPedidoProjection;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.RecheioPedidoEntity;

import java.util.List;
import java.util.Optional;

public interface RecheioPedidoGateway {
    RecheioPedido save(RecheioPedido recheioPedido);
    RecheioPedidoProjection buscarRecheioPedidoExclusivoPorId(Integer id);
    RecheioPedidoProjection buscarRecheioPedidoUnitariosPorId(Integer id);
    Boolean existsById(Integer id);
    RecheioPedido findById(Integer id);
    List<RecheioPedidoProjection> listarRecheiosPedido();
    Boolean existsByIdAndIsAtivoTrue(Integer id);
    Optional<RecheioPedidoEntity> findEntityById(Integer id);
}
