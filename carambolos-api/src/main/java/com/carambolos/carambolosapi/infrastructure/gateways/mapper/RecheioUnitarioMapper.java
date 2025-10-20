package com.carambolos.carambolosapi.infrastructure.gateways.mapper;

import com.carambolos.carambolosapi.domain.entity.RecheioUnitario;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.RecheioUnitarioEntity;
import com.carambolos.carambolosapi.infrastructure.web.request.RecheioUnitarioRequestDTO;
import com.carambolos.carambolosapi.infrastructure.web.response.RecheioUnitarioResponseDTO;

import java.util.List;

public class RecheioUnitarioMapper {
    public RecheioUnitarioEntity toEntity(RecheioUnitario recheioUnitario) {
        if (recheioUnitario == null) {
            return null;
        }

        RecheioUnitarioEntity entity = new RecheioUnitarioEntity();
        entity.setId(recheioUnitario.getId());
        entity.setSabor(recheioUnitario.getSabor());
        entity.setDescricao(recheioUnitario.getDescricao());
        entity.setValor(recheioUnitario.getValor());
        entity.setAtivo(recheioUnitario.getAtivo());
        return entity;
    }

    public RecheioUnitario toDomain(RecheioUnitarioEntity entity) {
        if (entity == null) {
            return null;
        }
        return new RecheioUnitario(
                entity.getId(),
                entity.getSabor(),
                entity.getDescricao(),
                entity.getValor(),
                entity.getAtivo()
        );
    }

    public List<RecheioUnitario> toDomain(List<RecheioUnitarioEntity> entities) {
        return entities.stream().map(this::toDomain).toList();
    }

    public RecheioUnitario toDomain(RecheioUnitarioRequestDTO requestDto) {
        if (requestDto == null) {
            return null;
        }
        return new RecheioUnitario(
                requestDto.sabor(),
                requestDto.descricao(),
                requestDto.valor()
        );
    }

    public RecheioUnitarioResponseDTO toResponse(RecheioUnitario recheioUnitario) {
        if (recheioUnitario == null) {
            return null;
        }
        return new RecheioUnitarioResponseDTO(
                recheioUnitario.getId(),
                recheioUnitario.getSabor(),
                recheioUnitario.getDescricao(),
                recheioUnitario.getValor()
        );
    }

    public List<RecheioUnitarioResponseDTO> toResponse(List<RecheioUnitario> recheios) {
        return recheios.stream().map(this::toResponse).toList();
    }
}
