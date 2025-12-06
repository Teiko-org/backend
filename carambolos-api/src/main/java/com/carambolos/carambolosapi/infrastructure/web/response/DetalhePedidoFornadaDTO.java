package com.carambolos.carambolosapi.infrastructure.web.response;

import com.carambolos.carambolosapi.domain.enums.TipoEntregaEnum;

import java.io.Serializable;
import java.time.LocalDateTime;

public record DetalhePedidoFornadaDTO(
        Integer quantidade,
        String produtoFornada,
        TipoEntregaEnum tipoEntrega,
        String nomeCliente,
        String telefoneCliente,
        String observacao,
        LocalDateTime dataPedido,
        EnderecoResponseDTO endereco
) implements Serializable {
    public static DetalhePedidoFornadaDTO toDetalhePedidoResponse(
            Integer quantidade,
            String produtoFornada,
            TipoEntregaEnum tipoEntrega,
            String nomeCliente,
            String telefoneCliente,
            String observacao,
            LocalDateTime dataPedido,
            EnderecoResponseDTO endereco
    ) {
        return new DetalhePedidoFornadaDTO(
                quantidade, produtoFornada, tipoEntrega, nomeCliente, telefoneCliente, observacao, dataPedido, endereco
        );
    }


}
