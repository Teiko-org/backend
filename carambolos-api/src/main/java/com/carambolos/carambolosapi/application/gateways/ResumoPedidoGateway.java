package com.carambolos.carambolosapi.application.gateways;

import com.carambolos.carambolosapi.domain.entity.ResumoPedido;
import com.carambolos.carambolosapi.domain.enums.StatusEnum;

import java.time.LocalDateTime;
import java.util.List;

public interface ResumoPedidoGateway {
    List<ResumoPedido> findAllByIsAtivoTrue();
    ResumoPedido findByIdAndIsAtivoTrue(Integer id);
    List<ResumoPedido> findByDataPedidoBetweenAndIsAtivoTrue(LocalDateTime comecoData, LocalDateTime fimData);
    List<ResumoPedido> findByStatusAndIsAtivoTrue(StatusEnum status);
    ResumoPedido save(ResumoPedido resumoPedido);
    boolean existsByIdAndIsAtivoTrue(Integer id);
}
