package com.carambolos.carambolosapi.application.usecases;

import com.carambolos.carambolosapi.application.exception.EntidadeJaExisteException;
import com.carambolos.carambolosapi.application.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.application.gateways.MassaGateway;
import com.carambolos.carambolosapi.domain.entity.Massa;

import java.util.List;

public class MassaUseCase {
    private final MassaGateway massaGateway;

    public MassaUseCase(MassaGateway massaGateway) {
        this.massaGateway = massaGateway;
    }

    public Massa cadastrarMassa(Massa massa) {
        if (massaGateway.countBySaborAndIsAtivo(massa.getSabor(), true) > 0) {
            throw new EntidadeJaExisteException("Massa com sabor %s já existente".formatted(massa.getSabor()));
        }
        return massaGateway.save(massa);
    }

    public Massa atualizarMassa(Massa massa, Integer id) {
        if (!massaGateway.existsByIdAndIsAtivo(id, true)) {
            throw new EntidadeNaoEncontradaException("Massa com id %d não existente".formatted(id));
        }
        if (massaGateway.countBySaborAndIdNotAndIsAtivo(massa.getSabor(), id, true) > 0) {
            throw new EntidadeJaExisteException("Massa com saber %s ja existente".formatted(massa.getSabor()));
        }
        return massaGateway.atualizarMassa(massa, id);
    }

    public List<Massa> listarMassas() {
        return massaGateway.listarMassas();
    }

    public Massa buscarMassaPorId(Integer id) {
        return massaGateway.findById(id);
    }

    public void deletarMassa(Integer id) {
        massaGateway.deletarMassa(id);
    }
}
