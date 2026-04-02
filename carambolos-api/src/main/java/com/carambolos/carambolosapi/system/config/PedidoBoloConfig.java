package com.carambolos.carambolosapi.system.config;

import com.carambolos.carambolosapi.application.gateways.BoloGateway;
import com.carambolos.carambolosapi.application.gateways.EnderecoGateway;
import com.carambolos.carambolosapi.application.gateways.PedidoBoloGateway;
import com.carambolos.carambolosapi.application.gateways.UsuarioGateway;
import com.carambolos.carambolosapi.application.usecases.PedidoBoloUseCase;
import com.carambolos.carambolosapi.infrastructure.gateways.impl.PedidoBoloGatewayImpl;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.BoloMapper;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.PedidoBoloCompletoMapper;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.PedidoBoloMapper;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.PedidoBoloRepository;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.ResumoPedidoRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PedidoBoloConfig {
    @Bean
    PedidoBoloUseCase createPedidoBoloUseCase(
            PedidoBoloGateway pedidoBoloGateway,
            BoloGateway boloGateway,
            EnderecoGateway enderecoGateway
    ) {
        return new PedidoBoloUseCase(
                pedidoBoloGateway,
                boloGateway,
                enderecoGateway
        );
    }

    @Bean
    PedidoBoloGateway createPedidoBoloGateway(PedidoBoloRepository repository, PedidoBoloMapper mapper) {
        return new PedidoBoloGatewayImpl(
                repository,
                mapper
        );
    }

    @Bean
    PedidoBoloMapper createPedidoBoloMapper() {
        return new PedidoBoloMapper();
    }

    @Bean
    PedidoBoloCompletoMapper createPedidoBoloCompletoMapper(
            BoloGateway boloGateway,
            EnderecoGateway enderecoGateway,
            UsuarioGateway usuarioGateway,
            BoloMapper boloMapper,
            ResumoPedidoRepository resumoPedidoRepository
    ) {
        return new PedidoBoloCompletoMapper(boloGateway, enderecoGateway, usuarioGateway, boloMapper, resumoPedidoRepository);
    }
}
