package com.carambolos.carambolosapi.system.config;

import com.carambolos.carambolosapi.application.gateways.EnderecoGateway;
import com.carambolos.carambolosapi.application.gateways.FornadaDaVezGateway;
import com.carambolos.carambolosapi.application.gateways.FornadaGateway;
import com.carambolos.carambolosapi.application.gateways.PedidoEventosGateway;
import com.carambolos.carambolosapi.application.gateways.PedidoFornadaGateway;
import com.carambolos.carambolosapi.application.gateways.ProdutoFornadaGateway;
import com.carambolos.carambolosapi.application.gateways.UsuarioGateway;
import com.carambolos.carambolosapi.application.usecases.PedidoFornadaUseCases;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.FornadaDaVezRepository;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.PedidoFornadaRepository;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.ProdutoFornadaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PedidoFornadaConfig {

    @Bean
    public PedidoFornadaUseCases pedidoFornadaUseCases(
            PedidoFornadaGateway pedidos,
            FornadaDaVezGateway fdv,
            FornadaGateway fornadas,
            EnderecoGateway end,
            UsuarioGateway usu,
            PedidoEventosGateway eventos
    ) {
        return new PedidoFornadaUseCases(pedidos, fdv, fornadas, end, usu, eventos);
    }

    @Bean
    public PedidoFornadaGateway pedidoFornadaGateway(PedidoFornadaRepository repository) {
        return new com.carambolos.carambolosapi.infrastructure.gateways.impl.PedidoFornadaGatewayImpl(repository);
    }

    @Bean
    public FornadaDaVezGateway fornadaDaVezGateway(FornadaDaVezRepository repository) {
        return new com.carambolos.carambolosapi.infrastructure.gateways.impl.FornadaDaVezGatewayImpl(repository);
    }

    @Bean
    public ProdutoFornadaGateway produtoFornadaGateway(ProdutoFornadaRepository repository) {
        return new com.carambolos.carambolosapi.infrastructure.gateways.impl.ProdutoFornadaGatewayImpl(repository);
    }
}
