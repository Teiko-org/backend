package com.carambolos.carambolosapi.infrastructure.messaging.listeners;

import com.carambolos.carambolosapi.infrastructure.messaging.RabbitConfig;
import com.carambolos.carambolosapi.infrastructure.messaging.dto.PedidoEvento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

@Component
public class PedidoEventsListener {

    private static final Logger log = LoggerFactory.getLogger(PedidoEventsListener.class);
    private final Timer consumeTimer;

    public PedidoEventsListener(MeterRegistry meterRegistry) {
        this.consumeTimer = Timer.builder("amqp.consume")
                .description("Tempo para processar eventos AMQP")
                .tag("queue", RabbitConfig.QUEUE_PEDIDOS)
                .register(meterRegistry);
    }

    @RabbitListener(queues = RabbitConfig.QUEUE_PEDIDOS)
    public void onPedidoCriado(@Payload PedidoEvento evento, @Header(name = "X-Correlation-Id", required = false) String correlationId) {
        consumeTimer.record(() -> {
            // Aqui integraríamos com os Use Cases (ex.: PedidoFornadaUseCases)
            log.info("[Rabbit] Recebido evento: tipo={} pedidoId={} origem={} correlationId={}",
                    evento.getEvento(), evento.getPedidoId(), evento.getOrigem(), correlationId);
            // TODO: chamar use cases para refletir o evento no domínio quando o schema/evento estiver definido
        });
    }
}


