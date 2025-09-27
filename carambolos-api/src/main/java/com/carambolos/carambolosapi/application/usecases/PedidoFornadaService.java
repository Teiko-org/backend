package com.carambolos.carambolosapi.application.usecases;

import com.carambolos.carambolosapi.infrastructure.persistence.entity.UsuarioEntity;
import com.carambolos.carambolosapi.infrastructure.web.request.PedidoFornadaRequestDTO;
import com.carambolos.carambolosapi.infrastructure.web.request.PedidoFornadaUpdateRequestDTO;
import com.carambolos.carambolosapi.application.exception.EntidadeImprocessavelException;
import com.carambolos.carambolosapi.application.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.domain.entity.Endereco;
import com.carambolos.carambolosapi.domain.entity.FornadaDaVez;
import com.carambolos.carambolosapi.domain.entity.PedidoFornada;
import com.carambolos.carambolosapi.domain.enums.TipoEntregaEnum;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.EnderecoRepository;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.FornadaDaVezRepository;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.PedidoFornadaRepository;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.UsuarioRepository;
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
                    .filter(UsuarioEntity::isAtivo)
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
        try {
            FornadaDaVez fdv = fornadaDaVezRepository.findById(pedidoFornada.getFornadaDaVez())
                    .orElse(null);
            if (fdv != null) {
                int atual = fdv.getQuantidade() != null ? fdv.getQuantidade() : 0;
                fdv.setQuantidade(atual + (pedidoFornada.getQuantidade() != null ? pedidoFornada.getQuantidade() : 0));
                fornadaDaVezRepository.save(fdv);
            }
        } catch (Exception e) {
            System.err.println("[ESTOQUE] Falha ao repor estoque ao excluir pedido fornada: " + e.getMessage());
        }
        pedidoFornada.setAtivo(false);
        pedidoFornadaRepository.save(pedidoFornada);
    }

    public PedidoFornada atualizarPedidoFornada(Integer id, PedidoFornadaUpdateRequestDTO request) {
        PedidoFornada pedidoFornada = buscarPedidoFornada(id);

        if (request.quantidade() == null || request.quantidade() <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }

        // Ajustar estoque conforme a diferença de quantidade
        try {
            int antigaQtd = pedidoFornada.getQuantidade() != null ? pedidoFornada.getQuantidade() : 0;
            int novaQtd = request.quantidade();
            int delta = novaQtd - antigaQtd; // positivo: precisa reservar mais; negativo: devolver

            if (delta != 0) {
                FornadaDaVez fdv = fornadaDaVezRepository.findById(pedidoFornada.getFornadaDaVez())
                        .orElse(null);
                if (fdv != null) {
                    int atual = fdv.getQuantidade() != null ? fdv.getQuantidade() : 0;
                    int ajustado = delta > 0 ? (atual - delta) : (atual + (-delta));
                    if (ajustado < 0) {
                        throw new EntidadeImprocessavelException("Estoque insuficiente para aumentar a quantidade do pedido.");
                    }
                    fdv.setQuantidade(ajustado);
                    fornadaDaVezRepository.save(fdv);
                }
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("[ESTOQUE] Falha ao ajustar estoque ao atualizar pedido fornada: " + e.getMessage());
        }

        pedidoFornada.setQuantidade(request.quantidade());
        pedidoFornada.setDataPrevisaoEntrega(request.dataPrevisaoEntrega());

        return pedidoFornadaRepository.save(pedidoFornada);
    }
}

