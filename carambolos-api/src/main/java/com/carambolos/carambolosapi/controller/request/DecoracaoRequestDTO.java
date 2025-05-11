//package com.carambolos.carambolosapi.controller.request;
//
//import com.carambolos.carambolosapi.model.Decoracao;
//import io.swagger.v3.oas.annotations.media.Schema;
//
//@Schema(description = "DTO para requisições de criação ou atualização de Decoração")
//public record DecoracaoRequestDTO(
//        @Schema(description = "Imagem da decoração", example = "imagem.png")
//        byte[] imagemReferencia,
//
//        @Schema(description = "Observação da decoração", example = "Estilo cyberpunk")
//        String estiloDecoracao
//) {
//        public static Decoracao toDecoracao(DecoracaoRequestDTO request) {
//                Decoracao decoracao = new Decoracao();
//                decoracao.setImagemReferencia(request.imagemReferencia);
//                decoracao.setEstiloDecoracao(request.estiloDecoracao);
//                return decoracao;
//        }
//}
