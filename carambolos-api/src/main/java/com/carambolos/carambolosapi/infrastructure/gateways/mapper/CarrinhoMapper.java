package com.carambolos.carambolosapi.infrastructure.gateways.mapper;

public final class CarrinhoMapper {
    private CarrinhoMapper() {}

    public static com.carambolos.carambolosapi.domain.entity.Carrinho toDomain(
            com.carambolos.carambolosapi.infrastructure.persistence.entity.CarrinhoEntity e) {
        if (e == null) return null;
        var d = new com.carambolos.carambolosapi.domain.entity.Carrinho();
        d.setId(e.getId());
        d.setUsuarioId(e.getUsuarioId());
        d.setItens(e.getItens());
        d.setDataUltimaAtualizacao(e.getDataUltimaAtualizacao());
        return d;
    }

    public static com.carambolos.carambolosapi.infrastructure.persistence.entity.CarrinhoEntity toEntity(
            com.carambolos.carambolosapi.domain.entity.Carrinho d) {
        if (d == null) return null;
        var e = new com.carambolos.carambolosapi.infrastructure.persistence.entity.CarrinhoEntity();
        e.setId(d.getId());
        e.setUsuarioId(d.getUsuarioId());
        e.setItens(d.getItens());
        e.setDataUltimaAtualizacao(d.getDataUltimaAtualizacao());
        return e;
    }
}