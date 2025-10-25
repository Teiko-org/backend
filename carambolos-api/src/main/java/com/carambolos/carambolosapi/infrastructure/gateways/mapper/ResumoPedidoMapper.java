package com.carambolos.carambolosapi.infrastructure.gateways.mapper;

import com.carambolos.carambolosapi.domain.entity.ResumoPedido;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.ResumoPedidoEntity;
import com.carambolos.carambolosapi.infrastructure.web.request.ResumoPedidoRequestDTO;
import com.carambolos.carambolosapi.infrastructure.web.response.ResumoPedidoResponseDTO;

import java.util.List;

public class ResumoPedidoMapper {

    public List<ResumoPedido> toDomain(List<ResumoPedidoEntity> entities) {
        return entities.stream()
                .map(this::toDomain)
                .toList();
    }

    public ResumoPedido toDomain(ResumoPedidoEntity entity) {
        return new ResumoPedido(
                entity.getId(),
                entity.getStatus(),
                entity.getValor(),
                entity.getDataPedido(),
                entity.getDataEntrega(),
                entity.getPedidoFornadaId(),
                entity.getPedidoBoloId(),
                entity.getAtivo()
        );
    }

    public ResumoPedidoEntity toEntity(ResumoPedido resumoPedido) {
        return new ResumoPedidoEntity(
                resumoPedido.getId(),
                resumoPedido.getStatus(),
                resumoPedido.getValor(),
                resumoPedido.getDataPedido(),
                resumoPedido.getDataEntrega(),
                resumoPedido.getPedidoFornadaId(),
                resumoPedido.getPedidoBoloId(),
                resumoPedido.getAtivo()
        );
    }

    public ResumoPedidoResponseDTO toResumoPedidoResponse(ResumoPedido pedido) {
        return new ResumoPedidoResponseDTO(
                pedido.getId(),
                pedido.getStatus(),
                pedido.getValor(),
                pedido.getDataPedido(),
                pedido.getDataEntrega(),
                pedido.getPedidoFornadaId(),
                pedido.getPedidoBoloId()
        );
    }

    public List<ResumoPedidoResponseDTO> toResumoPedidoResponse(List<ResumoPedido> resumoPedidoEntities) {
        return resumoPedidoEntities.stream()
                .map(this::toResumoPedidoResponse)
                .toList();
    }

    public ResumoPedido toResumoPedido(ResumoPedidoRequestDTO request) {
        if (request == null) {
            return null;
        }

        ResumoPedido resumoPedido = new ResumoPedido();
        resumoPedido.setDataEntrega(request.dataEntrega());
        resumoPedido.setPedidoFornadaId(request.pedidoFornadaId());
        resumoPedido.setPedidoBoloId(request.pedidoBoloId());

        return resumoPedido;
    }
}
