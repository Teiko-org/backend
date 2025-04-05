package com.carambolos.carambolosapi.entities.response;

import com.carambolos.carambolosapi.model.RecheioUnitario;

import java.util.List;

public record RecheioUnitarioResponse (
    Integer id,
    String sabor,
    String descricao,
    Double valor
) {
    public static RecheioUnitarioResponse toRecheioUnitarioResponse(RecheioUnitario recheioUnitario) {
        return new RecheioUnitarioResponse(
                recheioUnitario.getId(),
                recheioUnitario.getSabor(),
                recheioUnitario.getDescricao(),
                recheioUnitario.getValor()
        );
    }
    
    public static List<RecheioUnitarioResponse> toRecheioUnitarioResponse(List<RecheioUnitario> recheiosUnitarios) {
        return recheiosUnitarios.stream().map(RecheioUnitarioResponse::toRecheioUnitarioResponse).toList();
    }

}
