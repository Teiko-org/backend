package com.carambolos.carambolosapi.controller.response;

import com.carambolos.carambolosapi.model.PedidoBolo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoBoloResponseDTO(
        Integer id,
        Integer enderecoId,
        Integer boloId,
        Integer usuarioId,
        String observacao,
        LocalDate dataPrevisaoEntrega,
        LocalDateTime dataUltimaAtualizacao
) {
    public static PedidoBoloResponseDTO toPedidoBoloResponse(PedidoBolo pedido) {
        return new PedidoBoloResponseDTO(
                pedido.getId(),
                pedido.getEnderecoId(),
                pedido.getBoloId(),
                pedido.getUsuarioId(),
                pedido.getObservacao(),
                pedido.getDataPrevisaoEntrega(),
                pedido.getDataUltimaAtualizacao()
        );
    }

    public static List<PedidoBoloResponseDTO> toPedidoBoloResponse(List<PedidoBolo> pedidos) {
        return pedidos.stream().map(PedidoBoloResponseDTO::toPedidoBoloResponse).toList();
    }
}
