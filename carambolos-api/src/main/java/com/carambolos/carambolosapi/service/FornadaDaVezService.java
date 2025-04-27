package com.carambolos.carambolosapi.service;

import com.carambolos.carambolosapi.controller.dto.FornadaDaVezRequestDTO;
import com.carambolos.carambolosapi.controller.dto.FornadaDaVezUpdateRequestDTO;
import com.carambolos.carambolosapi.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.model.Fornada;
import com.carambolos.carambolosapi.model.FornadaDaVez;
import com.carambolos.carambolosapi.model.ProdutoFornada;
import com.carambolos.carambolosapi.repository.FornadaDaVezRepository;
import com.carambolos.carambolosapi.repository.FornadaRepository;
import com.carambolos.carambolosapi.repository.ProdutoFornadaRepository;
import org.springframework.stereotype.Service;

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
        ProdutoFornada produto = produtoFornadaRepository.findById(request.produtoFornadaId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("ProdutoFornada com ID " + request.produtoFornadaId() + " não encontrado."));

        Fornada fornada = fornadaRepository.findById(request.fornadaId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Fornada com ID " + request.fornadaId() + " não encontrada."));

        FornadaDaVez fornadaDaVez = request.toEntity(fornada, produto);

        return fornadaDaVezRepository.save(fornadaDaVez);
    }

    public List<FornadaDaVez> listarFornadasDaVez() {
        return fornadaDaVezRepository.findAll();
    }

    public FornadaDaVez buscarFornadaDaVez(Integer id) {
        return fornadaDaVezRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("FornadaDaVez com ID " + id + " não encontrada."));
    }

    public void excluirFornadaDaVez(Integer id) {
        if (!fornadaDaVezRepository.existsById(id)) {
            throw new EntidadeNaoEncontradaException("FornadaDaVez com ID " + id + " não existe para exclusão.");
        }
        fornadaDaVezRepository.deleteById(id);
    }

    public FornadaDaVez atualizarQuantidade(Integer id, FornadaDaVezUpdateRequestDTO request) {
        FornadaDaVez fornadaDaVez = fornadaDaVezRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("FornadaDaVez com ID " + id + " não encontrada para atualização."));

        fornadaDaVez.setQuantidade(request.quantidade());

        return fornadaDaVezRepository.save(fornadaDaVez);
    }
}