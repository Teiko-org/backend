package com.carambolos.carambolosapi.application.usecases;

import com.carambolos.carambolosapi.application.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.application.gateways.*;
import com.carambolos.carambolosapi.domain.entity.ResumoPedido;
import com.carambolos.carambolosapi.domain.enums.StatusEnum;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.ResumoPedidoEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ResumoPedidoUseCase {
    private final ResumoPedidoGateway resumoPedidoGateway;
    private final PedidoBoloGateway pedidoBoloGateway;
    private final PedidoFornadaGateway pedidoFornadaGateway;
    private final FornadaDaVezGateway fornadaDaVezGateway;
    private final ProdutoFornadaGateway produtoFornadaGateway;
    private final BoloGateway boloGateway;
    private final RecheioPedidoGateway recheioPedidoGateway;
    private final RecheioExclusivoGateway recheioExclusivoGateway;
    private final RecheioUnitarioGateway recheioUnitarioGateway;
    private final MassaGateway massaGateway;

    public ResumoPedidoUseCase(ResumoPedidoGateway resumoPedidoGateway, PedidoBoloGateway pedidoBoloGateway, PedidoFornadaGateway pedidoFornadaGateway, FornadaDaVezGateway fornadaDaVezGateway, ProdutoFornadaGateway produtoFornadaGateway, BoloGateway boloGateway, RecheioPedidoGateway recheioPedidoGateway, RecheioExclusivoGateway recheioExclusivoGateway, RecheioUnitarioGateway recheioUnitarioGateway, MassaGateway massaGateway) {
        this.resumoPedidoGateway = resumoPedidoGateway;
        this.pedidoBoloGateway = pedidoBoloGateway;
        this.pedidoFornadaGateway = pedidoFornadaGateway;
        this.fornadaDaVezGateway = fornadaDaVezGateway;
        this.produtoFornadaGateway = produtoFornadaGateway;
        this.boloGateway = boloGateway;
        this.recheioPedidoGateway = recheioPedidoGateway;
        this.recheioExclusivoGateway = recheioExclusivoGateway;
        this.recheioUnitarioGateway = recheioUnitarioGateway;
        this.massaGateway = massaGateway;
    }

    public List<ResumoPedido> listarResumosPedidos() {
        return resumoPedidoGateway.findAllByIsAtivoTrue();
    }

    public ResumoPedido buscarResumoPedidoPorId(Integer id) {
        return resumoPedidoGateway.findByIdAndIsAtivoTrue(id);
    }

    public List<ResumoPedido> buscarResumosPedidosPorDataPedido(LocalDate dataPedido) {
        LocalDateTime comecoData = dataPedido.atStartOfDay();
        LocalDateTime fimData = dataPedido.atTime(23, 59, 59);
        return resumoPedidoGateway.findByDataPedidoBetweenAndIsAtivoTrue(comecoData, fimData);
    }

    public List<ResumoPedido> buscarResumosPedidosPorStatus(StatusEnum status) {
        return resumoPedidoGateway.findByStatusAndIsAtivoTrue(status);
    }

    public ResumoPedido cadastrarResumoPedido(ResumoPedido resumoPedido) {
        validarReferencias(resumoPedido);

        resumoPedido.setDataPedido(LocalDateTime.now());
        resumoPedido.setStatus(StatusEnum.PENDENTE);

        if (resumoPedido.getPedidoFornadaId() != null) {
            resumoPedido.setValor(calcularValorPedidoFornada(resumoPedido.getPedidoFornadaId()));
        } else if (resumoPedido.getPedidoBoloId() != null) {
            resumoPedido.setValor(calcularValorPedidoBolo(resumoPedido.getPedidoBoloId()));
        }

        return resumoPedidoGateway.save(resumoPedido);
    }

    public ResumoPedido atualizarResumoPedido(Integer id, ResumoPedido resumoPedido) {
        if (!resumoPedidoGateway.existsByIdAndIsAtivoTrue(id)) {
            throw new EntidadeNaoEncontradaException("Resumo de pedido não encontrado");
        }
        validarReferencias(resumoPedido);
        resumoPedido.setId(id);
        return resumoPedidoGateway.save(resumoPedido);
    }

    private void validarReferencias(ResumoPedido resumoPedido) {
        if (resumoPedido.getPedidoBoloId() == null && resumoPedido.getPedidoFornadaId() == null) {
            throw new IllegalArgumentException("O resumo de pedido deve estar vinculado a um pedido de bolo ou pedido de fornada");
        }

        if (resumoPedido.getPedidoBoloId() != null) {
            validarPedidoBolo(resumoPedido.getPedidoBoloId());
        }

        if(resumoPedido.getPedidoFornadaId() != null) {
            validarPedidoFornada(resumoPedido.getPedidoFornadaId());
        }
    }

    private void validarPedidoBolo(Integer pedidoBoloId) {
        if (!pedidoBoloGateway.existsByIdAndIsAtivoTrue(pedidoBoloId)) {
            throw new EntidadeNaoEncontradaException("Pedido de bolo com ID %d não encontrado".formatted(pedidoBoloId));
        }
    }

    private void validarPedidoFornada(Integer pedidoFornadaId) {
        if (!pedidoFornadaGateway.existsByIdAndIsAtivoTrue(pedidoFornadaId)) {
            throw new EntidadeNaoEncontradaException("Pedido de fornada com ID %d não encontrado".formatted(pedidoFornadaId));
        }
    }

    private Double calcularValorPedidoFornada(Integer pedidoFornadaId) {
        var pedidoFornada = pedidoFornadaGateway.findById(pedidoFornadaId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Pedido fornada não encontrado"));

        var fornadaDaVez = fornadaDaVezGateway.findById(pedidoFornada.getFornadaDaVez())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Fornada da vez não encontrada"));

        var produtoFornada = produtoFornadaGateway.findById(fornadaDaVez.getProdutoFornada())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Produto da fornada não encontrado"));

        Double valorProduto = produtoFornada.getValor();
        return valorProduto * pedidoFornada.getQuantidade();
    }

    private Double calcularValorPedidoBolo(Integer pedidoBoloId) {
        var pedidoBolo = pedidoBoloGateway.findById(pedidoBoloId);

        var bolo = boloGateway.findById(pedidoBolo.getBoloId());

        Double valorTamanho = 0.0;
        if (bolo.getTamanho() != null) {
            switch (bolo.getTamanho()) {
                case TAMANHO_5 -> valorTamanho = 50.0;
                case TAMANHO_7 -> valorTamanho = 100.0;
                case TAMANHO_12 -> valorTamanho = 150.0;
                case TAMANHO_15 -> valorTamanho = 200.0;
                case TAMANHO_17 -> valorTamanho = 250.0;
            }
        }

        Double valorRecheio = 0.0;
        if (bolo.getRecheioPedido() != null) {
            var recheioPedido = recheioPedidoGateway.findById(bolo.getRecheioPedido());

            if (recheioPedido.getRecheioExclusivo() != null) {
                var recheioExclusivo = recheioExclusivoGateway.findById(recheioPedido.getRecheioExclusivo());

                if (recheioExclusivo.getRecheioUnitarioId1() != null) {
                    var recheioUnitario1 = recheioUnitarioGateway.findById(recheioExclusivo.getRecheioUnitarioId1());

                    if (recheioUnitario1 != null && recheioUnitario1.getValor() != null) {
                        valorRecheio += recheioUnitario1.getValor();
                    }
                }

                if (recheioExclusivo.getRecheioUnitarioId2() != null) {
                    var recheioUnitario2 = recheioUnitarioGateway.findById(recheioExclusivo.getRecheioUnitarioId2());

                    if (recheioUnitario2 != null && recheioUnitario2.getValor() != null) {
                        valorRecheio += recheioUnitario2.getValor();
                    }
                }
            } else {
                if (recheioPedido.getRecheioUnitarioId1() != null) {
                    var recheioUnitario1 = recheioUnitarioGateway.findById(recheioPedido.getRecheioUnitarioId1());

                    if (recheioUnitario1 != null && recheioUnitario1.getValor() != null) {
                        valorRecheio += recheioUnitario1.getValor();
                    }
                }

                if (recheioPedido.getRecheioUnitarioId2() != null) {
                    var recheioUnitario2 = recheioUnitarioGateway.findById(recheioPedido.getRecheioUnitarioId2());

                    if (recheioUnitario2 != null && recheioUnitario2.getValor() != null) {
                        valorRecheio += recheioUnitario2.getValor();
                    }
                }
            }
        }

        Double valorMassa = 0.0;
        if (bolo.getMassa() != null) {
            var massa = massaGateway.findById(bolo.getMassa());

            if (massa != null && massa.getValor() != null) {
                valorMassa = massa.getValor();
            }
        }

        Double valorTotal = valorTamanho + valorRecheio + valorMassa;
        return valorTotal;
    }
}
