package com.carambolos.carambolosapi.application.usecases;

import com.carambolos.carambolosapi.application.gateways.AdicionalDecoracaoGateway;
import com.carambolos.carambolosapi.domain.entity.AdicionalDecoracao;

import java.util.List;

public class AdicionalDecoracaoUseCase {
    private final AdicionalDecoracaoGateway gateway;

    public AdicionalDecoracaoUseCase(AdicionalDecoracaoGateway gateway) {
        this.gateway = gateway;
    }

    public List<AdicionalDecoracao> buscarAdicionaisPorDecoracao() {
        return gateway.buscarTodosAdicionaisPorDecoracao();
    }
}
