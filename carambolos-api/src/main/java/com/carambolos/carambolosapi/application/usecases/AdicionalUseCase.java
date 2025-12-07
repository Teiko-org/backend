package com.carambolos.carambolosapi.application.usecases;

import com.carambolos.carambolosapi.application.gateways.AdicionalGateway;
import com.carambolos.carambolosapi.domain.entity.Adicional;

import java.util.List;

public class AdicionalUseCase {
    private final AdicionalGateway gateway;

    public AdicionalUseCase(AdicionalGateway gateway) {
        this.gateway = gateway;
    }

    public List<Adicional> listarAdicionais() {
        return gateway.listarAdicionais();
    }
}
