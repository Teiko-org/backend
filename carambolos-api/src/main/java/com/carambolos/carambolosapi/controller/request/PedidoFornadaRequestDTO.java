package com.carambolos.carambolosapi.controller.request;

import com.carambolos.carambolosapi.model.Endereco;
import com.carambolos.carambolosapi.model.FornadaDaVez;
import com.carambolos.carambolosapi.model.PedidoFornada;
import com.carambolos.carambolosapi.model.Usuario;

import java.time.LocalDate;

public record PedidoFornadaRequestDTO(
        Integer fornadaDaVezId,
        Integer enderecoId,
        Integer usuarioId,
        Integer quantidade,
        LocalDate dataPrevisaoEntrega
) {
    public PedidoFornada toEntity(FornadaDaVez fornadaDaVez, Endereco endereco, Usuario usuario) {
        PedidoFornada pedidoFornada = new PedidoFornada();
        pedidoFornada.setFornadaDaVez(fornadaDaVez);
        pedidoFornada.setEndereco(endereco);
        pedidoFornada.setUsuario(usuario);
        pedidoFornada.setQuantidade(quantidade);
        pedidoFornada.setDataPrevisaoEntrega(dataPrevisaoEntrega);
        return pedidoFornada;
    }
}
