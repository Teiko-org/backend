package com.carambolos.carambolosapi.service;

import com.carambolos.carambolosapi.controller.request.PedidoFornadaRequestDTO;
import com.carambolos.carambolosapi.controller.request.PedidoFornadaUpdateRequestDTO;
import com.carambolos.carambolosapi.exception.EntidadeImprocessavelException;
import com.carambolos.carambolosapi.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.model.Endereco;
import com.carambolos.carambolosapi.model.FornadaDaVez;
import com.carambolos.carambolosapi.model.PedidoFornada;
import com.carambolos.carambolosapi.model.Usuario;
import com.carambolos.carambolosapi.model.enums.TipoEntregaEnum;
import com.carambolos.carambolosapi.repository.*;
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
                .filter(FornadaDaVez::isAtivo)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("FornadaDaVez com ID " + request.fornadaDaVezId() + " não encontrada."));

        if (fornadaDaVez.getQuantidade() < request.quantidade()) {
            throw new EntidadeImprocessavelException(
                    String.format("Estoque insuficiente. Disponível: %d, Solicitado: %d",
                            fornadaDaVez.getQuantidade(), request.quantidade())
            );
        }

        if (request.tipoEntrega() == TipoEntregaEnum.ENTREGA && request.enderecoId() == null) {
            throw new EntidadeImprocessavelException("Tipo de entrega 'ENTREGA' requer um endereço válido.");
        }

        if (request.enderecoId() != null) {
            enderecoRepository.findById(request.enderecoId())
                    .filter(Endereco::isAtivo)
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Endereço com ID " + request.enderecoId() + " não encontrado."));
        }

        if (request.usuarioId() != null) {
            usuarioRepository.findById(request.usuarioId())
                    .filter(Usuario::isAtivo)
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuário com ID " + request.usuarioId() + " não encontrado."));
        }

        int novaQuantidade = fornadaDaVez.getQuantidade() - request.quantidade();
        fornadaDaVez.setQuantidade(novaQuantidade);

        fornadaDaVezRepository.save(fornadaDaVez);

        System.out.println("✅ ESTOQUE ATUALIZADO: Produto " + fornadaDaVez.getProdutoFornada() +
                " | Quantidade anterior: " + (fornadaDaVez.getQuantidade() + request.quantidade()) +
                " | Vendido: " + request.quantidade() +
                " | Nova quantidade: " + novaQuantidade);

        PedidoFornada pedidoFornada = request.toEntity(request);
        return pedidoFornadaRepository.save(pedidoFornada);
    }

    public PedidoFornada buscarPedidoFornada(Integer id) {
        return pedidoFornadaRepository.findById(id)
                .filter(PedidoFornada::isAtivo)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("PedidoFornada com ID " + id + " não encontrado."));
    }

    public List<PedidoFornada> listarPedidosFornada() {
        return pedidoFornadaRepository.findAll().stream()
                .filter(PedidoFornada::isAtivo)
                .toList();
    }

    public void excluirPedidoFornada(Integer id) {
        PedidoFornada pedidoFornada = buscarPedidoFornada(id);
        pedidoFornada.setAtivo(false);
        pedidoFornadaRepository.save(pedidoFornada);
    }

    public PedidoFornada atualizarPedidoFornada(Integer id, PedidoFornadaUpdateRequestDTO request) {
        PedidoFornada pedidoFornada = buscarPedidoFornada(id);

        if (request.quantidade() == null || request.quantidade() <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }

        pedidoFornada.setQuantidade(request.quantidade());
        pedidoFornada.setDataPrevisaoEntrega(request.dataPrevisaoEntrega());

        return pedidoFornadaRepository.save(pedidoFornada);
    }
}

