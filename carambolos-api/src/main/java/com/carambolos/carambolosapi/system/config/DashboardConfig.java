package com.carambolos.carambolosapi.system.config;

import com.carambolos.carambolosapi.application.gateways.DashboardGateway;
import com.carambolos.carambolosapi.application.usecases.DashboardUseCase;
import com.carambolos.carambolosapi.infrastructure.gateways.impl.DashboardGatewayImpl;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DashboardConfig {

    @Bean
    DashboardUseCase createDashboardCase(DashboardGateway dashboardGateway) {
        return new DashboardUseCase(dashboardGateway);
    }

    @Bean
    DashboardGateway dashboardGateway(
            PedidoBoloRepository pedidoBoloRepository,
            BoloRepository boloRepository,
            PedidoFornadaRepository pedidoFornadaRepository,
            ProdutoFornadaRepository produtoFornadaRepository,
            FornadaDaVezRepository fornadaDaVezRepository,
            ResumoPedidoRepository resumoPedidoRepository,
            DecoracaoRepository decoracaoRepository,
            FornadaRepository fornadaRepository,
            MassaRepository massaRepository,
            RecheioPedidoRepository recheioPedidoRepository,
            RecheioUnitarioRepository recheioUnitarioRepository,
            RecheioExclusivoRepository recheioExclusivoRepository
    ) {
        return new DashboardGatewayImpl(
                pedidoBoloRepository,
                boloRepository,
                pedidoFornadaRepository,
                produtoFornadaRepository,
                fornadaDaVezRepository,
                resumoPedidoRepository,
                decoracaoRepository,
                fornadaRepository,
                massaRepository,
                recheioPedidoRepository,
                recheioUnitarioRepository,
                recheioExclusivoRepository
        );
    }
}
