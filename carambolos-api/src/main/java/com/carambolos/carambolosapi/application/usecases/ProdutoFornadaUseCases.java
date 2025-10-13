package com.carambolos.carambolosapi.application.usecases;

import com.carambolos.carambolosapi.infrastructure.web.request.ProdutoFornadaRequestDTO;
import com.carambolos.carambolosapi.application.exception.EntidadeJaExisteException;
import com.carambolos.carambolosapi.application.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.domain.entity.ImagemProdutoFornada;
import com.carambolos.carambolosapi.domain.entity.ProdutoFornada;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.ProdutoFornadaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProdutoFornadaUseCases {

    private final ProdutoFornadaRepository produtoFornadaRepository;

    @Autowired
    private AzureStorageService azureStorageService;

    public ProdutoFornadaUseCases(ProdutoFornadaRepository produtoFornadaRepository) {
        this.produtoFornadaRepository = produtoFornadaRepository;
    }

    public ProdutoFornada criarProdutoFornada(String produto, String descricao, Double valor, String categoria, MultipartFile[] arquivos) {
        ProdutoFornada produtoFornada = new ProdutoFornada();
        produtoFornada.setProduto(produto);
        produtoFornada.setDescricao(descricao);
        produtoFornada.setValor(valor);
        produtoFornada.setCategoria(categoria);
        produtoFornada.setIsAtivo(true);

        List<ImagemProdutoFornada> imagens = new ArrayList<>();
        for (MultipartFile arquivo : arquivos) {
            String url = azureStorageService.upload(arquivo);
            ImagemProdutoFornada imagem = new ImagemProdutoFornada();
            imagem.setUrl(url);
            imagem.setProdutoFornada(produtoFornada);
            imagens.add(imagem);
        }
        produtoFornada.setImagens(imagens);

        return produtoFornadaRepository.save(produtoFornada);
    }

    public List<ProdutoFornada> listarProdutosFornada(List<String> categorias) {
        List<ProdutoFornada> produtos;
        if (!categorias.isEmpty()) {
            produtos = produtoFornadaRepository.findByCategoriaIn(categorias).stream().filter(ProdutoFornada::isAtivo).toList();
        } else {
            produtos = produtoFornadaRepository.findAll().stream().filter(ProdutoFornada::isAtivo).toList();
        }
        return produtos;
    }

    public ProdutoFornada buscarProdutoFornada(Integer id) {
        return produtoFornadaRepository.findById(id)
                .filter(ProdutoFornada::isAtivo)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("ProdutoFornada com ID " + id + " não encontrado."));
    }

    public void excluirProdutoFornada(Integer id) {
        ProdutoFornada produtoFornada = buscarProdutoFornada(id);
        produtoFornada.setAtivo(false);
        produtoFornadaRepository.save(produtoFornada);
    }

    public ProdutoFornada atualizarProdutoFornada(Integer id, ProdutoFornadaRequestDTO request) {
        if (produtoFornadaRepository.existsByProdutoAndIsAtivoTrueAndIdNot(request.produto(), id)) {
            throw new EntidadeJaExisteException("Já existe um ProdutoFornada ativo com o nome informado: " + request.produto());
        }

        ProdutoFornada produtoFornada = buscarProdutoFornada(id);

        produtoFornada.setProduto(request.produto());
        produtoFornada.setDescricao(request.descricao());
        produtoFornada.setValor(request.valor());
        return produtoFornadaRepository.save(produtoFornada);
    }

    public void atualizarStatusProdutoFornada(Boolean status, Integer id) {
        Integer statusInt = status ? 1 : 0;
        if (produtoFornadaRepository.existsById(id)) {
            produtoFornadaRepository.updateStatus(statusInt, id);
        } else {
            throw new EntidadeNaoEncontradaException("Produto fornada com id %d não encontrado".formatted(id));
        }
    }
}


