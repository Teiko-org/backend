package com.carambolos.carambolosapi.controller.response;

import com.carambolos.carambolosapi.model.enums.TipoEntregaEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DetalhePedidoFornadaDTO(
        Integer quantidade,
        String produtoFornada,
        TipoEntregaEnum tipoEntrega,
        String nomeCliente,
        String telefoneCliente,
//        String observacao,
        LocalDateTime dataPedido,
        EnderecoResponseDTO endereco
) {
    public static DetalhePedidoFornadaDTO toDetalhePedidoResponse(
            Integer quantidade,
            String produtoFornada,
            TipoEntregaEnum tipoEntrega,
            String nomeCliente,
            String telefoneCliente,
//            String observacao,
            LocalDateTime dataPedido,
            EnderecoResponseDTO endereco
    ) {
        return new DetalhePedidoFornadaDTO(
                quantidade, produtoFornada, tipoEntrega, nomeCliente, telefoneCliente, dataPedido, endereco
        );
    }


}
