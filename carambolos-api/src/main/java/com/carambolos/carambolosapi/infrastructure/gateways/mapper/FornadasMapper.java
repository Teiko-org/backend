package com.carambolos.carambolosapi.infrastructure.gateways.mapper;

public final class FornadasMapper {
    private FornadasMapper() {}

    // Fornada (domain <-> persistence)
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
        e.setAtivo(Boolean.TRUE.equals(d.getAtivo()));
        return e;
    }

    // PedidoFornada (domain <-> persistence)
    public static com.carambolos.carambolosapi.domain.entity.PedidoFornada toDomain(
            com.carambolos.carambolosapi.infrastructure.persistence.entity.PedidoFornada e) {
        if (e == null) return null;
        var d = new com.carambolos.carambolosapi.domain.entity.PedidoFornada();
        d.setId(e.getId());
        d.setFornadaDaVez(e.getFornadaDaVez());
        d.setEndereco(e.getEndereco());
        d.setUsuario(e.getUsuario());
        d.setQuantidade(e.getQuantidade());
        d.setDataPrevisaoEntrega(e.getDataPrevisaoEntrega());
        d.setTipoEntrega(e.getTipoEntrega());
        d.setNomeCliente(e.getNomeCliente());
        d.setTelefoneCliente(e.getTelefoneCliente());
        d.setHorarioRetirada(e.getHorarioRetirada());
        d.setObservacoes(e.getObservacoes());
        d.setisAtivo(e.getAtivo());
        return d;
    }

    public static com.carambolos.carambolosapi.infrastructure.persistence.entity.PedidoFornada toEntity(
            com.carambolos.carambolosapi.domain.entity.PedidoFornada d) {
        if (d == null) return null;
        var e = new com.carambolos.carambolosapi.infrastructure.persistence.entity.PedidoFornada();
        e.setId(d.getId());
        e.setFornadaDaVez(d.getFornadaDaVez());
        e.setEndereco(d.getEndereco());
        e.setUsuario(d.getUsuario());
        e.setQuantidade(d.getQuantidade());
        e.setDataPrevisaoEntrega(d.getDataPrevisaoEntrega());
        e.setTipoEntrega(d.getTipoEntrega());
        e.setNomeCliente(d.getNomeCliente());
        e.setTelefoneCliente(d.getTelefoneCliente());
        e.setHorarioRetirada(d.getHorarioRetirada());
        e.setObservacoes(d.getObservacoes());
        e.setAtivo(Boolean.TRUE.equals(d.getisAtivo()));
        return e;
    }

    // FornadaDaVez (persistence passthroughs)
    public static com.carambolos.carambolosapi.infrastructure.persistence.entity.FornadaDaVez toDomain(
            com.carambolos.carambolosapi.infrastructure.persistence.entity.FornadaDaVez e) {
        return e; // mesma estrutura (persistence)
    }

    public static com.carambolos.carambolosapi.infrastructure.persistence.entity.FornadaDaVez toEntity(
            com.carambolos.carambolosapi.infrastructure.persistence.entity.FornadaDaVez d) {
        return d; // mesma estrutura (persistence)
    }
}


