package com.carambolos.carambolosapi.system.config;

import com.carambolos.carambolosapi.application.gateways.MassaGateway;
import com.carambolos.carambolosapi.application.usecases.MassaUseCase;
import com.carambolos.carambolosapi.infrastructure.gateways.impl.MassaGatewayImpl;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.MassaMapper;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.MassaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MassaConfig {

    @Bean
    MassaUseCase createUseCase(MassaGateway massaGateway) {
        return new MassaUseCase(massaGateway);
    }

    @Bean
    MassaGateway createGateway(MassaRepository repository, MassaMapper mapper) {
        return new MassaGatewayImpl(repository, mapper);
    }

    @Bean
    MassaMapper createMapper() {
        return new MassaMapper();
    }
}
