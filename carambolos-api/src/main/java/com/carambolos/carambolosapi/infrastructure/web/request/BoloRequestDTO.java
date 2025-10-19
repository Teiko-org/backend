package com.carambolos.carambolosapi.infrastructure.web.request;

import com.carambolos.carambolosapi.infrastructure.persistence.entity.BoloEntity;
import com.carambolos.carambolosapi.domain.enums.FormatoEnum;
import com.carambolos.carambolosapi.domain.enums.TamanhoEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO para requisição de cadastro de bolo.")
public record BoloRequestDTO(
        @Schema(description = "ID do recheio", example = "1")
        @NotNull
        Integer recheioPedidoId,

        @Schema(description = "ID da massa", example = "1")
        @NotNull
        Integer massaId,

        @Schema(description = "ID da cobertura", example = "1")
        @NotNull
        Integer coberturaId,

        @Schema(description = "ID da decoração", example = "1")
        Integer decoracaoId,

        @Schema(description = "Formato do bolo", example = "CIRCULO")
        @NotNull
        FormatoEnum formato,

        @Schema(description = "Tamanho do bolo em cm", example = "TAMANHO_12")
        @NotNull
        TamanhoEnum tamanho,

        @Schema(description = "Categoria do bolo", example = "bolo de páscoa")
        String categoria
) {

}
