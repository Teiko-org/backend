package com.carambolos.carambolosapi.infrastructure.web.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "DTO para salvar/atualizar o carrinho de um usuário")
public record CarrinhoRequestDTO(
        @Schema(description = "Lista de itens do carrinho")
        @NotNull
        @Valid
        List<CartItemDTO> itens
) {
}