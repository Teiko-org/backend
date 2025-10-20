package com.carambolos.carambolosapi.system.config;

import com.carambolos.carambolosapi.application.gateways.BoloGateway;
import com.carambolos.carambolosapi.application.gateways.CoberturaGateway;
import com.carambolos.carambolosapi.application.gateways.MassaGateway;
import com.carambolos.carambolosapi.application.gateways.RecheioPedidoGateway;
import com.carambolos.carambolosapi.application.usecases.BoloUseCase;
import com.carambolos.carambolosapi.infrastructure.gateways.impl.BoloGatewayImpl;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.BoloMapper;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.BoloRepository;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.DecoracaoRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BoloConfig {
    @Bean
    BoloGateway createBoloGateway(BoloRepository repository, BoloMapper mapper) {
        return new BoloGatewayImpl(repository, mapper);
    }

    @Bean
    BoloUseCase createBoloUseCase(
            CoberturaGateway coberturaGateway,
            MassaGateway massaGateway,
            RecheioPedidoGateway recheioPedidoGateway,
            BoloGateway boloGateway,
            DecoracaoRepository decoracaoRepository
    ) {
        return new BoloUseCase(
                coberturaGateway,
                massaGateway,
                recheioPedidoGateway,
                boloGateway,
                decoracaoRepository
        );
    }

    @Bean
    BoloMapper createBoloMapper() {
        return new BoloMapper();
    }
}
