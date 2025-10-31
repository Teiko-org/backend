package com.carambolos.carambolosapi.infrastructure.messaging.publishers;

import com.carambolos.carambolosapi.application.gateways.PedidoEventosGateway;
import com.carambolos.carambolosapi.infrastructure.messaging.RabbitConfig;
import com.carambolos.carambolosapi.infrastructure.messaging.dto.PedidoEvento;
import org.slf4j.MDC;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

@Component
public class PedidoEventosGatewayImpl implements PedidoEventosGateway {

    private final RabbitTemplate rabbitTemplate;
    private final Timer publishTimer;

    public PedidoEventosGatewayImpl(RabbitTemplate rabbitTemplate, MeterRegistry meterRegistry) {
        this.rabbitTemplate = rabbitTemplate;
        this.publishTimer = Timer.builder("amqp.publish")
                .description("Tempo para publicar eventos AMQP")
                .tag("routingKey", RabbitConfig.ROUTING_PEDIDOS_CRIADO)
                .register(meterRegistry);
    }

    @Override
    public void publicarPedidoCriado(PedidoEvento evento) {
        String correlationId = MDC.get("correlationId");
        if (evento.getCorrelationId() == null || evento.getCorrelationId().isBlank()) {
            evento.setCorrelationId(correlationId);
        }
        MessagePostProcessor mpp = message -> {
            if (correlationId != null) {
                message.getMessageProperties().setHeader("X-Correlation-Id", correlationId);
            }
            return message;
        };
        publishTimer.record(() -> rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE_CARAMBOLOS,
                RabbitConfig.ROUTING_PEDIDOS_CRIADO,
                evento,
                mpp
        ));
    }
}


