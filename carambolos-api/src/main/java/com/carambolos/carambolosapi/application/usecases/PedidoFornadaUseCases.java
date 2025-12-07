package com.carambolos.carambolosapi.application.usecases;

import com.carambolos.carambolosapi.application.exception.EntidadeImprocessavelException;
import com.carambolos.carambolosapi.application.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.application.gateways.EnderecoGateway;
import com.carambolos.carambolosapi.application.gateways.FornadaDaVezGateway;
import com.carambolos.carambolosapi.application.gateways.FornadaGateway;
import com.carambolos.carambolosapi.application.gateways.PedidoEventosGateway;
import com.carambolos.carambolosapi.application.gateways.PedidoFornadaGateway;
import com.carambolos.carambolosapi.application.gateways.UsuarioGateway;
import com.carambolos.carambolosapi.domain.entity.Fornada;
import com.carambolos.carambolosapi.domain.entity.PedidoFornada;
import com.carambolos.carambolosapi.domain.enums.TipoEntregaEnum;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.FornadasMapper;
import com.carambolos.carambolosapi.infrastructure.messaging.dto.PedidoEvento;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.FornadaDaVez;
import com.carambolos.carambolosapi.infrastructure.web.request.PedidoFornadaRequestDTO;
import com.carambolos.carambolosapi.infrastructure.web.request.PedidoFornadaUpdateRequestDTO;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

public class PedidoFornadaUseCases {
    private final PedidoFornadaGateway pedidos;
    private final FornadaDaVezGateway fdv;
    private final FornadaGateway fornadas;
    private final EnderecoGateway enderecos;
    private final UsuarioGateway usuarios;
    private final PedidoEventosGateway eventos;

    public PedidoFornadaUseCases(
            PedidoFornadaGateway pedidos,
            FornadaDaVezGateway fdv,
            FornadaGateway fornadas,
            EnderecoGateway enderecos,
            UsuarioGateway usuarios,
            PedidoEventosGateway eventos
    ) {
        this.pedidos = pedidos;
        this.fdv = fdv;
        this.fornadas = fornadas;
        this.enderecos = enderecos;
        this.usuarios = usuarios;
        this.eventos = eventos;
    }

    @Transactional
    public PedidoFornada criar(PedidoFornadaRequestDTO request) {
        FornadaDaVez fornadaDaVez = fdv.findById(request.fornadaDaVezId())
                .filter(fdv -> fdv.isAtivo())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("FornadaDaVez com ID " + request.fornadaDaVezId() + " não encontrada ou foi ocultada."));

        Fornada fornada = fornadas.findById(fornadaDaVez.getFornada())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Fornada com ID " + fornadaDaVez.getFornada() + " não encontrada."));
        
        if (fornada.isAtivo() == null || !Boolean.TRUE.equals(fornada.isAtivo())) {
            throw new EntidadeImprocessavelException("A fornada associada a este produto não está mais ativa.");
        }

        LocalDate hoje = LocalDate.now();
        if (fornada.getDataFim().isBefore(hoje)) {
            throw new EntidadeImprocessavelException(
                    String.format("Esta fornada encerrou em %s. Não é mais possível realizar pedidos.",
                    fornada.getDataFim().toString())
            );
        }

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
            enderecos.buscarPorId(request.enderecoId());
        }

        if (request.usuarioId() != null) {
            usuarios.buscarPorId(request.usuarioId());
        }

        int novaQuantidade = fornadaDaVez.getQuantidade() - request.quantidade();
        fornadaDaVez.setQuantidade(novaQuantidade);
        fdv.save(fornadaDaVez);

        PedidoFornada pedido = FornadasMapper.toDomain(request.toEntity(request));
        pedido = pedidos.save(pedido);

        PedidoFornada pedidoFinal = pedido;
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                try {
                    PedidoEvento evt = new PedidoEvento();
                    evt.setEvento("PEDIDO_CRIADO");
                    evt.setPedidoId(pedidoFinal.getId());
                    evt.setClienteId(pedidoFinal.getUsuario());
                    evt.setDataEvento(OffsetDateTime.now());
                    evt.setOrigem("api");
                    eventos.publicarPedidoCriado(evt);
                } catch (Exception ignored) {}
            }
        });

        return pedido;
    }

    public PedidoFornada buscarPorId(Integer id) {
        return pedidos.findById(id)
                .filter(pedido -> pedido.getisAtivo())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("PedidoFornada com ID " + id + " não encontrado."));
    }

    public List<PedidoFornada> listar() {
        return pedidos.findAll().stream().filter(pedido -> pedido.getisAtivo()).toList();
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

        try {
            int antigaQtd = pedido.getQuantidade() != null ? pedido.getQuantidade() : 0;
            int novaQtd = request.quantidade();
            int delta = novaQtd - antigaQtd;
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
        }

        pedido.setQuantidade(request.quantidade());
        pedido.setDataPrevisaoEntrega(request.dataPrevisaoEntrega());
        return pedidos.save(pedido);
    }
}
