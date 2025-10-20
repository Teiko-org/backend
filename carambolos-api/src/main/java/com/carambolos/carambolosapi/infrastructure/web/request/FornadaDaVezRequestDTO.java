package com.carambolos.carambolosapi.infrastructure.web.request;

import com.carambolos.carambolosapi.infrastructure.persistence.entity.FornadaDaVez;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO para criar uma fornada da vez, associando produto e fornada")
public record FornadaDaVezRequestDTO(

        @NotNull
        @Schema(description = "ID do produto da fornada", example = "1")
        Integer produtoFornadaId,

        @NotNull
        @Schema(description = "ID da fornada", example = "1")
        Integer fornadaId,

        @NotNull
        @Min(1)
        @Schema(description = "Quantidade de unidades da fornada do produto", example = "50")
        Integer quantidade

) {
    @Schema(description = "Método para converter o DTO em uma entidade FornadaDaVez")
    public FornadaDaVez toEntity(FornadaDaVezRequestDTO request) {
        FornadaDaVez fornadaDaVez = new FornadaDaVez();
        fornadaDaVez.setFornada(request.fornadaId);
        fornadaDaVez.setProdutoFornada(request.produtoFornadaId);
        fornadaDaVez.setQuantidade(quantidade);
        return fornadaDaVez;
    }
}
