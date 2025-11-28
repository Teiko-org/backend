package com.carambolos.carambolosapi.system.config;

import com.carambolos.carambolosapi.application.gateways.AdicionalGateway;
import com.carambolos.carambolosapi.application.usecases.AdicionalUseCase;
import com.carambolos.carambolosapi.infrastructure.gateways.impl.AdicionalGatewayImpl;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.AdicionalMapper;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.AdicionalRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdicionalConfig {

    @Bean
    public AdicionalGateway createAdicionalGateway(
            AdicionalRepository adicionalRepository,
            AdicionalMapper adicionalMapper
    ) {
        return new AdicionalGatewayImpl(adicionalRepository, adicionalMapper);
    }

    @Bean
    public AdicionalUseCase createAdicionalUseCase(AdicionalGateway adicionalGateway) {
        return new AdicionalUseCase(adicionalGateway);
    }

    @Bean
    public AdicionalMapper createAdicionalMapper() {
        return new AdicionalMapper();
    }
}
