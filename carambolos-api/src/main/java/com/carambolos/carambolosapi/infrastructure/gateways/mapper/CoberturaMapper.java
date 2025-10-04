package com.carambolos.carambolosapi.infrastructure.gateways.mapper;

import com.carambolos.carambolosapi.domain.entity.Cobertura;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.CoberturaEntity;
import com.carambolos.carambolosapi.infrastructure.web.request.CoberturaRequestDTO;
import com.carambolos.carambolosapi.infrastructure.web.response.CoberturaResponseDTO;

import java.util.List;

public class CoberturaMapper {
    public CoberturaEntity toEntity(Cobertura cobertura) {
        CoberturaEntity entity = new CoberturaEntity();
        entity.setCor(cobertura.getCor());
        entity.setDescricao(cobertura.getDescricao());
        entity.setAtivo(cobertura.getAtivo());

        return entity;
    }

    public Cobertura toDomain(CoberturaEntity entity) {
        Cobertura cobertura = new Cobertura();
        cobertura.setId(entity.getId());
        cobertura.setCor(entity.getCor());
        cobertura.setDescricao(entity.getDescricao());
        cobertura.setAtivo(entity.getAtivo());

        return cobertura;
    }

    public List<Cobertura> toDomain(List<CoberturaEntity> entities) {
        return entities.stream().map(this::toDomain).toList();
    }

    public Cobertura toDomain(CoberturaRequestDTO request) {
        if (request == null) {
            return null;
        }
        Cobertura cobertura = new Cobertura();
        cobertura.setCor(request.cor());
        cobertura.setDescricao(request.descricao());
        return cobertura;
    }

    public CoberturaResponseDTO toResponse(Cobertura cobertura) {
        if (cobertura == null) {
            return null;
        }

        CoberturaResponseDTO response = new CoberturaResponseDTO(
                cobertura.getId(),
                cobertura.getCor(),
                cobertura.getDescricao()
        );

        return response;
    }

    public List<CoberturaResponseDTO> toResponse(List<Cobertura> coberturas) {
        return coberturas.stream().map(this::toResponse).toList();
    }
}
