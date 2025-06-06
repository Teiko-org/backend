package com.carambolos.carambolosapi.service;

import com.carambolos.carambolosapi.controller.request.FornadaDaVezRequestDTO;
import com.carambolos.carambolosapi.controller.request.FornadaDaVezUpdateRequestDTO;
import com.carambolos.carambolosapi.exception.EntidadeImprocessavelException;
import com.carambolos.carambolosapi.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.model.Fornada;
import com.carambolos.carambolosapi.model.FornadaDaVez;
import com.carambolos.carambolosapi.model.ProdutoFornada;
import com.carambolos.carambolosapi.model.projection.ProdutoFornadaDaVezProjection;
import com.carambolos.carambolosapi.repository.FornadaDaVezRepository;
import com.carambolos.carambolosapi.repository.FornadaRepository;
import com.carambolos.carambolosapi.repository.ProdutoFornadaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FornadaDaVezService {

    private final FornadaDaVezRepository fornadaDaVezRepository;
    private final ProdutoFornadaRepository produtoFornadaRepository;
    private final FornadaRepository fornadaRepository;

    public FornadaDaVezService(
            FornadaDaVezRepository fornadaDaVezRepository,
            ProdutoFornadaRepository produtoFornadaRepository,
            FornadaRepository fornadaRepository) {
        this.fornadaDaVezRepository = fornadaDaVezRepository;
        this.produtoFornadaRepository = produtoFornadaRepository;
        this.fornadaRepository = fornadaRepository;
    }

    public FornadaDaVez criarFornadaDaVez(FornadaDaVezRequestDTO request) {
        produtoFornadaRepository.findById(request.produtoFornadaId())
                .filter(ProdutoFornada::isAtivo)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("ProdutoFornada com ID " + request.produtoFornadaId() + " não encontrado."));

        fornadaRepository.findById(request.fornadaId())
                .filter(Fornada::isAtivo)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Fornada com ID " + request.fornadaId() + " não encontrada."));

        FornadaDaVez fornadaDaVez = request.toEntity(request);

        return fornadaDaVezRepository.save(fornadaDaVez);
    }

    public List<FornadaDaVez> listarFornadasDaVez() {
        return fornadaDaVezRepository.findAll().stream().filter(FornadaDaVez::isAtivo).toList();
    }

    public FornadaDaVez buscarFornadaDaVez(Integer id) {
        return fornadaDaVezRepository.findById(id)
                .filter(FornadaDaVez::isAtivo)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("FornadaDaVez com ID " + id + " não encontrada."));
    }

    public void excluirFornadaDaVez(Integer id) {
        FornadaDaVez fornadaDaVez = buscarFornadaDaVez(id);
        fornadaDaVez.setAtivo(false);
        fornadaDaVezRepository.save(fornadaDaVez);
    }

    public FornadaDaVez atualizarQuantidade(Integer id, FornadaDaVezUpdateRequestDTO request) {
        FornadaDaVez fornadaDaVez = buscarFornadaDaVez(id);

        if (request.quantidade() <= 0) {
            throw new EntidadeImprocessavelException("Quantidade deve ser maior que zero.");
        }

        fornadaDaVez.setQuantidade(request.quantidade());

        return fornadaDaVezRepository.save(fornadaDaVez);
    }

    public List<ProdutoFornadaDaVezProjection> buscarProdutosFornadaDaVez(LocalDate dataInicio, LocalDate dataFim) {
        return fornadaDaVezRepository.findProductsByFornada(dataInicio, dataFim);
    }
}