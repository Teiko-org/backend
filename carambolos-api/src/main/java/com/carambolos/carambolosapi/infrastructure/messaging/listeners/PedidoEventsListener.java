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
import org.springframework.context.annotation.Profile;
import io.micrometer.core.instrument.Counter;

@Profile("worker")
@Component
public class PedidoEventsListener {

    private static final Logger log = LoggerFactory.getLogger(PedidoEventsListener.class);
    private final Timer consumeTimer;
    private final Counter successCounter;
    private final Counter failureCounter;

    public PedidoEventsListener(MeterRegistry meterRegistry) {
        this.consumeTimer = Timer.builder("amqp.consume")
                .description("Tempo para processar eventos AMQP")
                .tag("queue", RabbitConfig.QUEUE_PEDIDOS)
                .register(meterRegistry);
        this.successCounter = Counter.builder("amqp.consume.success")
                .description("Total de mensagens processadas com sucesso")
                .tag("queue", RabbitConfig.QUEUE_PEDIDOS)
                .register(meterRegistry);
        this.failureCounter = Counter.builder("amqp.consume.failure")
                .description("Total de mensagens que falharam no processamento")
                .tag("queue", RabbitConfig.QUEUE_PEDIDOS)
                .register(meterRegistry);
    }

    @RabbitListener(queues = RabbitConfig.QUEUE_PEDIDOS)
    public void onPedidoCriado(@Payload PedidoEvento evento, @Header(name = "X-Correlation-Id", required = false) String correlationId) {
        consumeTimer.record(() -> {
            try {
                // Aqui integraríamos com os Use Cases (ex.: PedidoFornadaUseCases)
                log.info("[Rabbit] Recebido evento: tipo={} pedidoId={} origem={} correlationId={}",
                        evento.getEvento(), evento.getPedidoId(), evento.getOrigem(), correlationId);
                // TODO: chamar use cases para refletir o evento no domínio quando o schema/evento estiver definido
                successCounter.increment();
            } catch (RuntimeException ex) {
                failureCounter.increment();
                log.error("[Rabbit] Erro ao processar evento do pedidoId={} correlationId={}",
                        evento.getPedidoId(), correlationId, ex);
                throw ex;
            }
        });
    }
}


