package com.carambolos.carambolosapi.infrastructure.web.response;

import com.carambolos.carambolosapi.domain.entity.ProdutoFornada;
import com.carambolos.carambolosapi.domain.entity.ImagemProdutoFornada;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "DTO de resposta com dados do produto da fornada")
public record ProdutoFornadaResponseDTO(
        @Schema(description = "ID do produto da fornada", example = "1")
        Integer id,

        @Schema(description = "Nome do produto", example = "Cupcake de Chocolate")
        String produto,

        @Schema(description = "Descrição do produto", example = "Cupcake artesanal de chocolate belga")
        String descricao,

        @Schema(description = "Preço unitário do produto", example = "7.50")
        Double valor,

        @Schema(description = "Categoria da fornada", example = "Fornada natalina")
        String categoria,

        @Schema(description = "Lista de URLs das imagens do produto da fornada", example = "[\"https://storage.com/imagem1.jpg\", \"https://storage.com/imagem2.jpg\"]")
        List<String> imagens
) {
    public static ProdutoFornadaResponseDTO fromEntity(ProdutoFornada produtoFornada) {
        List<String> urls = produtoFornada.getImagens() != null
                ? produtoFornada.getImagens().stream().map(ImagemProdutoFornada::getUrl).toList()
                : List.of();

        return new ProdutoFornadaResponseDTO(
                produtoFornada.getId(),
                produtoFornada.getProduto(),
                produtoFornada.getDescricao(),
                produtoFornada.getValor(),
                produtoFornada.getCategoria(),
                urls
        );
    }
}