package com.carambolos.carambolosapi.application.gateways;

import com.carambolos.carambolosapi.domain.entity.Massa;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.MassaEntity;

import java.util.List;

public interface MassaGateway {
    Massa save(Massa massa);
    Boolean existsByIdAndIsAtivo(Integer id, Boolean isAtivo);
    Integer countBySaborAndIdNotAndIsAtivo(String sabor, Integer id, Boolean isAtivo);
    Massa atualizarMassa(Massa massaEntity, Integer id);
    List<Massa> listarMassas();
    Massa findById(Integer id);
    void deletarMassa(Integer id);
    int countBySaborAndIsAtivo(String sabor, Boolean isAtivo);
    String getMassaAtivaPorSabor(Integer massaId);
}

