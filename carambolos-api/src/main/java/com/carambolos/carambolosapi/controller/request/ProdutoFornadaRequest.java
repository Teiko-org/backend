package com.carambolos.carambolosapi.controller.request;

import com.carambolos.carambolosapi.model.ProdutoFornada;

public record ProdutoFornadaRequest(
        String produto,
        String descricao,
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
