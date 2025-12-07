package com.carambolos.carambolosapi.application.usecases;


import com.carambolos.carambolosapi.application.exception.EntidadeJaExisteException;
import com.carambolos.carambolosapi.application.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.application.gateways.CoberturaGateway;
import com.carambolos.carambolosapi.domain.entity.Cobertura;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.CoberturaEntity;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Optional;

public class CoberturaUseCase {
    private final CoberturaGateway gateway;

    public CoberturaUseCase(CoberturaGateway gateway) {
        this.gateway = gateway;
    }

    public Cobertura cadastrarCobertura(Cobertura cobertura) {
        String cor = cobertura.getCor();
        String descricao = cobertura.getDescricao();
        Integer coberturasExistentes = gateway.countByCorAndDescricaoIgnoreCaseAndIsAtivoTrue(cor, descricao);

        if (coberturasExistentes > 0) {
            throw new EntidadeJaExisteException("Cobertura com a cor %s e descricao %s ja existente".formatted(cor, descricao));
        }

        return gateway.save(cobertura);
    }

    public Cobertura atualizarCobertura(Cobertura novaCobertura, Integer id) {
        Cobertura cobertura = gateway.findById(id);

        String cor = novaCobertura.getCor();
        String descricao = novaCobertura.getDescricao();

        int coberturasExistentes = gateway.countByCorAndDescricaoAndIdNotAndIsAtivoTrue(
                cor,
                descricao,
                id
        );

        if (coberturasExistentes > 0) {
            throw new EntidadeJaExisteException("Cobertura com cor %s e descricao %s ja existe".formatted(cor, descricao));
        }

        return gateway.save(
                verificarCampos(cobertura, novaCobertura)
        );
    }

    @Cacheable(cacheNames = "coberturas")
    public List<Cobertura> listarCoberturas() {
        return gateway.findAll().stream().filter(Cobertura::getAtivo).toList();
    }

    @Cacheable(cacheNames = "coberturas:porId", key = "#id")
    public Cobertura buscarCoberturaPorId(Integer id) {
        return gateway.findById(id);
    }

    public void deletarCobertura(Integer id) {
        Cobertura cobertura = gateway.findById(id);

        cobertura.setAtivo(false);
        gateway.save(cobertura);
    }

    private Cobertura verificarCampos(Cobertura cobertura, Cobertura novaCobertura) {
        if (novaCobertura.getCor() == null) {
            novaCobertura.setCor(cobertura.getCor());
        }

        if (novaCobertura.getDescricao() == null) {
            novaCobertura.setDescricao(cobertura.getDescricao());
        }
        novaCobertura.setId(cobertura.getId());
        return novaCobertura;
    }
}



