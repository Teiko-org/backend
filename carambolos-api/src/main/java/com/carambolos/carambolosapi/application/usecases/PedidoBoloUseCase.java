package com.carambolos.carambolosapi.application.usecases;

import com.carambolos.carambolosapi.application.exception.EntidadeImprocessavelException;
import com.carambolos.carambolosapi.application.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.application.gateways.BoloGateway;
import com.carambolos.carambolosapi.application.gateways.EnderecoGateway;
import com.carambolos.carambolosapi.application.gateways.PedidoBoloGateway;
import com.carambolos.carambolosapi.domain.entity.PedidoBolo;
import com.carambolos.carambolosapi.domain.enums.TipoEntregaEnum;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Service
public class PedidoBoloUseCase {
    private final PedidoBoloGateway pedidoBoloGateway;
    private final BoloGateway boloGateway;
    private final EnderecoGateway enderecoGateway;

    public PedidoBoloUseCase(PedidoBoloGateway pedidoBoloGateway, BoloGateway boloGateway, EnderecoGateway enderecoGateway) {
        this.pedidoBoloGateway = pedidoBoloGateway;
        this.boloGateway = boloGateway;
        this.enderecoGateway = enderecoGateway;
    }

    public List<PedidoBolo> listarPedidos(Integer ano, Integer mes) {
        if (ano == null && mes == null) {
            return pedidoBoloGateway.findAll();
        }

        if (ano == null || mes == null) {
            throw new EntidadeImprocessavelException("Para filtrar pedidos, informe ano e mes juntos.");
        }

        if (mes < 1 || mes > 12) {
            throw new EntidadeImprocessavelException("Mes invalido. Informe um valor entre 1 e 12.");
        }

        YearMonth anoMes = YearMonth.of(ano, mes);
        LocalDateTime dataInicio = anoMes.atDay(1).atStartOfDay();
        LocalDateTime dataFim = anoMes.atEndOfMonth().atTime(LocalTime.MAX);

        return pedidoBoloGateway.findAllByDataUltimaAtualizacaoBetween(dataInicio, dataFim);
    }

    public List<PedidoBolo> listarPedidosCompleto(Integer ano, Integer mes) {
        return listarPedidos(ano, mes);
    }

    public PedidoBolo buscarPedidoPorId(Integer id) {
        return pedidoBoloGateway.findById(id);
    }

    public PedidoBolo cadastrarPedido(PedidoBolo pedidoBolo) {
        if (!boloGateway.existsByIdAndIsAtivoTrue(pedidoBolo.getBoloId())) {
            throw new EntidadeNaoEncontradaException("Bolo com id %d não encontrado".formatted(pedidoBolo.getBoloId()));
        }
        if (pedidoBolo.getTipoEntrega() == TipoEntregaEnum.ENTREGA && pedidoBolo.getEnderecoId() == null) {
            throw new EntidadeImprocessavelException("Tipo de entrega 'ENTREGA' requer um endereço válido.");
        }
        if (pedidoBolo.getEnderecoId() != null && !enderecoGateway.existsByIdAndIsAtivoTrue(pedidoBolo.getEnderecoId())) {
            throw new EntidadeNaoEncontradaException("Endereço com id %d não encontrado".formatted(pedidoBolo.getEnderecoId()));
        }

        pedidoBolo.setDataUltimaAtualizacao(LocalDateTime.now());

        return pedidoBoloGateway.save(pedidoBolo);
    }

    public PedidoBolo atualizarPedido(PedidoBolo pedidoBolo, Integer id) {
        if (!pedidoBoloGateway.existsByIdAndIsAtivoTrue(id)) {
            throw new EntidadeNaoEncontradaException("Pedido com id %d não encontrado".formatted(id));
        }
        if (pedidoBolo.getTipoEntrega() == TipoEntregaEnum.ENTREGA && pedidoBolo.getEnderecoId() == null) {
            throw new EntidadeImprocessavelException("Tipo de entrega 'ENTREGA' requer um endereço válido.");
        }
        if (!boloGateway.existsByIdAndIsAtivoTrue(pedidoBolo.getBoloId())) {
            throw new EntidadeNaoEncontradaException("Bolo com id %d não encontrado".formatted(pedidoBolo.getBoloId()));
        }
        if (pedidoBolo.getEnderecoId() != null && !enderecoGateway.existsByIdAndIsAtivoTrue(pedidoBolo.getEnderecoId())) {
            throw new EntidadeNaoEncontradaException("Endereço com id %d não encontrado".formatted(pedidoBolo.getEnderecoId()));
        }

        pedidoBolo.setDataUltimaAtualizacao(LocalDateTime.now());
        pedidoBolo.setId(id);

        return pedidoBoloGateway.save(pedidoBolo);
    }

    public void deletarPedido(Integer id) {
        PedidoBolo pedido = pedidoBoloGateway.findById(id);
        pedido.setAtivo(false);
        pedidoBoloGateway.save(pedido);
    }
}
