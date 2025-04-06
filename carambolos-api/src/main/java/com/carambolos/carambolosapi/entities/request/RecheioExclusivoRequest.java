package com.carambolos.carambolosapi.entities.request;

import com.carambolos.carambolosapi.model.RecheioExclusivo;
import jakarta.validation.constraints.NotBlank;

public record RecheioExclusivoRequest(
        @NotBlank
        String nome,
        Integer idRecheioUnitario1,
        Integer idRecheioUnitario2
) {
    public static RecheioExclusivo toRecheioExclusivo(RecheioExclusivoRequest request) {
        RecheioExclusivo recheioExclusivo = new RecheioExclusivo();

        recheioExclusivo.setRecheioUnitarioId1(request.idRecheioUnitario1);
        recheioExclusivo.setRecheioUnitarioId2(request.idRecheioUnitario2);
        recheioExclusivo.setNome(request.nome);

        return recheioExclusivo;
    }
}
