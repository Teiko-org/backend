package com.carambolos.carambolosapi.application.gateways;

import com.carambolos.carambolosapi.domain.entity.Carrinho;

import java.util.Optional;

public interface CarrinhoGateway {
    Optional<Carrinho> findByUsuarioId(Integer usuarioId);
    Carrinho save(Carrinho carrinho);
    void deleteByUsuarioId(Integer usuarioId);
}