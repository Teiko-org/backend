package com.carambolos.carambolosapi.application.usecases;

import com.carambolos.carambolosapi.infrastructure.web.request.ProdutoFornadaRequestDTO;
import com.carambolos.carambolosapi.application.exception.EntidadeJaExisteException;
import com.carambolos.carambolosapi.application.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.domain.entity.ImagemProdutoFornada;
import com.carambolos.carambolosapi.domain.entity.ProdutoFornada;
import com.carambolos.carambolosapi.application.gateways.ProdutoFornadaGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProdutoFornadaUseCases {

    private final ProdutoFornadaGateway produtoFornadaGateway;

    @Autowired
    private AzureStorageService azureStorageService;

    public ProdutoFornadaUseCases(ProdutoFornadaGateway produtoFornadaGateway) {
        this.produtoFornadaGateway = produtoFornadaGateway;
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

        return produtoFornadaGateway.save(produtoFornada);
    }

    public Page<ProdutoFornada> listarProdutosFornada(Pageable pageable, List<String> categorias) {
        if (categorias != null && !categorias.isEmpty()) {
            return produtoFornadaGateway.findAtivosByCategoriaIn(pageable, categorias);
        }
        return produtoFornadaGateway.findAtivos(pageable);
    }

    public ProdutoFornada buscarProdutoFornada(Integer id) {
        return produtoFornadaGateway.findById(id)
                .filter(ProdutoFornada::isAtivo)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("ProdutoFornada com ID " + id + " não encontrado."));
    }

    public void excluirProdutoFornada(Integer id) {
        ProdutoFornada produtoFornada = buscarProdutoFornada(id);
        produtoFornada.setAtivo(false);
        produtoFornadaGateway.save(produtoFornada);
    }

    public ProdutoFornada atualizarProdutoFornada(Integer id, ProdutoFornadaRequestDTO request) {
        if (produtoFornadaGateway.existsByProdutoAndIsAtivoTrueAndIdNot(request.produto(), id)) {
            throw new EntidadeJaExisteException("Já existe um ProdutoFornada ativo com o nome informado: " + request.produto());
        }

        ProdutoFornada produtoFornada = buscarProdutoFornada(id);

        produtoFornada.setProduto(request.produto());
        produtoFornada.setDescricao(request.descricao());
        produtoFornada.setValor(request.valor());
        return produtoFornadaGateway.save(produtoFornada);
    }

    public void atualizarStatusProdutoFornada(Boolean status, Integer id) {
        var exists = produtoFornadaGateway.findById(id).isPresent();
        if (exists) {
            produtoFornadaGateway.updateStatus(status, id);
        } else {
            throw new EntidadeNaoEncontradaException("Produto fornada com id %d não encontrado".formatted(id));
        }
    }
}


