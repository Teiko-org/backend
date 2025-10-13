package com.carambolos.carambolosapi.system.config;

import com.carambolos.carambolosapi.application.gateways.RecheioUnitarioGateway;
import com.carambolos.carambolosapi.application.usecases.RecheioUnitarioUseCase;
import com.carambolos.carambolosapi.infrastructure.gateways.impl.RecheioUnitarioGatewayImpl;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.RecheioUnitarioMapper;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.RecheioUnitarioRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RecheioUnitarioConfig {
    @Bean
    RecheioUnitarioUseCase useCase(RecheioUnitarioGateway gateway) {
        return new RecheioUnitarioUseCase(gateway);
    }

    @Bean
    RecheioUnitarioGateway gateway(RecheioUnitarioRepository repository, RecheioUnitarioMapper mapper) {
        return new RecheioUnitarioGatewayImpl(repository, mapper);
    }

    @Bean
    RecheioUnitarioMapper mapper() {
        return new RecheioUnitarioMapper();
    }
}
