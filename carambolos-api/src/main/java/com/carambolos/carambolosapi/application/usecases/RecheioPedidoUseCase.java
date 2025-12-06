package com.carambolos.carambolosapi.application.usecases;

import com.carambolos.carambolosapi.application.exception.EntidadeImprocessavelException;
import com.carambolos.carambolosapi.application.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.application.gateways.RecheioPedidoGateway;
import com.carambolos.carambolosapi.domain.entity.RecheioPedido;
import com.carambolos.carambolosapi.infrastructure.persistence.projection.RecheioPedidoProjection;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

public class RecheioPedidoUseCase {
    private final RecheioPedidoGateway gateway;

    public RecheioPedidoUseCase(RecheioPedidoGateway gateway) {
        this.gateway = gateway;
    }

    public RecheioPedidoProjection cadastrarRecheioPedido(RecheioPedido recheioPedido) {
        verificarCampos(recheioPedido);

        if (recheioPedido.getRecheioExclusivo() != null) {
            RecheioPedido recheioSalvo = gateway.save(recheioPedido);
            return gateway.buscarRecheioPedidoExclusivoPorId(recheioSalvo.getId());
        }
        RecheioPedido recheioSalvo = gateway.save(recheioPedido);
        return gateway.buscarRecheioPedidoUnitariosPorId(recheioSalvo.getId());
    }

    public RecheioPedidoProjection atualizarRecheioPedido(RecheioPedido recheioPedido, Integer id) {
        verificarCampos(recheioPedido);

        if (recheioPedido.getRecheioExclusivo() != null) {
            RecheioPedido recheioSalvo = gateway.save(recheioPedido);
            return gateway.buscarRecheioPedidoExclusivoPorId(recheioSalvo.getId());
        }
        recheioPedido.setId(id);
        RecheioPedido recheioSalvo = gateway.save(recheioPedido);
        return gateway.buscarRecheioPedidoUnitariosPorId(recheioSalvo.getId());
    }

    @Cacheable(cacheNames = "recheiosPedido:porId", key = "#id")
    public RecheioPedidoProjection buscarRecheioPedidoPorId(Integer id) {
        if (!gateway.existsById(id)) {
            throw new EntidadeNaoEncontradaException("Recheio do pedido com id %d não encontrado".formatted(id));
        }
        RecheioPedido recheio = gateway.findById(id);
        verificarCampos(recheio);
        if (recheio.getRecheioExclusivo() != null) {
            return gateway.buscarRecheioPedidoExclusivoPorId(recheio.getId());
        }
        return gateway.buscarRecheioPedidoUnitariosPorId(recheio.getId());
    }

    @Cacheable(cacheNames = "recheiosPedido")
    public List<RecheioPedidoProjection> listarRecheiosPedido() {
        return gateway.listarRecheiosPedido();
    }

    public void deletarRecheioPedido(Integer id) {
        RecheioPedido recheio = gateway.findById(id);

        recheio.setAtivo(false);
        gateway.save(recheio);
    }

    private void verificarCampos(RecheioPedido recheioPedido) {
        if (recheioPedido.getRecheioUnitarioId1() != null && recheioPedido.getRecheioUnitarioId2() != null && recheioPedido.getRecheioExclusivo() != null) {
            throw new EntidadeImprocessavelException("Requisição apenas pode ter o recheio exclusivo nulo ou recheios unitarios nulos");
        } else if (
                recheioPedido.getRecheioUnitarioId1() == null && recheioPedido.getRecheioUnitarioId2() != null ||
                        recheioPedido.getRecheioUnitarioId1() != null && recheioPedido.getRecheioUnitarioId2() == null
        ) {
            throw new EntidadeImprocessavelException("Requisição apenas pode ter o recheio exclusivo nulo ou recheios unitarios nulos");
        }
    }
}
