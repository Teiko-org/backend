package com.carambolos.carambolosapi.controller.response;

import com.carambolos.carambolosapi.model.Decoracao;
import com.carambolos.carambolosapi.model.ImagemDecoracao;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "DTO de resposta com dados da decoração")
public record DecoracaoResponseDTO(
        @Schema(description = "ID da decoração", example = "1")
        Integer id,

        @Schema(description = "Lista de URLs das imagens da decoração")
        List<String> imagens,

        @Schema(description = "Observações", example = "Tema Naruto, tons de laranja e preto")
        String observacao
) {
    public static DecoracaoResponseDTO fromEntity(Decoracao decoracao) {
        List<String> urls = decoracao.getImagens()
                .stream()
                .map(ImagemDecoracao::getUrl)
                .toList();

        return new DecoracaoResponseDTO(
                decoracao.getId(),
                urls,
                decoracao.getObservacao()
        );
    }
}
