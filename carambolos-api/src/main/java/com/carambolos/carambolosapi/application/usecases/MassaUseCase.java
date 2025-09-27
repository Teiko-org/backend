package com.carambolos.carambolosapi.application.usecases;

import com.carambolos.carambolosapi.application.gateways.MassaGateway;
import com.carambolos.carambolosapi.domain.entity.Massa;

import java.util.List;

public class MassaUseCase {
    private final MassaGateway massaGateway;

    public MassaUseCase(MassaGateway massaGateway) {
        this.massaGateway = massaGateway;
    }

    public Massa cadastrarMassa(Massa massa) {
        return massaGateway.cadastrarMassa(massa);
    }

    public Massa atualizarMassa(Massa massa, Integer id) {
        return massaGateway.atualizarMassa(massa, id);
    }

    public List<Massa> listarMassas() {
        return massaGateway.listarMassas();
    }

    public Massa buscarMassaPorId(Integer id) {
        return massaGateway.buscarMassaPorId(id);
    }

    public void deletarMassa(Integer id) {
        massaGateway.deletarMassa(id);
    }
}
