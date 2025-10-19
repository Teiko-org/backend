package com.carambolos.carambolosapi.system.config;

import com.carambolos.carambolosapi.application.gateways.RecheioExclusivoGateway;
import com.carambolos.carambolosapi.application.gateways.RecheioUnitarioGateway;
import com.carambolos.carambolosapi.application.usecases.RecheioExclusivoUseCase;
import com.carambolos.carambolosapi.infrastructure.gateways.impl.RecheioExclusivoGatewayImpl;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.RecheioExclusivoMapper;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.RecheioExclusivoRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RecheioExclusivoConfig {
    @Bean
    RecheioExclusivoUseCase createRecheioExclusivoUseCase(RecheioExclusivoGateway recheioExclusivoGateway, RecheioUnitarioGateway recheioUnitarioGateway) {
        return new RecheioExclusivoUseCase(recheioExclusivoGateway, recheioUnitarioGateway);
    }

    @Bean
    RecheioExclusivoGateway createRecheioExclusivoGateway(RecheioExclusivoRepository repository, RecheioExclusivoMapper mapper) {
        return new RecheioExclusivoGatewayImpl(repository, mapper);
    }

    @Bean
    RecheioExclusivoMapper createRecheioExclusivoMapper() {
        return new RecheioExclusivoMapper();
    }
}
