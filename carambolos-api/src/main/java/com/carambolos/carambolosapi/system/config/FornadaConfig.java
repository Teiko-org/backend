package com.carambolos.carambolosapi.system.config;

import com.carambolos.carambolosapi.application.gateways.FornadaGateway;
import com.carambolos.carambolosapi.application.usecases.FornadasUseCases;
import com.carambolos.carambolosapi.infrastructure.gateways.impl.FornadaGatewayImpl;
import com.carambolos.carambolosapi.infrastructure.gateways.mapperEntity.FornadaEntityMapper;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.FornadaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FornadaConfig {
    @Bean
    public FornadasUseCases fornadasUseCases(FornadaGateway gateway) {
        return new FornadasUseCases(gateway);
    }

    @Bean
    public FornadaGateway fornadaGateway(FornadaRepository repository, FornadaEntityMapper mapper) {
        return new FornadaGatewayImpl(repository);
    }

    @Bean
    public FornadaEntityMapper fornadaEntityMapper() {
        return new FornadaEntityMapper();
    }
}
