package com.carambolos.carambolosapi.infrastructure.messaging.publishers;

import com.carambolos.carambolosapi.infrastructure.messaging.RabbitConfig;
import com.carambolos.carambolosapi.infrastructure.messaging.dto.PedidoEvento;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("local")
public class PedidoEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public PedidoEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishPedidoCriado(PedidoEvento evento) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE_CARAMBOLOS,
                RabbitConfig.ROUTING_PEDIDOS_CRIADO,
                evento
        );
    }
}


