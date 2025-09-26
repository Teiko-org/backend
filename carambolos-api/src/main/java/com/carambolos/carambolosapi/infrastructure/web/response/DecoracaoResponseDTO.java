package com.carambolos.carambolosapi.infrastructure.web.response;

import com.carambolos.carambolosapi.domain.entity.Decoracao;
import com.carambolos.carambolosapi.domain.entity.ImagemDecoracao;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "DTO de resposta com dados da decoração")
public record DecoracaoResponseDTO(
        @Schema(description = "ID da decoração", example = "1")
        Integer id,

        @Schema(description = "Lista de URLs das imagens da decoração")
        List<String> imagens,

        @Schema(description = "Observações", example = "Tema Naruto, tons de laranja e preto")
        String observacao,

        @Schema(description = "Nome do tipo da decoração", example = "Bolo de natal")
        String nome,

        @Schema(description = "Categoria para exibição (pré-decoração)", example = "Vintage")
        String categoria
) {
    public static DecoracaoResponseDTO fromEntity(Decoracao decoracao) {
        List<String> urls = decoracao.getImagens()
                .stream()
                .map(ImagemDecoracao::getUrl)
                .toList();

        return new DecoracaoResponseDTO(
                decoracao.getId(),
                urls,
                decoracao.getObservacao(),
                decoracao.getNome(),
                decoracao.getCategoria()
        );
    }
}
