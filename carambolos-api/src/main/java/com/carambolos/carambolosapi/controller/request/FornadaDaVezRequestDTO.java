package com.carambolos.carambolosapi.controller.request;

import com.carambolos.carambolosapi.model.Fornada;
import com.carambolos.carambolosapi.model.ProdutoFornada;
import com.carambolos.carambolosapi.model.FornadaDaVez;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para criar uma fornada da vez, associando produto e fornada")
public record FornadaDaVezRequestDTO(

        @Schema(description = "ID do produto da fornada", example = "1")
        Integer produtoFornadaId,

        @Schema(description = "ID da fornada", example = "1")
        Integer fornadaId,

        @Schema(description = "Quantidade de unidades da fornada do produto", example = "50")
        Integer quantidade

) {
    @Schema(description = "Método para converter o DTO em uma entidade FornadaDaVez")
    public FornadaDaVez toEntity(Fornada fornada, ProdutoFornada produtoFornada) {
        FornadaDaVez fornadaDaVez = new FornadaDaVez();
        fornadaDaVez.setFornada(fornada);
        fornadaDaVez.setProdutoFornada(produtoFornada);
        fornadaDaVez.setQuantidade(quantidade);
        return fornadaDaVez;
    }
}
