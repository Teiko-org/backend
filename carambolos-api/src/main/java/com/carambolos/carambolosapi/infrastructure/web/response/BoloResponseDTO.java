package com.carambolos.carambolosapi.infrastructure.web.response;

import com.carambolos.carambolosapi.infrastructure.persistence.entity.BoloEntity;
import com.carambolos.carambolosapi.domain.enums.FormatoEnum;
import com.carambolos.carambolosapi.domain.enums.TamanhoEnum;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de resposta contendo informações do bolo")
public record BoloResponseDTO(

        @Schema(description = "Identificador único do bolo", example = "1")
        Integer id,

        @Schema(description = "ID do recheio", example = "1")
        Integer recheioPedidoId,

        @Schema(description = "ID da massa", example = "1")
        Integer massaId,

        @Schema(description = "ID da cobertura", example = "1")
        Integer coberturaId,

        Integer decoracaoId,

        @Schema(description = "Formato do bolo", example = "Circulo")
        FormatoEnum formato,

        @Schema(description = "Tamanho do bolo em cm", example = "12")
        TamanhoEnum tamanho
) {

}
