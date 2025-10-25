package com.carambolos.carambolosapi.infrastructure.web.controllers;

import com.carambolos.carambolosapi.infrastructure.messaging.dto.PedidoEvento;
import com.carambolos.carambolosapi.infrastructure.messaging.publishers.PedidoEventPublisher;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/_test/publish")
@Profile("local")
public class RabbitTestController {

    private final PedidoEventPublisher publisher;

    public RabbitTestController(PedidoEventPublisher publisher) {
        this.publisher = publisher;
    }

    @PostMapping("/pedido-criado")
    public ResponseEntity<Void> publishPedidoCriado(@RequestBody PedidoEvento evento) {
        publisher.publishPedidoCriado(evento);
        return ResponseEntity.accepted().build();
    }
}


