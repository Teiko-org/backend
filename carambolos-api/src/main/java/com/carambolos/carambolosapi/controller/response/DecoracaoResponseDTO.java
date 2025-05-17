package com.carambolos.carambolosapi.controller.response;

import com.carambolos.carambolosapi.model.Decoracao;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de resposta com dados da decoração")
public record DecoracaoResponseDTO(
        @Schema(description = "ID da decoração", example = "1")
        Integer id,

        @Schema(description = "URL da imagem de referência", example = "https://azure.com/decoracoes/tema-x.jpg")
        String imagemReferencia,

        @Schema(description = "Observações", example = "Tema Naruto, tons de laranja e preto")
        String observacao
) {
    public static DecoracaoResponseDTO fromEntity(Decoracao decoracao) {
        return new DecoracaoResponseDTO(
                decoracao.getId(),
                decoracao.getImagemReferencia(),
                decoracao.getObservacao()
        );
    }
}
