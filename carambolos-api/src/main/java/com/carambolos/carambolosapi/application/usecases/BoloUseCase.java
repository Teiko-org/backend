package com.carambolos.carambolosapi.application.usecases;

import com.carambolos.carambolosapi.application.gateways.BoloGateway;
import com.carambolos.carambolosapi.application.gateways.CoberturaGateway;
import com.carambolos.carambolosapi.application.gateways.MassaGateway;
import com.carambolos.carambolosapi.application.gateways.RecheioPedidoGateway;
import com.carambolos.carambolosapi.domain.entity.Bolo;
import com.carambolos.carambolosapi.domain.projection.DetalheBoloProjection;
import com.carambolos.carambolosapi.application.exception.EntidadeJaExisteException;
import com.carambolos.carambolosapi.application.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BoloUseCase {
    private final CoberturaGateway coberturaGateway;
    private final MassaGateway massaGateway;
    private final RecheioPedidoGateway recheioPedidoGateway;
    private final BoloGateway boloGateway;
    private final DecoracaoRepository decoracaoRepository;

    public BoloUseCase(CoberturaGateway coberturaGateway,
                       MassaGateway massaGateway,
                       RecheioPedidoGateway recheioPedidoGateway,
                       BoloGateway boloGateway,
                       DecoracaoRepository decoracaoRepository
    ) {
        this.coberturaGateway = coberturaGateway;
        this.massaGateway = massaGateway;
        this.recheioPedidoGateway = recheioPedidoGateway;
        this.boloGateway = boloGateway;
        this.decoracaoRepository = decoracaoRepository;
    }


    public List<Bolo> listarBolos(List<String> categorias) {
        List<Bolo> boloEntities;

        if (!categorias.isEmpty()) {
            boloEntities = boloGateway.findByCategoriaIn(categorias);
        } else {
            boloEntities = boloGateway.findAll();
        }
        return boloEntities;
    }

    public List<DetalheBoloProjection> listarDetalhesBolos() {
        return boloGateway.listarDetalheBolo();
    }

    public Bolo buscarBoloPorId(Integer id) {
        return boloGateway.findById(id);
    }

    public Bolo cadastrarBolo(Bolo bolo) {
        // Para novos bolos, o ID será null ou 0, então não precisamos verificar se já existe
        if (bolo.getId() != null && bolo.getId() > 0) {
            boolean existeBoloAtivo = boloGateway.existsByIdAndIsAtivoTrue(bolo.getId());
            if (existeBoloAtivo) {
                throw new EntidadeJaExisteException("Esse bolo já existe no banco de dados.");
            }
        }

        if (!massaGateway.existsByIdAndIsAtivo(bolo.getMassa(), true)) {
            throw new EntidadeNaoEncontradaException("Essa massa não existe");
        }
        if (!recheioPedidoGateway.existsByIdAndIsAtivoTrue(bolo.getRecheioPedido())) {
            throw new EntidadeNaoEncontradaException("Esse recheio não existe");
        }
        if (!coberturaGateway.existsByIdAndIsAtivoTrue(bolo.getCobertura())) {
            throw new EntidadeNaoEncontradaException("Essa cobertura não existe");
        }
        if (bolo.getDecoracao() != null && !decoracaoRepository.existsByIdAndIsAtivoTrue(bolo.getDecoracao())) {
            throw new EntidadeNaoEncontradaException("Essa decoração não existe");
        }

        return boloGateway.save(bolo);
    }

    public Bolo atualizarBolo(Bolo bolo, Integer id) {
        boloGateway.findById(id);

        if (boloGateway.existsByIdAndIdNotAndIsAtivoTrue(id, bolo.getId())) {
            throw new EntidadeJaExisteException("Esse bolo já existe no banco de dados.");
        }

        if (!massaGateway.existsByIdAndIsAtivo(bolo.getMassa(), true)) {
            throw new EntidadeNaoEncontradaException("Essa massa não existe");
        }
        if (!recheioPedidoGateway.existsByIdAndIsAtivoTrue(bolo.getRecheioPedido())) {
            throw new EntidadeNaoEncontradaException("Esse recheio não existe");
        }
        if (!coberturaGateway.existsByIdAndIsAtivoTrue(bolo.getCobertura())) {
            throw new EntidadeNaoEncontradaException("Essa cobertura não existe");
        }
        if (bolo.getDecoracao() != null && !decoracaoRepository.existsByIdAndIsAtivoTrue(bolo.getDecoracao())) {
            throw new EntidadeNaoEncontradaException("Essa decoração não existe");
        }

        bolo.setId(id);
        return boloGateway.save(bolo);
    }

    public void atualizarStatusBolo(Boolean status, Integer id) {
        Integer statusInt = status ? 1 : 0;

        if (boloGateway.existsById(id)) {
            boloGateway.atualizarStatusBolo(statusInt, id);
        } else {
            throw new EntidadeNaoEncontradaException("Bolo com id %d não encontrado".formatted(id));
        }
    }

    public void deletarBolo(Integer id) {
        Bolo bolo = boloGateway.findById(id);
        bolo.setAtivo(false);
        boloGateway.save(bolo);
    }
}
