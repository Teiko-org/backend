package com.carambolos.carambolosapi.application.gateways;

import com.carambolos.carambolosapi.domain.entity.RecheioUnitario;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.RecheioUnitarioEntity;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.List;
import java.util.Optional;

public interface RecheioUnitarioGateway {
    int countBySaborIgnoreCaseAndIsAtivo(String sabor, Boolean isAtivo);
    RecheioUnitario save(RecheioUnitario recheioUnitario);
    List<RecheioUnitario> findAll();
    RecheioUnitario findById(Integer id);
    int countBySaborIgnoreCaseAndIdNotAndIsAtivo(String sabor, Integer id, Boolean isAtivo);
    void deletarRecheioUnitario(Integer id);
    Boolean existsByIdAndIsAtivoTrue(Integer id);
    Optional<RecheioUnitarioEntity> findEntityById(Integer id);
}
