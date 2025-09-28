package com.carambolos.carambolosapi.infrastructure.gateways.mapperEntity;

public final class FornadaEntityMapper {
    private FornadaEntityMapper() {}

    public static com.carambolos.carambolosapi.domain.entity.Fornada toDomain(
            com.carambolos.carambolosapi.infrastructure.persistence.entity.Fornada e) {
        if (e == null) return null;
        var d = new com.carambolos.carambolosapi.domain.entity.Fornada(
                e.getDataInicio(), e.getDataFim(), e.isAtivo()
        );
        d.setId(e.getId());
        return d;
    }

    public static com.carambolos.carambolosapi.infrastructure.persistence.entity.Fornada toEntity(
            com.carambolos.carambolosapi.domain.entity.Fornada d) {
        if (d == null) return null;
        var e = new com.carambolos.carambolosapi.infrastructure.persistence.entity.Fornada();
        e.setId(d.getId());
        e.setDataInicio(d.getDataInicio());
        e.setDataFim(d.getDataFim());
        e.setAtivo(Boolean.TRUE.equals(d.getAtivo())) ;
        return e;
    }
}