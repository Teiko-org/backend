package com.carambolos.carambolosapi.infrastructure.gateways.mapperEntity;

import com.carambolos.carambolosapi.infrastructure.persistence.entity.FornadaDaVez;

public final class FornadaDaVezEntityMapper {
    private FornadaDaVezEntityMapper() {}

    public static FornadaDaVez toDomain(FornadaDaVez e) {
        if (e == null) return null;
        FornadaDaVez d = new FornadaDaVez();
        d.setId(e.getId());
        d.setProdutoFornada(e.getProdutoFornada());
        d.setFornada(e.getFornada());
        d.setQuantidade(e.getQuantidade());
        d.setAtivo(e.isAtivo());
        return d;
    }

    public static FornadaDaVez toEntity(FornadaDaVez d) {
        if (d == null) return null;
        FornadaDaVez e = new FornadaDaVez();
        e.setId(d.getId());
        e.setProdutoFornada(d.getProdutoFornada());
        e.setFornada(d.getFornada());
        e.setQuantidade(d.getQuantidade());
        e.setAtivo(d.isAtivo());
        return e;
    }
}


