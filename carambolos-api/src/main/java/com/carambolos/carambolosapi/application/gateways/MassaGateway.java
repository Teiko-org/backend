package com.carambolos.carambolosapi.application.gateways;

import com.carambolos.carambolosapi.domain.entity.Massa;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.MassaEntity;

import java.util.List;

public interface MassaGateway {
    Massa cadastrarMassa(Massa massa);
    Massa atualizarMassa(Massa massaEntity, Integer id);
    List<Massa> listarMassas();
    Massa buscarMassaPorId(Integer id);
    void deletarMassa(Integer id);
}
