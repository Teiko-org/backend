package com.carambolos.carambolosapi.infrastructure.web.response;

import com.carambolos.carambolosapi.domain.entity.PedidoBolo;
import com.carambolos.carambolosapi.domain.enums.TipoEntregaEnum;

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
        LocalDateTime dataUltimaAtualizacao,
        TipoEntregaEnum tipoEntrega,
        String nomeCliente,
        String telefoneCliente
) {
    public static PedidoBoloResponseDTO toPedidoBoloResponse(PedidoBolo pedido) {
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

    public static List<PedidoBoloResponseDTO> toPedidoBoloResponse(List<PedidoBolo> pedidos) {
        return pedidos.stream().map(PedidoBoloResponseDTO::toPedidoBoloResponse).toList();
    }
}
