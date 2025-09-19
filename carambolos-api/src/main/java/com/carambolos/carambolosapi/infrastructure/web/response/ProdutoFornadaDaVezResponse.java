package com.carambolos.carambolosapi.infrastructure.web.response;

import com.carambolos.carambolosapi.domain.projection.ProdutoFornadaDaVezProjection;

import java.time.LocalDate;
import java.util.List;

public record ProdutoFornadaDaVezResponse(
        Integer fornadaDaVezId,
        Integer id,
        String produto,
        String descricao,
        Double valor,
        String categoria,
        Integer quantidade,
        Integer quantidadeVendida,
        Boolean isAtivo,
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
                projection.getQuantidadeVendida(),
                projection.getAtivoPf(),
                projection.getDataInicio(),
                projection.getDataFim()
        );
    }
}
