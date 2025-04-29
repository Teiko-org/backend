package com.carambolos.carambolosapi.controller.dto;

import com.carambolos.carambolosapi.model.RecheioExclusivo;
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
    public static RecheioExclusivo toRecheioExclusivo(RecheioExclusivoRequestDTO request) {
        RecheioExclusivo recheioExclusivo = new RecheioExclusivo();
        recheioExclusivo.setRecheioUnitarioId1(request.idRecheioUnitario1);
        recheioExclusivo.setRecheioUnitarioId2(request.idRecheioUnitario2);
        recheioExclusivo.setNome(request.nome);
        return recheioExclusivo;
    }
}
