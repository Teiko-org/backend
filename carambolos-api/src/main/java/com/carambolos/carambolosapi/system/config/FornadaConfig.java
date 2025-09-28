package com.carambolos.carambolosapi.system.config;

import com.carambolos.carambolosapi.application.gateways.FornadaGateway;
import com.carambolos.carambolosapi.application.usecases.FornadasUseCases;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FornadaConfig {
    @Bean
    public FornadasUseCases fornadasUseCases(FornadaGateway gateway) {
        return new FornadasUseCases(gateway);
    }
}
