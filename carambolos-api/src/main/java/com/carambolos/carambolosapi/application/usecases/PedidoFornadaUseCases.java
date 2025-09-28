package com.carambolos.carambolosapi.application.usecases;

import com.carambolos.carambolosapi.application.exception.EntidadeImprocessavelException;
import com.carambolos.carambolosapi.application.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.application.gateways.FornadaDaVezGateway;
import com.carambolos.carambolosapi.application.gateways.PedidoFornadaGateway;
import com.carambolos.carambolosapi.domain.entity.Endereco;
import com.carambolos.carambolosapi.domain.entity.PedidoFornada;
import com.carambolos.carambolosapi.domain.entity.Usuario;
import com.carambolos.carambolosapi.domain.enums.TipoEntregaEnum;
import com.carambolos.carambolosapi.infrastructure.web.request.PedidoFornadaRequestDTO;
import com.carambolos.carambolosapi.infrastructure.web.request.PedidoFornadaUpdateRequestDTO;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.FornadaDaVez;
import com.carambolos.carambolosapi.infrastructure.gateways.mapperEntity.PedidoFornadaEntityMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class PedidoFornadaUseCases {
    private final PedidoFornadaGateway pedidos;
    private final FornadaDaVezGateway fdv;
    private final com.carambolos.carambolosapi.infrastructure.persistence.jpa.EnderecoRepository enderecos;
    private final com.carambolos.carambolosapi.infrastructure.persistence.jpa.UsuarioRepository usuarios;

    public PedidoFornadaUseCases(
            PedidoFornadaGateway pedidos,
            FornadaDaVezGateway fdv,
            com.carambolos.carambolosapi.infrastructure.persistence.jpa.EnderecoRepository enderecos,
            com.carambolos.carambolosapi.infrastructure.persistence.jpa.UsuarioRepository usuarios
    ) {
        this.pedidos = pedidos;
        this.fdv = fdv;
        this.enderecos = enderecos;
        this.usuarios = usuarios;
    }

    @Transactional
    public PedidoFornada criar(PedidoFornadaRequestDTO request) {
        FornadaDaVez fornadaDaVez = fdv.findById(request.fornadaDaVezId())
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
            enderecos.findById(request.enderecoId())
                    .filter(Endereco::isAtivo)
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Endereço com ID " + request.enderecoId() + " não encontrado."));
        }

        if (request.usuarioId() != null) {
            usuarios.findById(request.usuarioId())
                    .filter(Usuario::isAtivo)
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuário com ID " + request.usuarioId() + " não encontrado."));
        }

        int novaQuantidade = fornadaDaVez.getQuantidade() - request.quantidade();
        fornadaDaVez.setQuantidade(novaQuantidade);
        fdv.save(fornadaDaVez);

        PedidoFornada pedido = PedidoFornadaEntityMapper.toDomain(request.toEntity(request));
        return pedidos.save(pedido);
    }

    public PedidoFornada buscarPorId(Integer id) {
        return pedidos.findById(id)
                .filter(PedidoFornada::getisAtivo)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("PedidoFornada com ID " + id + " não encontrado."));
    }

    public List<PedidoFornada> listar() {
        return pedidos.findAll().stream().filter(PedidoFornada::getisAtivo).toList();
    }

    @Transactional
    public void excluir(Integer id) {
        PedidoFornada pedido = buscarPorId(id);
        try {
            FornadaDaVez fz = fdv.findById(pedido.getFornadaDaVez()).orElse(null);
            if (fz != null) {
                int atual = fz.getQuantidade() != null ? fz.getQuantidade() : 0;
                fz.setQuantidade(atual + (pedido.getQuantidade() != null ? pedido.getQuantidade() : 0));
                fdv.save(fz);
            }
        } catch (Exception ignored) {}
        pedido.setisAtivo(false);
        pedidos.save(pedido);
    }

    @Transactional
    public PedidoFornada atualizar(Integer id, PedidoFornadaUpdateRequestDTO request) {
        PedidoFornada pedido = buscarPorId(id);
        if (request.quantidade() == null || request.quantidade() <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }

        // Ajustar estoque conforme a diferença
        try {
            int antigaQtd = pedido.getQuantidade() != null ? pedido.getQuantidade() : 0;
            int novaQtd = request.quantidade();
            int delta = novaQtd - antigaQtd; // positivo: reservar mais; negativo: devolver
            if (delta != 0) {
                FornadaDaVez fz = fdv.findById(pedido.getFornadaDaVez()).orElse(null);
                if (fz != null) {
                    int atual = fz.getQuantidade() != null ? fz.getQuantidade() : 0;
                    int ajustado = delta > 0 ? (atual - delta) : (atual + (-delta));
                    if (ajustado < 0) {
                        throw new EntidadeImprocessavelException("Estoque insuficiente para aumentar a quantidade do pedido.");
                    }
                    fz.setQuantidade(ajustado);
                    fdv.save(fz);
                }
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            // logar
        }

        pedido.setQuantidade(request.quantidade());
        pedido.setDataPrevisaoEntrega(request.dataPrevisaoEntrega());
        return pedidos.save(pedido);
    }
}
