package com.carambolos.carambolosapi.infrastructure.messaging.publishers;

import com.carambolos.carambolosapi.infrastructure.messaging.RabbitConfig;
import com.carambolos.carambolosapi.infrastructure.messaging.dto.PedidoEvento;
import org.slf4j.MDC;
import org.springframework.amqp.core.MessagePostProcessor;
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
        final String eventCorrelation = evento.getCorrelationId();
        final String effectiveCorrelationId =
                (eventCorrelation == null || eventCorrelation.isBlank())
                        ? MDC.get("correlationId")
                        : eventCorrelation;

        if ((eventCorrelation == null || eventCorrelation.isBlank())
                && effectiveCorrelationId != null && !effectiveCorrelationId.isBlank()) {
            evento.setCorrelationId(effectiveCorrelationId);
        }

        MessagePostProcessor mpp = message -> {
            if (effectiveCorrelationId != null && !effectiveCorrelationId.isBlank()) {
                message.getMessageProperties().setHeader("X-Correlation-Id", effectiveCorrelationId);
            }
            return message;
        };

        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE_CARAMBOLOS,
                RabbitConfig.ROUTING_PEDIDOS_CRIADO,
                evento,
                mpp
        );
    }
}