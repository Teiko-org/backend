package com.carambolos.carambolosapi.application.usecases;

import com.carambolos.carambolosapi.application.gateways.CarrinhoGateway;
import com.carambolos.carambolosapi.application.gateways.UsuarioGateway;
import com.carambolos.carambolosapi.domain.entity.Carrinho;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

public class CarrinhoUseCases {
    private final CarrinhoGateway carrinhoGateway;
    private final UsuarioGateway usuarioGateway;
    private final ObjectMapper objectMapper;

    public CarrinhoUseCases(CarrinhoGateway carrinhoGateway, UsuarioGateway usuarioGateway, ObjectMapper objectMapper) {
        this.carrinhoGateway = carrinhoGateway;
        this.usuarioGateway = usuarioGateway;
        this.objectMapper = objectMapper;
    }

    public Carrinho buscarCarrinhoPorUsuario(Integer usuarioId) {
        usuarioGateway.buscarPorId(usuarioId);
        return carrinhoGateway.findByUsuarioId(usuarioId)
                .orElse(new Carrinho(null, usuarioId, "[]", LocalDateTime.now()));
    }

    public Carrinho salvarCarrinho(Integer usuarioId, String itensJson) {
        usuarioGateway.buscarPorId(usuarioId);

        try {
            objectMapper.readValue(itensJson, new TypeReference<List<Object>>() {});
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("JSON de itens inválido: " + e.getMessage());
        }

        Carrinho carrinhoExistente = carrinhoGateway.findByUsuarioId(usuarioId).orElse(null);

        if (carrinhoExistente != null) {
            carrinhoExistente.setItens(itensJson);
            carrinhoExistente.setDataUltimaAtualizacao(LocalDateTime.now());
            return carrinhoGateway.save(carrinhoExistente);
        } else {
            Carrinho novoCarrinho = new Carrinho(null, usuarioId, itensJson, LocalDateTime.now());
            return carrinhoGateway.save(novoCarrinho);
        }
    }

    public void limparCarrinho(Integer usuarioId) {
        usuarioGateway.buscarPorId(usuarioId);
        carrinhoGateway.deleteByUsuarioId(usuarioId);
    }
}