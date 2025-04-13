package com.carambolos.carambolosapi.entities.response;

import com.carambolos.carambolosapi.entities.RecheioExclusivoProjection;

import java.util.List;

public record RecheioExclusivoResponse(
        Integer id,
        String nome,
        String sabor1,
        String sabor2

) {
    public static List<RecheioExclusivoResponse> toRecheioExclusivoResponse(List<RecheioExclusivoProjection> projections) {
        return projections.stream().map(RecheioExclusivoResponse::toRecheioExclusivoResponse).toList();
    }
    public static RecheioExclusivoResponse toRecheioExclusivoResponse(RecheioExclusivoProjection projection) {
        return new RecheioExclusivoResponse(
                projection.getId(),
                projection.getNome(),
                projection.getSabor1(),
                projection.getSabor2()
        );
    }

}
