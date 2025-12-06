package com.carambolos.carambolosapi.application.usecases;

import com.carambolos.carambolosapi.application.exception.EntidadeImprocessavelException;
import com.carambolos.carambolosapi.application.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.application.gateways.FornadaDaVezGateway;
import com.carambolos.carambolosapi.application.gateways.FornadaGateway;
import com.carambolos.carambolosapi.application.gateways.ProdutoFornadaGateway;
import com.carambolos.carambolosapi.infrastructure.persistence.projection.ProdutoFornadaDaVezProjection;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.FornadaDaVez;
import com.carambolos.carambolosapi.infrastructure.web.request.FornadaDaVezRequestDTO;
import com.carambolos.carambolosapi.infrastructure.web.request.FornadaDaVezUpdateRequestDTO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FornadaDaVezUseCases {

    private final FornadaDaVezGateway fornadaDaVezGateway;
    private final ProdutoFornadaGateway produtoFornadaGateway;
    private final FornadaGateway fornadaGateway;

    public FornadaDaVezUseCases(
            FornadaDaVezGateway fornadaDaVezGateway,
            ProdutoFornadaGateway produtoFornadaGateway,
            FornadaGateway fornadaGateway) {
        this.fornadaDaVezGateway = fornadaDaVezGateway;
        this.produtoFornadaGateway = produtoFornadaGateway;
        this.fornadaGateway = fornadaGateway;
    }

    public FornadaDaVez criarFornadaDaVez(FornadaDaVezRequestDTO request) {
        produtoFornadaGateway.findById(request.produtoFornadaId())
                .filter(produto -> Boolean.TRUE.equals(produto.isAtivo()))
                .orElseThrow(() -> new EntidadeNaoEncontradaException("ProdutoFornada com ID " + request.produtoFornadaId() + " não encontrado."));

        fornadaGateway.findById(request.fornadaId())
                .filter(fornada -> Boolean.TRUE.equals(fornada.getAtivo()))
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Fornada com ID " + request.fornadaId() + " não encontrada."));

        return fornadaDaVezGateway.saveSummingIfExists(
                request.fornadaId(), request.produtoFornadaId(), request.quantidade()
        );
    }

    @Cacheable(cacheNames = "fornadasDaVez:ativas")
    public List<FornadaDaVez> listarFornadasDaVez() {
        return fornadaDaVezGateway.findAll().stream().filter(fdv -> fdv.isAtivo()).toList();
    }

    @Cacheable(cacheNames = "fornadasDaVez:porId", key = "#id")
    public FornadaDaVez buscarFornadaDaVez(Integer id) {
        return fornadaDaVezGateway.findById(id)
                .filter(fdv -> fdv.isAtivo())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("FornadaDaVez com ID " + id + " não encontrada."));
    }

    public void excluirFornadaDaVez(Integer id) {
        FornadaDaVez fornadaDaVez = buscarFornadaDaVez(id);
        fornadaDaVez.setAtivo(false);
        fornadaDaVezGateway.save(fornadaDaVez);
    }

    public FornadaDaVez atualizarQuantidade(Integer id, FornadaDaVezUpdateRequestDTO request) {
        FornadaDaVez fornadaDaVez = buscarFornadaDaVez(id);

        if (request.quantidade() <= 0) {
            throw new EntidadeImprocessavelException("Quantidade deve ser maior que zero.");
        }

        fornadaDaVez.setQuantidade(request.quantidade());

        return fornadaDaVezGateway.save(fornadaDaVez);
    }

    @Cacheable(cacheNames = "fornadasDaVez:produtosPorPeriodo", key = "#dataInicio.toString() + ':' + #dataFim.toString()")
    public List<ProdutoFornadaDaVezProjection> buscarProdutosFornadaDaVez(LocalDate dataInicio, LocalDate dataFim) {
        return fornadaDaVezGateway.findProductsByFornada(dataInicio, dataFim);
    }

    @Cacheable(cacheNames = "fornadasDaVez:produtosPorFornadaId", key = "#fornadaId")
    public List<ProdutoFornadaDaVezProjection> buscarProdutosPorFornadaId(Integer fornadaId) {
        return fornadaDaVezGateway.findResumoKpiByFornadaId(fornadaId);
    }
}