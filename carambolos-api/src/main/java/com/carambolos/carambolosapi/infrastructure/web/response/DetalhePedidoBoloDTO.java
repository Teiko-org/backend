package com.carambolos.carambolosapi.infrastructure.web.response;

import com.carambolos.carambolosapi.domain.enums.FormatoEnum;
import com.carambolos.carambolosapi.domain.enums.TamanhoEnum;
import com.carambolos.carambolosapi.domain.enums.TipoEntregaEnum;

import java.io.Serializable;
import java.time.LocalDateTime;

public record DetalhePedidoBoloDTO(
    Integer numeroPedido,
    TamanhoEnum tamanho,
    FormatoEnum formato,
    String massa,
    String recheio,
    String cobertura,
    String decoracao,
    String[] imagensDecoracao,
    String adicionais,
    String observacoes,
    TipoEntregaEnum tipoEntrega,
    LocalDateTime data,
    String nomeCliente,
    String telefone,
    String horarioRetirada,
    EnderecoResponseDTO endereco
) implements Serializable {

    public static DetalhePedidoBoloDTO toDetalhePedidoResponse(
            Integer numeroPedido,
            TamanhoEnum tamanho,
            FormatoEnum formato,
            String massa,
            String recheio,
            String cobertura,
            String decoracao,
            String[] imagensDecoracao,
            String adicionais,
            String observacoes,
            TipoEntregaEnum tipoEntrega,
            LocalDateTime data,
            String nomeCliente,
            String telefone,
            String horarioRetirada,
            EnderecoResponseDTO endereco
    ) {
        return new DetalhePedidoBoloDTO(
                numeroPedido, tamanho, formato, massa, recheio, cobertura, decoracao, imagensDecoracao, adicionais, observacoes, tipoEntrega, data, nomeCliente, telefone, horarioRetirada, endereco
        );
    }
}
