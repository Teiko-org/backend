package com.carambolos.carambolosapi.entities.response;

import com.carambolos.carambolosapi.entities.RecheioExclusivoProjection;

public record RecheioExclusivoResponse(
        Integer id,
        String nome,
        String sabor1,
        String sabor2

) {
    public static RecheioExclusivoResponse toRecheioExclusivoResponse(RecheioExclusivoProjection projection) {
        return new RecheioExclusivoResponse(
                projection.getId(),
                projection.getNome(),
                projection.getSabor1(),
                projection.getSabor2()
        );
    }

}
