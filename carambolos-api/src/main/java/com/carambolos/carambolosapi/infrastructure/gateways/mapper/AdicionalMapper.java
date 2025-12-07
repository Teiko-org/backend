package com.carambolos.carambolosapi.infrastructure.gateways.mapper;

import com.carambolos.carambolosapi.domain.entity.Adicional;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.AdicionalEntity;
import com.carambolos.carambolosapi.infrastructure.web.response.AdicionalResponseDTO;

import java.util.List;

public class AdicionalMapper {
    public Adicional toDomain(AdicionalEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Adicional(
                entity.getId(),
                entity.getDescricao(),
                entity.getAtivo()
        );
    }

    public List<Adicional> toDomain(List<AdicionalEntity> entities) {
        return entities.stream()
                .map(this::toDomain)
                .toList();
    }

    public AdicionalResponseDTO toResponseDTO(Adicional domain) {
        if (domain == null) {
            return null;
        }

        return new AdicionalResponseDTO(
                domain.getId(),
                domain.getDescricao(),
                domain.getAtivo()
        );
    }

    public List<AdicionalResponseDTO> toResponseDTO(List<Adicional> domains) {
        return domains.stream()
                .map(this::toResponseDTO)
                .toList();
    }
}
