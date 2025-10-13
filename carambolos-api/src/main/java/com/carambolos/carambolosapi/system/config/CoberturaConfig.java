package com.carambolos.carambolosapi.system.config;

import com.carambolos.carambolosapi.application.gateways.CoberturaGateway;
import com.carambolos.carambolosapi.application.usecases.CoberturaUseCase;
import com.carambolos.carambolosapi.infrastructure.gateways.impl.CoberturaGatewayImpl;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.CoberturaMapper;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.CoberturaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoberturaConfig {
    @Bean
    CoberturaUseCase coberturaUseCase(CoberturaGateway coberturaGateway) {
        return new CoberturaUseCase(coberturaGateway);
    }

    @Bean
    CoberturaGateway coberturaGateway(CoberturaMapper coberturaMapper, CoberturaRepository coberturaRepository) {
        return new CoberturaGatewayImpl(coberturaRepository, coberturaMapper);
    }

    @Bean
    CoberturaMapper coberturaMapper() {
        return new CoberturaMapper();
    }
}
