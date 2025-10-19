package com.carambolos.carambolosapi.infrastructure.gateways.mapper;

import com.carambolos.carambolosapi.domain.entity.Bolo;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.BoloEntity;
import com.carambolos.carambolosapi.infrastructure.web.request.BoloRequestDTO;
import com.carambolos.carambolosapi.infrastructure.web.response.BoloResponseDTO;

import java.util.List;

public class BoloMapper {
    public List<Bolo> toDomain(List<BoloEntity> entities) {
        return entities.stream()
                .map(this::toDomain)
                .toList();
    }

    public Bolo toDomain(BoloEntity entity) {
        if (entity == null) {
            return null;
        }
        Bolo bolo = new Bolo();
        bolo.setId(entity.getId());
        bolo.setRecheioPedido(entity.getRecheioPedido());
        bolo.setMassa(entity.getMassa());
        bolo.setCobertura(entity.getCobertura());
        bolo.setDecoracao(entity.getDecoracao());
        bolo.setFormato(entity.getFormato());
        bolo.setTamanho(entity.getTamanho());
        bolo.setCategoria(entity.getCategoria());
        bolo.setAtivo(entity.getAtivo());
        return bolo;
    }

    public BoloEntity toEntity(Bolo bolo) {
        if (bolo == null) {
            return null;
        }
        BoloEntity entity = new BoloEntity();
        entity.setId(bolo.getId());
        entity.setRecheioPedido(bolo.getRecheioPedido());
        entity.setMassa(bolo.getMassa());
        entity.setCobertura(bolo.getCobertura());
        entity.setDecoracao(bolo.getDecoracao());
        entity.setFormato(bolo.getFormato());
        entity.setTamanho(bolo.getTamanho());
        entity.setCategoria(bolo.getCategoria());
        entity.setAtivo(bolo.getAtivo());
        return entity;
    }

    public List<BoloResponseDTO> toBoloResponse(List<Bolo> bolos) {
        return bolos.stream()
                .map(this::toBoloResponse)
                .toList();
    }

    public  BoloResponseDTO toBoloResponse(Bolo bolo) {
        return new BoloResponseDTO(
                bolo.getId(),
                bolo.getRecheioPedido(),
                bolo.getMassa(),
                bolo.getCobertura(),
                bolo.getDecoracao(),
                bolo.getFormato(),
                bolo.getTamanho()
        );
    }

    public static Bolo toBolo(BoloRequestDTO request) {
        if (request == null) {
            return null;
        }
        Bolo bolo = new Bolo();
        bolo.setRecheioPedido(request.recheioPedidoId());
        bolo.setMassa(request.massaId());
        bolo.setCobertura(request.coberturaId());
        bolo.setDecoracao(request.decoracaoId());
        bolo.setFormato(request.formato());
        bolo.setTamanho(request.tamanho());
        bolo.setCategoria(request.categoria());
        return bolo;
    }
}
