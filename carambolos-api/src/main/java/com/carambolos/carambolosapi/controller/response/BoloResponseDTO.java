package com.carambolos.carambolosapi.controller.response;

import com.carambolos.carambolosapi.model.Bolo;
import com.carambolos.carambolosapi.model.enums.FormatoEnum;
import com.carambolos.carambolosapi.model.enums.TamanhoEnum;
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

//        Integer decoracaoId,

        @Schema(description = "Formato do bolo", example = "Circulo")
        FormatoEnum formato,

        @Schema(description = "Tamanho do bolo em cm", example = "12")
        TamanhoEnum tamanho
) {
    @Schema(description = "Método para converter a entidade Bolo em um DTO de resposta")
    public static BoloResponseDTO toBoloResponse(Bolo bolo) {
        return new BoloResponseDTO(
                bolo.getId(),
                bolo.getRecheioPedido(),
                bolo.getMassa(),
                bolo.getCobertura(),
                bolo.getFormato(),
                bolo.getTamanho()
        );
    }
}
