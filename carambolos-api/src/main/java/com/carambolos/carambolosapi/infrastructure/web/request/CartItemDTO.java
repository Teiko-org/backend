package com.carambolos.carambolosapi.infrastructure.web.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para representar um item do carrinho")
public record CartItemDTO(
        @Schema(description = "ID do produto", example = "1")
        Integer id,

        @Schema(description = "Nome do produto", example = "Croissant")
        String name,

        @Schema(description = "Preço unitário do produto", example = "12.00")
        Double price,

        @Schema(description = "URL da imagem do produto", example = "https://example.com/image.jpg")
        String image,

        @Schema(description = "Tipo do produto", example = "Fornada", allowableValues = "Fornada, Produto, Bolo")
        String type,

        @Schema(description = "ID da fornada da vez (se aplicável)", example = "5")
        Integer fornadaDaVezId,

        @Schema(description = "Quantidade do item no carrinho", example = "2")
        Integer quantity,

        @Schema(description = "Quantidade máxima disponível", example = "10")
        Integer maxQuantity
) {
}