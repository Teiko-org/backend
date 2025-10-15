package com.carambolos.carambolosapi.system.config;

import com.carambolos.carambolosapi.application.gateways.EnderecoGateway;
import com.carambolos.carambolosapi.application.usecases.EnderecoUseCase;
import com.carambolos.carambolosapi.application.usecases.UsuarioUseCase;
import com.carambolos.carambolosapi.infrastructure.gateways.impl.EnderecoGatewayImpl;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.EnderecoMapper;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.EnderecoRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnderecoConfig {

    @Bean
    EnderecoUseCase createEnderecoCase(EnderecoGateway enderecoGateway, UsuarioUseCase usuarioUseCase) {
        return new EnderecoUseCase(enderecoGateway, usuarioUseCase);
    }

    @Bean
    EnderecoGateway enderecoGateway(EnderecoRepository enderecoRepository, EnderecoMapper enderecoMapper) {
        return new EnderecoGatewayImpl(enderecoRepository, enderecoMapper);
    }

    @Bean
    EnderecoMapper enderecoMapper() {
        return new EnderecoMapper();
    }

}
