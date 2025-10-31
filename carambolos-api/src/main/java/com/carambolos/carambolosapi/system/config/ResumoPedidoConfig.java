package com.carambolos.carambolosapi.system.config;

import com.carambolos.carambolosapi.application.gateways.*;
import com.carambolos.carambolosapi.application.usecases.ResumoPedidoUseCase;
import com.carambolos.carambolosapi.infrastructure.gateways.impl.ResumoPedidoGatewayImpl;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.EnderecoMapper;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.ResumoPedidoMapper;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.ResumoPedidoRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResumoPedidoConfig {

    @Bean
    ResumoPedidoGateway createResumoPedidoGateway(ResumoPedidoRepository repository, ResumoPedidoMapper mapper) {
        return new ResumoPedidoGatewayImpl(repository, mapper);
    }

    @Bean
    ResumoPedidoUseCase createResumoPedidoUseCase(
            ResumoPedidoGateway resumoPedidoGateway,
            PedidoBoloGateway pedidoBoloGateway,
            PedidoFornadaGateway pedidoFornadaGateway,
            FornadaDaVezGateway fornadaDaVezGateway,
            ProdutoFornadaGateway produtoFornadaGateway,
            BoloGateway boloGateway,
            RecheioPedidoGateway recheioPedidoGateway,
            RecheioExclusivoGateway recheioExclusivoGateway,
            RecheioUnitarioGateway recheioUnitarioGateway,
            MassaGateway massaGateway,
            CoberturaGateway coberturaGateway,
            EnderecoGateway enderecoGateway,
            EnderecoMapper enderecoMapper
    ) {
        return new ResumoPedidoUseCase(
                resumoPedidoGateway,
                pedidoBoloGateway,
                pedidoFornadaGateway,
                fornadaDaVezGateway,
                produtoFornadaGateway,
                boloGateway,
                recheioPedidoGateway,
                recheioExclusivoGateway,
                recheioUnitarioGateway,
                massaGateway,
                coberturaGateway,
                enderecoGateway,
                enderecoMapper
        );
    }

    @Bean
    ResumoPedidoMapper createResumoPedidoMapper() {
        return new ResumoPedidoMapper();
    }
}
