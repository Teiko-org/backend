package com.carambolos.carambolosapi.infrastructure.gateways.impl;

import com.carambolos.carambolosapi.application.gateways.ProdutoFornadaGateway;
import com.carambolos.carambolosapi.domain.entity.ProdutoFornada;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.FornadasMapper;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.ProdutoFornadaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public class ProdutoFornadaGatewayImpl implements ProdutoFornadaGateway {
    private final ProdutoFornadaRepository repository;

    public ProdutoFornadaGatewayImpl(ProdutoFornadaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<ProdutoFornada> findById(Integer id) {
        return repository.findById(id).map(FornadasMapper::toDomain);
    }

    @Override
    public ProdutoFornada save(ProdutoFornada produto) {
        var saved = repository.save(FornadasMapper.toEntity(produto));
        return FornadasMapper.toDomain(saved);
    }

    @Override
    public List<ProdutoFornada> findAll() {
        return repository.findAll().stream().map(FornadasMapper::toDomain).toList();
    }

    @Override
    public List<ProdutoFornada> findByCategoriaIn(List<String> categorias) {
        return repository.findByCategoriaIn(categorias).stream().map(FornadasMapper::toDomain).toList();
    }

    @Override
    public boolean existsByProdutoAndIsAtivoTrue(String produto) {
        return repository.existsByProdutoAndIsAtivoTrue(produto);
    }

    @Override
    public boolean existsByProdutoAndIsAtivoTrueAndIdNot(String produto, Integer id) {
        return repository.existsByProdutoAndIsAtivoTrueAndIdNot(produto, id);
    }

    @Override
    public void updateStatus(Boolean status, Integer id) {
        repository.updateStatus(Boolean.TRUE.equals(status) ? 1 : 0, id);
    }

    @Override
    public Page<ProdutoFornada> findAtivos(Pageable pageable) {
        return repository.findByIsAtivoTrue(pageable).map(FornadasMapper::toDomain);
    }

    @Override
    public Page<ProdutoFornada> findAtivosByCategoriaIn(Pageable pageable, List<String> categorias) {
        return repository.findByCategoriaInAndIsAtivoTrue(pageable, categorias).map(FornadasMapper::toDomain);
    }
}


