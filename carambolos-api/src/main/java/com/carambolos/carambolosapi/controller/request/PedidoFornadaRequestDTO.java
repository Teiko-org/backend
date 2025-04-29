package com.carambolos.carambolosapi.controller.request;

import com.carambolos.carambolosapi.model.Endereco;
import com.carambolos.carambolosapi.model.FornadaDaVez;
import com.carambolos.carambolosapi.model.PedidoFornada;
import com.carambolos.carambolosapi.model.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "DTO para criar um novo pedido de fornada")
public record PedidoFornadaRequestDTO(

        @Schema(description = "ID da fornada da vez associada ao pedido", example = "1")
        Integer fornadaDaVezId,

        @Schema(description = "ID do endereço de entrega", example = "2")
        Integer enderecoId,

        @Schema(description = "ID do usuário responsável pelo pedido", example = "3")
        Integer usuarioId,

        @Schema(description = "Quantidade de produtos no pedido", example = "50")
        Integer quantidade,

        @Schema(description = "Data prevista para entrega do pedido", example = "2025-05-10")
        LocalDate dataPrevisaoEntrega

) {

    public PedidoFornada toEntity(FornadaDaVez fornadaDaVez, Endereco endereco, Usuario usuario) {
        PedidoFornada pedidoFornada = new PedidoFornada();
        pedidoFornada.setFornadaDaVez(fornadaDaVez);
        pedidoFornada.setEndereco(endereco);
        pedidoFornada.setUsuario(usuario);
        pedidoFornada.setQuantidade(quantidade);
        pedidoFornada.setDataPrevisaoEntrega(dataPrevisaoEntrega);
        return pedidoFornada;
    }
}