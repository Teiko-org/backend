package com.carambolos.carambolosapi.service;

import com.carambolos.carambolosapi.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.model.PedidoBolo;
import com.carambolos.carambolosapi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoBoloService {

    @Autowired
    private BoloRepository boloRepository;
    @Autowired
    PedidoBoloRepository pedidoBoloRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;

    public List<PedidoBolo> listarPedidos() {
        return pedidoBoloRepository.findAll().stream().filter(PedidoBolo::getAtivo).toList();
    }

    public PedidoBolo buscarPedidoPorId(Integer id) {
        return pedidoBoloRepository.findById(id)
                .filter(PedidoBolo::getAtivo)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Pedido com o id %d não encontrado".formatted(id)));
    }

    public PedidoBolo cadastrarPedido(PedidoBolo pedidoBolo) {
        if (!boloRepository.existsByIdAndIsAtivoTrue(pedidoBolo.getBoloId())) {
            throw new EntidadeNaoEncontradaException("Bolo com id %d não encontrado".formatted(pedidoBolo.getBoloId()));
        }
        if (!enderecoRepository.existsByIdAndIsAtivoTrue(pedidoBolo.getEnderecoId())) {
            throw new EntidadeNaoEncontradaException("Endereço com id %d não encontrado".formatted(pedidoBolo.getEnderecoId()));
        }

        pedidoBolo.setDataUltimaAtualizacao(LocalDateTime.now());

        return pedidoBoloRepository.save(pedidoBolo);
    }

    public PedidoBolo atualizarPedido(PedidoBolo pedidoBolo, Integer id) {
        if (!pedidoBoloRepository.existsByIdAndIsAtivoTrue(id)) {
            throw new EntidadeNaoEncontradaException("Pedido com id %d não encontrado".formatted(id));
        }
        if (!boloRepository.existsByIdAndIsAtivoTrue(pedidoBolo.getBoloId())) {
            throw new EntidadeNaoEncontradaException("Bolo com id %d não encontrado".formatted(pedidoBolo.getBoloId()));
        }
        if (!enderecoRepository.existsByIdAndIsAtivoTrue(pedidoBolo.getEnderecoId())) {
            throw new EntidadeNaoEncontradaException("Endereço com id %d não encontrado".formatted(pedidoBolo.getEnderecoId()));
        }

        pedidoBolo.setDataUltimaAtualizacao(LocalDateTime.now());
        pedidoBolo.setId(id);

        return pedidoBoloRepository.save(pedidoBolo);
    }

    public void deletarPedido(Integer id) {
        PedidoBolo pedido = buscarPedidoPorId(id);
        pedido.setAtivo(false);
        pedidoBoloRepository.save(pedido);
    }
}
