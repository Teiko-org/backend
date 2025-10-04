package com.carambolos.carambolosapi.application.gateways;

import com.carambolos.carambolosapi.domain.entity.Cobertura;

import java.util.List;

public interface CoberturaGateway {
    int countByCorAndDescricaoIgnoreCaseAndIsAtivoTrue(String cor, String descricao);
    Cobertura save(Cobertura cobertura);
    Cobertura findById(Integer id);
    int countByCorAndDescricaoAndIdNotAndIsAtivoTrue(String cor, String descricao, Integer id);
    List<Cobertura> findAll();
}
