package com.carambolos.carambolosapi.controller.request;

import com.carambolos.carambolosapi.model.Endereco;
import com.carambolos.carambolosapi.model.FornadaDaVez;
import com.carambolos.carambolosapi.model.PedidoFornada;
import com.carambolos.carambolosapi.model.Usuario;
import com.carambolos.carambolosapi.model.enums.TipoEntregaEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Schema(description = "DTO para criar um novo pedido de fornada")
public record PedidoFornadaRequestDTO(

        @Schema(description = "ID da fornada da vez associada ao pedido", example = "1")
        @NotNull
        Integer fornadaDaVezId,

        @Schema(description = "ID do endereço de entrega", example = "2")
        Integer enderecoId,

        @Schema(description = "ID do usuário responsável pelo pedido", example = "3")
        Integer usuarioId,

        @Schema(description = "Quantidade de produtos no pedido", example = "50")
        @NotNull
        Integer quantidade,

        @Schema(description = "Data prevista para entrega do pedido", example = "2025-05-10")
        @NotNull
        LocalDate dataPrevisaoEntrega,

        @Schema(description = "Tipo de entrega", example = "RETIRADA", allowableValues = "RETIRADA, ENTREGA")
        TipoEntregaEnum tipoEntrega,

        @Schema(description = "Nome do cliente", example = "João da Silva")
        @NotBlank
        String nomeCliente,

        @Schema(description = "Telefone do cliente", example = "(11) 91234-5678")
        @NotBlank
        String telefoneCliente

) {

    public PedidoFornada toEntity(PedidoFornadaRequestDTO request) {
        PedidoFornada pedidoFornada = new PedidoFornada();
        pedidoFornada.setFornadaDaVez(request.fornadaDaVezId);
        pedidoFornada.setEndereco(request.enderecoId);
        pedidoFornada.setUsuario(request.usuarioId);
        pedidoFornada.setQuantidade(request.quantidade);
        pedidoFornada.setDataPrevisaoEntrega(request.dataPrevisaoEntrega);
        pedidoFornada.setTipoEntrega(request.tipoEntrega);
        pedidoFornada.setNomeCliente(request.nomeCliente);
        pedidoFornada.setTelefoneCliente(request.telefoneCliente);
        return pedidoFornada;
    }
}