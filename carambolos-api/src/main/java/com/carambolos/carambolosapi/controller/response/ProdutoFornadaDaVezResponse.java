package com.carambolos.carambolosapi.controller.response;

import com.carambolos.carambolosapi.model.projection.ProdutoFornadaDaVezProjection;

import java.time.LocalDate;
import java.util.List;

public record ProdutoFornadaDaVezResponse(
        Integer fornadaDaVezId,
        Integer produtoFornadaId,
        String produto,
        String descricao,
        Double valor,
        String categoria,
        Integer quantidade,
        Boolean isAtivoPf,
        Boolean isAtivoFdv,
        LocalDate dataInicio,
        LocalDate dataFim
) {
    public static List<ProdutoFornadaDaVezResponse> toProdutoFornadaDaVezResonse(List<ProdutoFornadaDaVezProjection> projections) {
        return projections.stream().map(ProdutoFornadaDaVezResponse::toProdutoFornadaDaVezResonse).toList();
    }
    public static ProdutoFornadaDaVezResponse toProdutoFornadaDaVezResonse(ProdutoFornadaDaVezProjection projection) {
        return new ProdutoFornadaDaVezResponse(
                projection.getFornadaDaVezId(),
                projection.getProdutoFornadaId(),
                projection.getProduto(),
                projection.getDescricao(),
                projection.getValor(),
                projection.getCategoria(),
                projection.getQuantidade(),
                projection.getAtivoPf(),
                projection.getAtivoFdv(),
                projection.getDataInicio(),
                projection.getDataFim()
        );
    }
}
