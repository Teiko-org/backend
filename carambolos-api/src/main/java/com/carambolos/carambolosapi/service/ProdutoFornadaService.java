package com.carambolos.carambolosapi.service;
import com.carambolos.carambolosapi.controller.request.ProdutoFornadaRequestDTO;
import com.carambolos.carambolosapi.exception.EntidadeJaExisteException;
import com.carambolos.carambolosapi.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.model.ProdutoFornada;
import com.carambolos.carambolosapi.repository.ProdutoFornadaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoFornadaService {

    private final ProdutoFornadaRepository produtoFornadaRepository;

    public ProdutoFornadaService(ProdutoFornadaRepository produtoFornadaRepository) {
        this.produtoFornadaRepository = produtoFornadaRepository;
    }

    public ProdutoFornada criarProdutoFornada(ProdutoFornadaRequestDTO request) {
        if (produtoFornadaRepository.existsByProduto(request.produto())) {
            throw new EntidadeJaExisteException("Já existe um ProdutoFornada com o nome informado: " + request.produto());
        }

        ProdutoFornada produtoFornada = request.toEntity();
        return produtoFornadaRepository.save(produtoFornada);
    }

    public List<ProdutoFornada> listarProdutosFornada(List<String> categorias) {
        List<ProdutoFornada> produtos;
        if (!categorias.isEmpty()) {
            produtos = produtoFornadaRepository.findByCategoriaIn(categorias);
        } else {
            produtos = produtoFornadaRepository.findAll();
        }
        return produtos;
    }

    public ProdutoFornada buscarProdutoFornada(Integer id) {
        return produtoFornadaRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("ProdutoFornada com ID " + id + " não encontrado."));
    }

    public void excluirProdutoFornada(Integer id) {
        if (!produtoFornadaRepository.existsById(id)) {
            throw new EntidadeNaoEncontradaException("Não é possível excluir. ProdutoFornada com ID " + id + " não existe.");
        }
        produtoFornadaRepository.deleteById(id);
    }

    public ProdutoFornada atualizarProdutoFornada(Integer id, ProdutoFornadaRequestDTO request) {
        ProdutoFornada produtoFornada = produtoFornadaRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("ProdutoFornada com ID " + id + " não encontrado para atualização."));

        produtoFornada.setProduto(request.produto());
        produtoFornada.setDescricao(request.descricao());
        produtoFornada.setValor(request.valor());
        return produtoFornadaRepository.save(produtoFornada);
    }
}
