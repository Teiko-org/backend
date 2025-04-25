package com.carambolos.carambolosapi.controller.response;

import com.carambolos.carambolosapi.model.projection.RecheioExclusivoProjection;

import java.util.List;

public record RecheioExclusivoResponseDTO(
        Integer id,
        String nome,
        String sabor1,
        String sabor2

) {
    public static List<RecheioExclusivoResponseDTO> toRecheioExclusivoResponse(List<RecheioExclusivoProjection> projections) {
        return projections.stream().map(RecheioExclusivoResponseDTO::toRecheioExclusivoResponse).toList();
    }
    public static RecheioExclusivoResponseDTO toRecheioExclusivoResponse(RecheioExclusivoProjection projection) {
        return new RecheioExclusivoResponseDTO(
                projection.getId(),
                projection.getNome(),
                projection.getSabor1(),
                projection.getSabor2()
        );
    }

}
