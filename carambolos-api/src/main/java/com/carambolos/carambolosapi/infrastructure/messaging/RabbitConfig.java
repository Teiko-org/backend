package com.carambolos.carambolosapi.infrastructure.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE_CARAMBOLOS = "carambolos";
    public static final String DLX_CARAMBOLOS = "carambolos.dlx";
    public static final String QUEUE_PEDIDOS = "carambolos.pedidos";
    public static final String QUEUE_PEDIDOS_DLQ = "carambolos.pedidos.dlq";
    public static final String ROUTING_PEDIDOS_CRIADO = "pedidos.criado";

    @Bean
    public TopicExchange carambolosExchange() {
        return new TopicExchange(EXCHANGE_CARAMBOLOS, true, false);
    }

    @Bean
    public TopicExchange carambolosDlx() {
        return new TopicExchange(DLX_CARAMBOLOS, true, false);
    }

    @Bean
    public Queue pedidosQueue() {
        return new Queue(QUEUE_PEDIDOS, true, false, false, Map.of(
                "x-dead-letter-exchange", DLX_CARAMBOLOS,
                "x-dead-letter-routing-key", QUEUE_PEDIDOS_DLQ
        ));
    }

    @Bean
    public Queue pedidosDlq() {
        return new Queue(QUEUE_PEDIDOS_DLQ, true);
    }

    @Bean
    public Binding bindPedidos() {
        return BindingBuilder.bind(pedidosQueue())
                .to(carambolosExchange())
                .with(ROUTING_PEDIDOS_CRIADO);
    }

    @Bean
    public Binding bindPedidosDlq() {
        return BindingBuilder.bind(pedidosDlq())
                .to(carambolosDlx())
                .with(QUEUE_PEDIDOS_DLQ);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            SimpleRabbitListenerContainerFactoryConfigurer configurer,
            ConnectionFactory connectionFactory
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        // aplica propriedades spring.rabbitmq.listener.simple.* (concorrência, prefetch etc.)
        configurer.configure(factory, connectionFactory);
        factory.setMessageConverter(messageConverter());
        // retry stateless: 3 tentativas com backoff exponencial; após esgotar, DLQ (reject and don't requeue)
        factory.setAdviceChain(
                RetryInterceptorBuilder.stateless()
                        .maxAttempts(3)
                        .backOffOptions(1000L, 2.0, 5000L)
                        .recoverer(new RejectAndDontRequeueRecoverer())
                        .build()
        );
        factory.setDefaultRequeueRejected(false);
        return factory;
    }
}


