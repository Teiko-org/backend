package com.carambolos.carambolosapi.application.gateways;

import com.carambolos.carambolosapi.domain.entity.PedidoFornada;

import java.util.List;
import java.util.Optional;

public interface PedidoFornadaGateway {
    PedidoFornada save(PedidoFornada pedidoFornada);
    Optional<PedidoFornada> findById(Integer id);
    List<PedidoFornada> findAll();
    boolean existsByIdAndIsAtivoTrue(Integer id);
}
