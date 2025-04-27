package com.carambolos.carambolosapi.controller.response;

import com.carambolos.carambolosapi.model.RecheioUnitario;

import java.util.List;

public record RecheioUnitarioResponseDTO(
    Integer id,
    String sabor,
    String descricao,
    Double valor
) {
    public static RecheioUnitarioResponseDTO toRecheioUnitarioResponse(RecheioUnitario recheioUnitario) {
        return new RecheioUnitarioResponseDTO(
                recheioUnitario.getId(),
                recheioUnitario.getSabor(),
                recheioUnitario.getDescricao(),
                recheioUnitario.getValor()
        );
    }
    
    public static List<RecheioUnitarioResponseDTO> toRecheioUnitarioResponse(List<RecheioUnitario> recheiosUnitarios) {
        return recheiosUnitarios.stream().map(RecheioUnitarioResponseDTO::toRecheioUnitarioResponse).toList();
    }

}
