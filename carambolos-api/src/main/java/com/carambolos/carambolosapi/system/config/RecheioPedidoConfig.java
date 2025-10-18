package com.carambolos.carambolosapi.system.config;

import com.carambolos.carambolosapi.application.gateways.RecheioPedidoGateway;
import com.carambolos.carambolosapi.application.usecases.RecheioPedidoUseCase;
import com.carambolos.carambolosapi.infrastructure.gateways.impl.RecheioPedidoGatewayImpl;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.RecheioPedidoMapper;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.RecheioPedidoRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RecheioPedidoConfig {
    @Bean
    RecheioPedidoUseCase creatRecheioPedidoUseCase(RecheioPedidoGateway gateway) {
        return new RecheioPedidoUseCase(gateway);
    }

    @Bean
    RecheioPedidoGateway createRecheioPedidoGateway(RecheioPedidoRepository repository, RecheioPedidoMapper mapper) {
        return new RecheioPedidoGatewayImpl(repository, mapper);
    }

    @Bean
    RecheioPedidoMapper createRecheioPedidoMapper() {
        return new RecheioPedidoMapper();
    }
}
