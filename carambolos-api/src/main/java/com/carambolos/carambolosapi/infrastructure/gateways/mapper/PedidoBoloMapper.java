package com.carambolos.carambolosapi.infrastructure.gateways.mapper;

import com.carambolos.carambolosapi.domain.entity.PedidoBolo;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.PedidoBoloEntity;
import com.carambolos.carambolosapi.infrastructure.web.request.PedidoBoloRequestDTO;
import com.carambolos.carambolosapi.infrastructure.web.response.PedidoBoloResponseDTO;

import java.util.List;

public class PedidoBoloMapper {
    public List<PedidoBolo> toDomain(List<com.carambolos.carambolosapi.infrastructure.persistence.entity.PedidoBoloEntity> entities) {
        return entities.stream()
                .map(this::toDomain)
                .toList();
    }

    public PedidoBolo toDomain(com.carambolos.carambolosapi.infrastructure.persistence.entity.PedidoBoloEntity entity) {
        if (entity == null) {
            return null;
        }

        return new PedidoBolo(
                entity.getId(),
                entity.getEnderecoId(),
                entity.getBoloId(),
                entity.getUsuarioId(),
                entity.getObservacao(),
                entity.getDataPrevisaoEntrega(),
                entity.getDataUltimaAtualizacao(),
                entity.getTipoEntrega(),
                entity.getNomeCliente(),
                entity.getTelefoneCliente(),
                entity.getHorarioRetirada(),
                entity.getAtivo()
        );
    }

    public PedidoBoloEntity toEntity(PedidoBolo pedidoBolo) {
        if (pedidoBolo == null) {
            return null;
        }

        PedidoBoloEntity entity = new PedidoBoloEntity();
        entity.setId(pedidoBolo.getId());
        entity.setEnderecoId(pedidoBolo.getEnderecoId());
        entity.setBoloId(pedidoBolo.getBoloId());
        entity.setUsuarioId(pedidoBolo.getUsuarioId());
        entity.setObservacao(pedidoBolo.getObservacao());
        entity.setDataPrevisaoEntrega(pedidoBolo.getDataPrevisaoEntrega());
        entity.setDataUltimaAtualizacao(pedidoBolo.getDataUltimaAtualizacao());
        entity.setTipoEntrega(pedidoBolo.getTipoEntrega());
        entity.setNomeCliente(pedidoBolo.getNomeCliente());
        entity.setTelefoneCliente(pedidoBolo.getTelefoneCliente());
        entity.setHorarioRetirada(pedidoBolo.getHorarioRetirada());
        entity.setAtivo(pedidoBolo.getAtivo());
        return entity;
    }

    public PedidoBoloResponseDTO toPedidoBoloResponse(PedidoBolo pedido) {
        return new PedidoBoloResponseDTO(
                pedido.getId(),
                pedido.getEnderecoId(),
                pedido.getBoloId(),
                pedido.getUsuarioId(),
                pedido.getObservacao(),
                pedido.getDataPrevisaoEntrega(),
                pedido.getDataUltimaAtualizacao(),
                pedido.getTipoEntrega(),
                pedido.getNomeCliente(),
                pedido.getTelefoneCliente()
        );
    }

    public List<PedidoBoloResponseDTO> toPedidoBoloResponse(List<PedidoBolo> pedidos) {
        return pedidos.stream().map(this::toPedidoBoloResponse).toList();
    }

    public static PedidoBolo toPedidoBolo(PedidoBoloRequestDTO request) {
        if (request == null) {
            return null;
        }
        PedidoBolo pedidoBoloEntity = new PedidoBolo();
        pedidoBoloEntity.setEnderecoId(request.enderecoId());
        pedidoBoloEntity.setBoloId(request.boloId());
        pedidoBoloEntity.setUsuarioId(request.usuarioId());
        pedidoBoloEntity.setObservacao(request.observacao());
        pedidoBoloEntity.setDataPrevisaoEntrega(request.dataPrevisaoEntrega());
        pedidoBoloEntity.setDataUltimaAtualizacao(request.dataUltimaAtualizacao());
        pedidoBoloEntity.setTipoEntrega(request.tipoEntrega());
        pedidoBoloEntity.setNomeCliente(request.nomeCliente());
        pedidoBoloEntity.setTelefoneCliente(request.telefoneCliente());
        pedidoBoloEntity.setHorarioRetirada(request.horarioRetirada());
        return pedidoBoloEntity;
    }
}
