package com.carambolos.carambolosapi.application.usecases;

import com.carambolos.carambolosapi.application.exception.EntidadeJaExisteException;
import com.carambolos.carambolosapi.application.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.application.gateways.RecheioUnitarioGateway;
import com.carambolos.carambolosapi.domain.entity.RecheioUnitario;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.RecheioUnitarioEntity;

import java.util.List;
import java.util.Optional;

public class RecheioUnitarioUseCase {
    private final RecheioUnitarioGateway gateway;

    public RecheioUnitarioUseCase(RecheioUnitarioGateway gateway) {
        this.gateway = gateway;
    }

    public RecheioUnitario cadastrarRecheioUnitario(RecheioUnitario recheioUnitario) {
        if (gateway.countBySaborIgnoreCaseAndIsAtivo(recheioUnitario.getSabor(), true) > 0) {
            throw new EntidadeJaExisteException("Recheio com o sabor %s já existe".formatted(recheioUnitario.getSabor()));
        }
        return gateway.save(recheioUnitario);
    }

    public List<RecheioUnitario> listarRecheiosUnitarios() {
        return gateway.findAll().stream().filter(RecheioUnitario::getAtivo).toList();
    }

    public RecheioUnitario buscarPorId(Integer id) {
        return gateway.findById(id);
    }

    public RecheioUnitario atualizarRecheioUnitario(RecheioUnitario recheioUnitario, Integer id) {
        RecheioUnitario recheioExistente = gateway.findById(id);

        if (gateway.countBySaborIgnoreCaseAndIdNotAndIsAtivo(recheioUnitario.getSabor(), id, true) > 0) {
            throw new EntidadeJaExisteException("Recheio com o sabor %s já existe".formatted(recheioUnitario.getSabor()));
        }

        if (recheioUnitario.getSabor() != null) {
            recheioExistente.setSabor(recheioUnitario.getSabor());
        }
        if (recheioUnitario.getDescricao() != null) {
            recheioExistente.setDescricao(recheioUnitario.getDescricao());
        }
        if (recheioUnitario.getValor() != null) {
            recheioExistente.setValor(recheioUnitario.getValor());
        }

        recheioExistente.setId(id);

        return gateway.save(recheioExistente);
    }

    public void deletarRecheioUnitario(Integer id) {
        gateway.deletarRecheioUnitario(id);
    }
}
