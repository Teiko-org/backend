package com.carambolos.carambolosapi.controller.response;

import com.carambolos.carambolosapi.model.enums.FormatoEnum;
import com.carambolos.carambolosapi.model.enums.TamanhoEnum;
import com.carambolos.carambolosapi.model.enums.TipoEntregaEnum;

import java.time.LocalDateTime;

public record DetalhePedidoBoloDTO(
    Integer numeroPedido,
    TamanhoEnum tamanho,
    FormatoEnum formato,
    String massa,
    String recheio,
    String cobertura,
    String decoracao,
    String observacoes,
    TipoEntregaEnum tipoEntrega,
    LocalDateTime data,
    String nomeCliente,
    String telefone,
    EnderecoResponseDTO endereco
) {

    public static DetalhePedidoBoloDTO toDetalhePedidoResponse(
            Integer numeroPedido,
            TamanhoEnum tamanho,
            FormatoEnum formato,
            String massa,
            String recheio,
            String cobertura,
            String decoracao,
            String observacoes,
            TipoEntregaEnum tipoEntrega,
            LocalDateTime data,
            String nomeCliente,
            String telefone,
            EnderecoResponseDTO endereco
    ) {
        return new DetalhePedidoBoloDTO(
                numeroPedido, tamanho, formato, massa, recheio, cobertura, decoracao, observacoes, tipoEntrega, data, nomeCliente, telefone, endereco
        );
    }
}
