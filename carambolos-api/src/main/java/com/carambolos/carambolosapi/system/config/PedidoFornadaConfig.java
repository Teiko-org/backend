package com.carambolos.carambolosapi.system.config;

import com.carambolos.carambolosapi.application.gateways.FornadaDaVezGateway;
import com.carambolos.carambolosapi.application.gateways.PedidoFornadaGateway;
import com.carambolos.carambolosapi.application.usecases.PedidoFornadaUseCases;
import com.carambolos.carambolosapi.infrastructure.gateways.impl.FornadaDaVezRepositoryGateway;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.FornadaDaVezRepository;
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
}
