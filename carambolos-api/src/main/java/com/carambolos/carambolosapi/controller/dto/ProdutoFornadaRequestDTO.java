package com.carambolos.carambolosapi.controller.dto;

import com.carambolos.carambolosapi.model.ProdutoFornada;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representação do ProdutoFornada com detalhes completos")
public record ProdutoFornadaRequestDTO(

        @Schema(description = "ID do produto", example = "1")
        Integer id,

        @Schema(description = "Nome do produto", example = "Cupcake de Chocolate")
        String produto,

        @Schema(description = "Descrição do produto", example = "Cupcake de chocolate com cobertura de brigadeiro")
        String descricao,

        @Schema(description = "Preço unitário do produto", example = "7.50")
        Double valor

) {

    public ProdutoFornada toEntity() {
        ProdutoFornada produtoFornada = new ProdutoFornada();
        produtoFornada.setProduto(produto);
        produtoFornada.setDescricao(descricao);
        produtoFornada.setValor(valor);
        return produtoFornada;
    }
}
