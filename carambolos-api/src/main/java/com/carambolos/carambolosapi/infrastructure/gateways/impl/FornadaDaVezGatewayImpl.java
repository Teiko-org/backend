package com.carambolos.carambolosapi.infrastructure.gateways.impl;

import com.carambolos.carambolosapi.application.gateways.FornadaDaVezGateway;
import com.carambolos.carambolosapi.infrastructure.persistence.projection.ProdutoFornadaDaVezProjection;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.FornadasMapper;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.FornadaDaVez;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.FornadaDaVezRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class FornadaDaVezGatewayImpl implements FornadaDaVezGateway {
    private final FornadaDaVezRepository repository;

    public FornadaDaVezGatewayImpl(FornadaDaVezRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<FornadaDaVez> findById(Integer id) {
        return repository.findById(id).map(FornadasMapper::toDomain);
    }

    @Override
    public FornadaDaVez save(FornadaDaVez fornadaDaVez) {
        var saved = repository.save(FornadasMapper.toEntity(fornadaDaVez));
        return FornadasMapper.toDomain(saved);
    }

    @Override
    public List<FornadaDaVez> findAll() {
        return repository.findAll().stream().map(FornadasMapper::toDomain).toList();
    }

    @Override
    public List<ProdutoFornadaDaVezProjection> findProductsByFornada(LocalDate dataInicio, LocalDate dataFim) {
        return repository.findProductsByFornada(dataInicio, dataFim);
    }

    @Override
    public List<ProdutoFornadaDaVezProjection> findByFornadaId(Integer fornadaId) {
        return repository.findByFornadaId(fornadaId);
    }

    @Override
    public List<ProdutoFornadaDaVezProjection> findResumoKpiByFornadaId(Integer fornadaId) {
        return repository.findResumoKpiByFornadaId(fornadaId);
    }

    @Override
    public FornadaDaVez findByFornadaAndProdutoFornadaAndIsAtivoTrue(Integer fornadaId, Integer produtoFornadaId) {
        return repository.findFirstByFornadaAndProdutoFornadaAndIsAtivoTrue(fornadaId, produtoFornadaId)
                .map(FornadasMapper::toDomain)
                .orElse(null);
    }

    @Override
    public FornadaDaVez saveSummingIfExists(Integer fornadaId, Integer produtoFornadaId, Integer quantidade) {
        // Validar quantidade antes de salvar
        if (quantidade == null || quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }
        
        var existenteOpt = repository.findFirstByFornadaAndProdutoFornadaAndIsAtivoTrue(fornadaId, produtoFornadaId);
        if (existenteOpt.isPresent()) {
            var existente = existenteOpt.get();
            // Substitui a quantidade ao invés de somar
            existente.setQuantidade(quantidade);
            var saved = repository.save(existente);
            return FornadasMapper.toDomain(saved);
        }
        var novo = new FornadaDaVez();
        novo.setFornada(fornadaId);
        novo.setProdutoFornada(produtoFornadaId);
        novo.setQuantidade(quantidade);
        novo.setAtivo(true);
        return repository.save(novo);
    }
}
