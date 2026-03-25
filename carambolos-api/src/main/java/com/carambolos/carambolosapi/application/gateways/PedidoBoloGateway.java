package com.carambolos.carambolosapi.application.gateways;

import com.carambolos.carambolosapi.domain.entity.PedidoBolo;

import java.time.LocalDateTime;
import java.util.List;

public interface PedidoBoloGateway {
    List<PedidoBolo> findAll();
    List<PedidoBolo> findAllByDataUltimaAtualizacaoBetween(LocalDateTime dataInicio, LocalDateTime dataFim);
    PedidoBolo findById(Integer id);
    Boolean existsByIdAndIsAtivoTrue(Integer id);
    PedidoBolo save(PedidoBolo pedidoBolo);
}
