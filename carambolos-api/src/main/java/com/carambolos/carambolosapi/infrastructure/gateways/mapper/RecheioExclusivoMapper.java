package com.carambolos.carambolosapi.infrastructure.gateways.mapper;

import com.carambolos.carambolosapi.domain.entity.RecheioExclusivo;
import com.carambolos.carambolosapi.domain.projection.RecheioExclusivoProjection;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.RecheioExclusivoEntity;
import com.carambolos.carambolosapi.infrastructure.web.request.RecheioExclusivoRequestDTO;
import com.carambolos.carambolosapi.infrastructure.web.response.RecheioExclusivoResponseDTO;

import java.util.List;

public class RecheioExclusivoMapper {
    public RecheioExclusivoEntity toEntity(RecheioExclusivo recheioExclusivo) {
        if (recheioExclusivo == null) {
            return null;
        }

        RecheioExclusivoEntity recheioExclusivoEntity = new RecheioExclusivoEntity();
        recheioExclusivoEntity.setId(recheioExclusivo.getId());
        recheioExclusivoEntity.setNome(recheioExclusivo.getNome());
        recheioExclusivoEntity.setRecheioUnitarioId1(recheioExclusivo.getRecheioUnitarioId1());
        recheioExclusivoEntity.setRecheioUnitarioId2(recheioExclusivo.getRecheioUnitarioId2());
        recheioExclusivoEntity.setAtivo(recheioExclusivo.getAtivo());
        return recheioExclusivoEntity;
    }

    public RecheioExclusivo toDomain(RecheioExclusivoEntity recheioExclusivoEntity) {
        if (recheioExclusivoEntity == null) {
            return null;
        }

        RecheioExclusivo recheioExclusivo = new RecheioExclusivo();
        recheioExclusivo.setId(recheioExclusivoEntity.getId());
        recheioExclusivo.setNome(recheioExclusivoEntity.getNome());
        recheioExclusivo.setRecheioUnitarioId1(recheioExclusivoEntity.getRecheioUnitarioId1());
        recheioExclusivo.setRecheioUnitarioId2(recheioExclusivoEntity.getRecheioUnitarioId2());
        recheioExclusivo.setAtivo(recheioExclusivoEntity.getAtivo());
        return recheioExclusivo;
    }

    public List<RecheioExclusivoResponseDTO> toRecheioExclusivoResponse(List<RecheioExclusivoProjection> projections) {
        return projections.stream().map(this::toRecheioExclusivoResponse).toList();
    }

    public RecheioExclusivoResponseDTO toRecheioExclusivoResponse(RecheioExclusivoProjection projection) {
        return new RecheioExclusivoResponseDTO(
                projection.getId(),
                projection.getNome(),
                projection.getSabor1(),
                projection.getSabor2()
        );
    }

    public RecheioExclusivo toRecheioExclusivo(RecheioExclusivoRequestDTO request) {
        RecheioExclusivo recheioExclusivo = new RecheioExclusivo();
        recheioExclusivo.setRecheioUnitarioId1(request.idRecheioUnitario1());
        recheioExclusivo.setRecheioUnitarioId2(request.idRecheioUnitario2());
        recheioExclusivo.setNome(request.nome());
        return recheioExclusivo;
    }
}
