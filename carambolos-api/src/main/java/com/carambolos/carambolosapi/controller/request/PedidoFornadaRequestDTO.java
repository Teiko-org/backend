package com.carambolos.carambolosapi.controller.request;

import com.carambolos.carambolosapi.model.Endereco;
import com.carambolos.carambolosapi.model.FornadaDaVez;
import com.carambolos.carambolosapi.model.PedidoFornada;
import com.carambolos.carambolosapi.model.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Schema(description = "DTO para criar um novo pedido de fornada")
public record PedidoFornadaRequestDTO(

        @Schema(description = "ID da fornada da vez associada ao pedido", example = "1")
        @NotNull
        Integer fornadaDaVezId,

        @Schema(description = "ID do endereço de entrega", example = "2")
        @NotNull
        Integer enderecoId,

        @Schema(description = "ID do usuário responsável pelo pedido", example = "3")
        Integer usuarioId,

        @Schema(description = "Quantidade de produtos no pedido", example = "50")
        @NotNull
        Integer quantidade,

        @Schema(description = "Data prevista para entrega do pedido", example = "2025-05-10")
        @NotNull
        LocalDate dataPrevisaoEntrega

) {

    public PedidoFornada toEntity(PedidoFornadaRequestDTO request) {
        PedidoFornada pedidoFornada = new PedidoFornada();
        pedidoFornada.setFornadaDaVez(request.fornadaDaVezId);
        pedidoFornada.setEndereco(request.enderecoId);
        pedidoFornada.setUsuario(request.usuarioId);
        pedidoFornada.setQuantidade(request.quantidade);
        pedidoFornada.setDataPrevisaoEntrega(request.dataPrevisaoEntrega);
        return pedidoFornada;
    }
}