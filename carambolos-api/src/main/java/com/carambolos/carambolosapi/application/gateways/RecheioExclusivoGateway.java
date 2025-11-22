package com.carambolos.carambolosapi.application.gateways;

import com.carambolos.carambolosapi.domain.entity.RecheioExclusivo;
import com.carambolos.carambolosapi.infrastructure.persistence.projection.RecheioExclusivoProjection;

import java.util.List;

public interface RecheioExclusivoGateway {
    int countByRecheioUnitarioIds(int recheioUnitarioId1, int recheioUnitarioId2);
    RecheioExclusivo save(RecheioExclusivo recheioExclusivo);
    RecheioExclusivoProjection buscarRecheioExclusivoPorId(int id);
    List<RecheioExclusivoProjection> listarRecheiosExclusivos();
    RecheioExclusivo findById(int id);
}
