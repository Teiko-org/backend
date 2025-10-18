package com.carambolos.carambolosapi.application.gateways;

import com.carambolos.carambolosapi.domain.entity.RecheioExclusivo;
import com.carambolos.carambolosapi.domain.projection.RecheioExclusivoProjection;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.RecheioExclusivoEntity;

import java.util.List;

public interface RecheioExclusivoGateway {
    int countByRecheioUnitarioIds(int recheioUnitarioId1, int recheioUnitarioId2);
    RecheioExclusivo save(RecheioExclusivo recheioExclusivo);
    RecheioExclusivoProjection buscarRecheioExclusivoPorId(int id);
    List<RecheioExclusivoProjection> listarRecheiosExclusivos();
    RecheioExclusivo findById(int id);
}
