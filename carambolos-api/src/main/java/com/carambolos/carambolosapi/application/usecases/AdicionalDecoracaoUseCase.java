package com.carambolos.carambolosapi.application.usecases;

import com.carambolos.carambolosapi.application.gateways.AdicionalDecoracaoGateway;
import com.carambolos.carambolosapi.domain.entity.AdicionalDecoracaoSummary;

import java.util.List;
import java.util.Optional;

public class AdicionalDecoracaoUseCase {
    private final AdicionalDecoracaoGateway gateway;

    public AdicionalDecoracaoUseCase(AdicionalDecoracaoGateway gateway) {
        this.gateway = gateway;
    }

    public List<AdicionalDecoracaoSummary> buscarAdicionaisPorDecoracao() {
        return gateway.buscarTodosAdicionaisPorDecoracao();
    }

    public Optional<AdicionalDecoracaoSummary> buscarAdicionaisPorDecoracaoId(Integer decoracaoId) {
        return gateway.buscarAdicionaisPorDecoracaoId(decoracaoId);
    }
}
