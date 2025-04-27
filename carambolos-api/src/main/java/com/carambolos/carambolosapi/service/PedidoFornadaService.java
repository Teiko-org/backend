package com.carambolos.carambolosapi.service;

import com.carambolos.carambolosapi.controller.request.PedidoFornadaRequestDTO;
import com.carambolos.carambolosapi.controller.request.PedidoFornadaUpdateRequestDTO;
import com.carambolos.carambolosapi.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.model.Endereco;
import com.carambolos.carambolosapi.model.FornadaDaVez;
import com.carambolos.carambolosapi.model.PedidoFornada;
import com.carambolos.carambolosapi.model.Usuario;
import com.carambolos.carambolosapi.repository.EnderecoRepository;
import com.carambolos.carambolosapi.repository.FornadaDaVezRepository;
import com.carambolos.carambolosapi.repository.PedidoFornadaRepository;
import com.carambolos.carambolosapi.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoFornadaService {
    private final PedidoFornadaRepository pedidoFornadaRepository;
    private final FornadaDaVezRepository fornadaDaVezRepository;
    private final EnderecoRepository enderecoRepository;
    private final UsuarioRepository usuarioRepository;

    public PedidoFornadaService(
            PedidoFornadaRepository pedidoFornadaRepository,
            FornadaDaVezRepository fornadaDaVezRepository,
            EnderecoRepository enderecoRepository,
            UsuarioRepository usuarioRepository) {
        this.pedidoFornadaRepository = pedidoFornadaRepository;
        this.fornadaDaVezRepository = fornadaDaVezRepository;
        this.enderecoRepository = enderecoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public PedidoFornada criarPedidoFornada(PedidoFornadaRequestDTO request) {
        FornadaDaVez fornadaDaVez = fornadaDaVezRepository.findById(request.fornadaDaVezId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("FornadaDaVez com ID " + request.fornadaDaVezId() + " não encontrada."));

        Endereco endereco = enderecoRepository.findById(request.enderecoId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Endereço com ID " + request.enderecoId() + " não encontrado."));

        Usuario usuario = null;
        if (request.usuarioId() != null) {
            usuario = usuarioRepository.findById(request.usuarioId())
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuário com ID " + request.usuarioId() + " não encontrado."));
        }

        PedidoFornada pedidoFornada = request.toEntity(fornadaDaVez, endereco, usuario);
        return pedidoFornadaRepository.save(pedidoFornada);
    }

    public PedidoFornada buscarPedidoFornada(Integer id) {
        return pedidoFornadaRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("PedidoFornada com ID " + id + " não encontrado."));
    }

    public List<PedidoFornada> listarPedidosFornada() {
        return pedidoFornadaRepository.findAll();
    }

    public void excluirPedidoFornada(Integer id) {
        if (!pedidoFornadaRepository.existsById(id)) {
            throw new EntidadeNaoEncontradaException("PedidoFornada com ID " + id + " não existe.");
        }
        pedidoFornadaRepository.deleteById(id);
    }

    public PedidoFornada atualizarPedidoFornada(Integer id, PedidoFornadaUpdateRequestDTO request) {
        PedidoFornada pedidoFornada = pedidoFornadaRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("PedidoFornada com ID " + id + " não encontrado para atualização."));

        pedidoFornada.setQuantidade(request.quantidade());
        pedidoFornada.setDataPrevisaoEntrega(request.dataPrevisaoEntrega());

        return pedidoFornadaRepository.save(pedidoFornada);
    }
}

