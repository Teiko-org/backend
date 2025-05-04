//package com.carambolos.carambolosapi.controller.response;
//
//import com.carambolos.carambolosapi.model.Decoracao;
//import io.swagger.v3.oas.annotations.media.Schema;
//
//@Schema(description = "DTO de resposta para dados da Decoração")
//public record DecoracaoResponseDTO (
//
//        @Schema(description = "Identificador único da decoração", example = "1")
//        Integer id,
//
//        @Schema(description = "Imagem da decoração", example = "imagem.png")
//        byte[] imagemReferencia,
//
//        @Schema(description = "Observação da decoração", example = "Estilo cyberpunk")
//        String estiloDecoracao
//) {
//    public static DecoracaoResponseDTO toDecoracaoResponse(Decoracao decoracao) {
//        return new DecoracaoResponseDTO(
//                decoracao.getId(),
//                decoracao.getImagemReferencia(),
//                decoracao.getEstiloDecoracao()
//        );
//    }
//}
