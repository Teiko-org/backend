package com.carambolos.carambolosapi.system.config;

import com.carambolos.carambolosapi.application.gateways.AdicionalDecoracaoGateway;
import com.carambolos.carambolosapi.application.gateways.BoloGateway;
import com.carambolos.carambolosapi.application.gateways.CoberturaGateway;
import com.carambolos.carambolosapi.application.gateways.DecoracaoGateway;
import com.carambolos.carambolosapi.application.gateways.EnderecoGateway;
import com.carambolos.carambolosapi.application.gateways.MassaGateway;
import com.carambolos.carambolosapi.application.gateways.PedidoBoloGateway;
import com.carambolos.carambolosapi.application.gateways.RecheioPedidoGateway;
import com.carambolos.carambolosapi.application.gateways.UsuarioGateway;
import com.carambolos.carambolosapi.application.usecases.PedidoBoloUseCase;
import com.carambolos.carambolosapi.infrastructure.gateways.impl.PedidoBoloGatewayImpl;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.CoberturaMapper;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.DecoracaoMapper;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.MassaMapper;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.PedidoBoloCompletoMapper;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.PedidoBoloMapper;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.RecheioPedidoMapper;
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
            MassaGateway massaGateway,
            RecheioPedidoGateway recheioPedidoGateway,
            CoberturaGateway coberturaGateway,
            DecoracaoGateway decoracaoGateway,
            AdicionalDecoracaoGateway adicionalDecoracaoGateway,
            MassaMapper massaMapper,
            RecheioPedidoMapper recheioPedidoMapper,
            CoberturaMapper coberturaMapper,
            DecoracaoMapper decoracaoMapper,
            ResumoPedidoRepository resumoPedidoRepository
    ) {
        return new PedidoBoloCompletoMapper(
                boloGateway,
                enderecoGateway,
                usuarioGateway,
                massaGateway,
                recheioPedidoGateway,
                coberturaGateway,
                decoracaoGateway,
                adicionalDecoracaoGateway,
                massaMapper,
                recheioPedidoMapper,
                coberturaMapper,
                decoracaoMapper,
                resumoPedidoRepository
        );
    }
}
