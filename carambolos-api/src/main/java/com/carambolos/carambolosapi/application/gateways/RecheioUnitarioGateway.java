package com.carambolos.carambolosapi.application.gateways;

import com.carambolos.carambolosapi.domain.entity.RecheioUnitario;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.List;

public interface RecheioUnitarioGateway {
    int countBySaborIgnoreCaseAndIsAtivo(String sabor, Boolean isAtivo);
    RecheioUnitario save(RecheioUnitario recheioUnitario);
    List<RecheioUnitario> findAll();
    RecheioUnitario findById(Integer id);
    int countBySaborIgnoreCaseAndIdNotAndIsAtivo(String sabor, Integer id, Boolean isAtivo);
    void deletarRecheioUnitario(Integer id);
    Boolean existsByIdAndIsAtivoTrue(Integer id);
}
