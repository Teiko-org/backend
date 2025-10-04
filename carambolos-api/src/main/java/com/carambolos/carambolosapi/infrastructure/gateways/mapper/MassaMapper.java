package com.carambolos.carambolosapi.infrastructure.gateways.mapper;

import com.carambolos.carambolosapi.domain.entity.Massa;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.MassaEntity;
import com.carambolos.carambolosapi.infrastructure.web.request.MassaRequestDTO;
import com.carambolos.carambolosapi.infrastructure.web.response.MassaResponseDTO;

import java.util.List;

public class MassaMapper {
    public MassaEntity toEntity(Massa massaDomain) {
        return new MassaEntity(
                massaDomain.getSabor(),
                massaDomain.getValor(),
                massaDomain.getAtivo()
        );
    }

    public Massa toDomain(MassaEntity massaEntity) {
        return new Massa(
                massaEntity.getId(),
                massaEntity.getSabor(),
                massaEntity.getValor(),
                massaEntity.getAtivo()
        );
    }

    public List<Massa> toDomain(List<MassaEntity> massasEntity) {
        return massasEntity.stream().map(this::toDomain).toList();
    }

    public Massa toMassa(MassaRequestDTO request) {
        return new Massa(
                null,
                request.getSabor(),
                request.getValor(),
                true
        );
    }

    public MassaResponseDTO toResponse(Massa massaDomain) {
        MassaResponseDTO responseDto = new MassaResponseDTO(
                massaDomain.getId(),
                massaDomain.getSabor(),
                massaDomain.getValor()
        );
        return responseDto;
    }

    public List<MassaResponseDTO> toResponse(List<Massa> massasDomain) {
        return massasDomain.stream().map(this::toResponse).toList();
    }
}
