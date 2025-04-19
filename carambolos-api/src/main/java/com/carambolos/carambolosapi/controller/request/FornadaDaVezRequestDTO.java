package com.carambolos.carambolosapi.controller.request;

import com.carambolos.carambolosapi.model.Fornada;
import com.carambolos.carambolosapi.model.FornadaDaVez;
import com.carambolos.carambolosapi.model.ProdutoFornada;

public record FornadaDaVezRequestDTO(
        Integer produtoFornadaId,
        Integer fornadaId,
        Integer quantidade
) {
    public FornadaDaVez toEntity(Fornada fornada, ProdutoFornada produtoFornada) {
        FornadaDaVez fornadaDaVez = new FornadaDaVez();
        fornadaDaVez.setFornada(fornada);
        fornadaDaVez.setProdutoFornada(produtoFornada);
        fornadaDaVez.setQuantidade(quantidade);
        return fornadaDaVez;
    }
}
