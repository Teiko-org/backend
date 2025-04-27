package com.carambolos.carambolosapi.controller.request;

import com.carambolos.carambolosapi.model.FornadaDaVez;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para atualizar a quantidade de uma fornada da vez")
public record FornadaDaVezUpdateRequestDTO(

        @Schema(description = "Quantidade de unidades da fornada da vez", example = "100")
        Integer quantidade

) {

    public FornadaDaVez toEntity() {
        FornadaDaVez fornadaDaVez = new FornadaDaVez();
        fornadaDaVez.setQuantidade(quantidade);
        return fornadaDaVez;
    }
}
