package com.carambolos.carambolosapi.application.gateways;

import com.carambolos.carambolosapi.infrastructure.messaging.dto.PedidoEvento;

public interface PedidoEventosGateway {
    void publicarPedidoCriado(PedidoEvento evento);
}


