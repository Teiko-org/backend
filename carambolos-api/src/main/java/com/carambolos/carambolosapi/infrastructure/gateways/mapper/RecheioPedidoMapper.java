package com.carambolos.carambolosapi.infrastructure.gateways.mapper;

import com.carambolos.carambolosapi.domain.entity.RecheioPedido;
import com.carambolos.carambolosapi.domain.projection.RecheioPedidoProjection;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.RecheioPedidoEntity;
import com.carambolos.carambolosapi.infrastructure.web.request.RecheioPedidoRequestDTO;
import com.carambolos.carambolosapi.infrastructure.web.response.RecheioPedidoResponseDTO;

import java.util.List;

public class RecheioPedidoMapper {
    public RecheioPedidoEntity toEntity(RecheioPedido recheioPedido) {
        return new RecheioPedidoEntity(
                recheioPedido.getId(),
                recheioPedido.getRecheioUnitarioId1(),
                recheioPedido.getRecheioUnitarioId2(),
                recheioPedido.getRecheioExclusivo(),
                recheioPedido.getAtivo()
        );
    }

    public RecheioPedido toDomain(RecheioPedidoEntity entity) {
        return new RecheioPedido(
                entity.getId(),
                entity.getRecheioUnitarioId1(),
                entity.getRecheioUnitarioId2(),
                entity.getRecheioExclusivo(),
                entity.getAtivo()
        );
    }

    public RecheioPedido toRecheioPedido(RecheioPedidoRequestDTO request) {
        if(request == null) {
            return null;
        }

        RecheioPedido recheioPedido = new RecheioPedido();
        recheioPedido.setRecheioExclusivo(request.idExclusivo());
        recheioPedido.setRecheioUnitarioId1(request.idUnitario1());
        recheioPedido.setRecheioUnitarioId2(request.idUnitario2());

        return recheioPedido;
    }

    public List<RecheioPedidoResponseDTO> toResponse(List<RecheioPedidoProjection> projections) {
        return projections.stream().map(this::toResponse).toList();
    }

    public RecheioPedidoResponseDTO toResponse(RecheioPedidoProjection projection) {
        return new RecheioPedidoResponseDTO(
                projection.getId(),
                projection.getSabor1(),
                projection.getSabor2(),
                projection.getValor()
        );
    }
}
