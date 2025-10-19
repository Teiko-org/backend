package com.carambolos.carambolosapi.infrastructure.web.request;

import com.carambolos.carambolosapi.infrastructure.persistence.entity.RecheioExclusivoEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO para requisições de criação ou atualização de Recheio Exclusivo")
public record RecheioExclusivoRequestDTO(

        @NotBlank
        @Schema(description = "Nome do recheio exclusivo", example = "Brigadeiro com Morango")
        String nome,

        @Schema(description = "ID do primeiro recheio unitário", example = "1")
        Integer idRecheioUnitario1,

        @Schema(description = "ID do segundo recheio unitário", example = "2")
        Integer idRecheioUnitario2
) {

}
