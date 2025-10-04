package com.carambolos.carambolosapi.system.config;

import com.carambolos.carambolosapi.application.gateways.FornadaDaVezGateway;
import com.carambolos.carambolosapi.application.gateways.PedidoFornadaGateway;
import com.carambolos.carambolosapi.application.usecases.PedidoFornadaUseCases;
import com.carambolos.carambolosapi.infrastructure.gateways.impl.FornadaDaVezGatewayImpl;
import com.carambolos.carambolosapi.infrastructure.gateways.impl.PedidoFornadaGatewayImpl;
import com.carambolos.carambolosapi.infrastructure.gateways.mapperEntity.FornadaDaVezEntityMapper;
import com.carambolos.carambolosapi.infrastructure.gateways.mapperEntity.PedidoFornadaEntityMapper;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.FornadaDaVezRepository;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.PedidoFornadaRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

@Configuration
public class PedidoFornadaConfig {

    @Bean
    public PedidoFornadaUseCases pedidoFornadaUseCases(
            PedidoFornadaGateway pedidos,
            FornadaDaVezGateway fdv,
            com.carambolos.carambolosapi.infrastructure.persistence.jpa.EnderecoRepository end,
            com.carambolos.carambolosapi.infrastructure.persistence.jpa.UsuarioRepository usu
    ) {
        return new PedidoFornadaUseCases(pedidos, fdv, end, usu);
    }

    @Bean
    public PedidoFornadaGateway pedidoFornadaGateway(PedidoFornadaRepository repository, PedidoFornadaEntityMapper mapper) {
        return new PedidoFornadaGatewayImpl(repository);
    }

    @Bean
    public FornadaDaVezGateway fornadaDaVezGateway(FornadaDaVezRepository repository, FornadaDaVezEntityMapper mapper) {
        return new FornadaDaVezGatewayImpl(repository);
    }

    @Bean
    public PedidoFornadaEntityMapper pedidoFornadaEntityMapper() {
        return new PedidoFornadaEntityMapper();
    }

    @Bean
    public FornadaDaVezEntityMapper fornadaDaVezEntityMapper() {
        return new FornadaDaVezEntityMapper();
    }
}
