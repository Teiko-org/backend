package com.carambolos.carambolosapi.infrastructure.gateways.mapper;

public final class FornadasMapper {
    private FornadasMapper() {}

    // Fornada (domain <-> persistence)
    public static com.carambolos.carambolosapi.domain.entity.Fornada toDomain(
            com.carambolos.carambolosapi.infrastructure.persistence.entity.Fornada e) {
        if (e == null) return null;
        var d = new com.carambolos.carambolosapi.domain.entity.Fornada();
        d.setId(e.getId());
        d.setDataInicio(e.getDataInicio());
        d.setDataFim(e.getDataFim());
        d.setAtivo(e.isAtivo());
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

    // ProdutoFornada (domain <-> persistence)
    public static com.carambolos.carambolosapi.domain.entity.ProdutoFornada toDomain(
            com.carambolos.carambolosapi.infrastructure.persistence.entity.ProdutoFornada e) {
        if (e == null) return null;
        var d = new com.carambolos.carambolosapi.domain.entity.ProdutoFornada();
        d.setId(e.getId());
        d.setProduto(e.getProduto());
        d.setDescricao(e.getDescricao());
        d.setValor(e.getValor());
        d.setCategoria(e.getCategoria());
        d.setAtivo(e.isAtivo());
        if (e.getImagens() != null) {
            d.setImagens(e.getImagens().stream().map(FornadasMapper::toDomain).toList());
        }
        return d;
    }

    public static com.carambolos.carambolosapi.infrastructure.persistence.entity.ProdutoFornada toEntity(
            com.carambolos.carambolosapi.domain.entity.ProdutoFornada d) {
        if (d == null) return null;
        var e = new com.carambolos.carambolosapi.infrastructure.persistence.entity.ProdutoFornada();
        e.setId(d.getId());
        e.setProduto(d.getProduto());
        e.setDescricao(d.getDescricao());
        e.setValor(d.getValor());
        e.setCategoria(d.getCategoria());
        e.setAtivo(Boolean.TRUE.equals(d.getAtivo()));
        if (d.getImagens() != null) {
            e.setImagens(d.getImagens().stream().map(img -> {
                var ent = FornadasMapper.toEntity(img);
                ent.setProdutoFornada(e);
                return ent;
            }).toList());
        }
        return e;
    }

    // ImagemProdutoFornada (domain <-> persistence)
    public static com.carambolos.carambolosapi.domain.entity.ImagemProdutoFornada toDomain(
            com.carambolos.carambolosapi.infrastructure.persistence.entity.ImagemProdutoFornada e) {
        if (e == null) return null;
        var d = new com.carambolos.carambolosapi.domain.entity.ImagemProdutoFornada();
        d.setId(e.getId());
        d.setUrl(e.getUrl());
        // relação setada no ProdutoFornada
        return d;
    }

    public static com.carambolos.carambolosapi.infrastructure.persistence.entity.ImagemProdutoFornada toEntity(
            com.carambolos.carambolosapi.domain.entity.ImagemProdutoFornada d) {
        if (d == null) return null;
        var e = new com.carambolos.carambolosapi.infrastructure.persistence.entity.ImagemProdutoFornada();
        e.setId(d.getId());
        e.setUrl(d.getUrl());
        return e;
    }
}


