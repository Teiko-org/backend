package com.carambolos.carambolosapi.system.config;

import com.carambolos.carambolosapi.application.gateways.CarrinhoGateway;
import com.carambolos.carambolosapi.application.gateways.UsuarioGateway;
import com.carambolos.carambolosapi.application.usecases.CarrinhoUseCases;
import com.carambolos.carambolosapi.infrastructure.gateways.impl.CarrinhoGatewayImpl;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.CarrinhoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CarrinhoConfig {

    @Bean
    public CarrinhoGateway carrinhoGateway(CarrinhoRepository repository) {
        return new CarrinhoGatewayImpl(repository);
    }

    @Bean
    public CarrinhoUseCases carrinhoUseCases(CarrinhoGateway carrinhoGateway, UsuarioGateway usuarioGateway, ObjectMapper objectMapper) {
        return new CarrinhoUseCases(carrinhoGateway, usuarioGateway, objectMapper);
    }
}