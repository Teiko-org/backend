package com.carambolos.carambolosapi.application.gateways;

import com.carambolos.carambolosapi.domain.entity.PedidoBolo;

import java.time.LocalDate;
import java.util.List;

public interface PedidoBoloGateway {
    List<PedidoBolo> findAll();
    List<PedidoBolo> findAllByDataPrevisaoEntregaBetween(LocalDate dataInicio, LocalDate dataFim);
    List<PedidoBolo> findAllByDataPrevisaoEntregaMonth(Integer mes);
    PedidoBolo findById(Integer id);
    Boolean existsByIdAndIsAtivoTrue(Integer id);
    PedidoBolo save(PedidoBolo pedidoBolo);
}
