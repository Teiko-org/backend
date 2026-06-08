package com.carambolos.carambolosapi.system.config;

import com.carambolos.carambolosapi.application.gateways.EnderecoGateway;
import com.carambolos.carambolosapi.application.gateways.GeocodingGateway;
import com.carambolos.carambolosapi.application.usecases.EnderecoUseCase;
import com.carambolos.carambolosapi.application.usecases.UsuarioUseCase;
import com.carambolos.carambolosapi.infrastructure.gateways.impl.EnderecoGatewayImpl;
import com.carambolos.carambolosapi.infrastructure.gateways.impl.NominatimGeocodingGatewayImpl;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.EnderecoMapper;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.EnderecoRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class EnderecoConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public GeocodingGateway geocodingGateway(RestTemplate restTemplate) {
        return new NominatimGeocodingGatewayImpl(restTemplate);
    }

    @Bean
    EnderecoUseCase createEnderecoCase(EnderecoGateway enderecoGateway, UsuarioUseCase usuarioUseCase, GeocodingGateway geocodingGateway) {
        return new EnderecoUseCase(enderecoGateway, usuarioUseCase, geocodingGateway);
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
