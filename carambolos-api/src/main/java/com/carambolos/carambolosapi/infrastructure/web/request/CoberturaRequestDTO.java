package com.carambolos.carambolosapi.infrastructure.web.request;

import com.carambolos.carambolosapi.domain.entity.Cobertura;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para requisições de criação ou atualização de Cobertura")
public record CoberturaRequestDTO(

        @Schema(description = "ID da cobertura", example = "1")
        Integer id,

        @Schema(description = "Cor da cobertura", example = "Marrom")
        String cor,

        @Schema(description = "Descrição da cobertura", example = "Cobertura de chocolate ao leite")
        String descricao
) {
    public static Cobertura toCobertura(CoberturaRequestDTO request) {
        if (request == null) {
            return null;
        }
        Cobertura cobertura = new Cobertura();
        cobertura.setCor(request.cor);
        cobertura.setDescricao(request.descricao);
        return cobertura;
    }
}
