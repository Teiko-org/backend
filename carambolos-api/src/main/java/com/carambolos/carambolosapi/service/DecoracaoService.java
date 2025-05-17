package com.carambolos.carambolosapi.service;

import com.carambolos.carambolosapi.controller.request.DecoracaoRequestDTO;
import com.carambolos.carambolosapi.controller.response.DecoracaoResponseDTO;
import com.carambolos.carambolosapi.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.model.Decoracao;
import com.carambolos.carambolosapi.repository.DecoracaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DecoracaoService {

    @Autowired
    private DecoracaoRepository decoracaoRepository;

    public DecoracaoResponseDTO cadastrar(DecoracaoRequestDTO request) {
        Decoracao decoracao = new Decoracao();
        decoracao.setImagemReferencia(request.imagemReferencia());
        decoracao.setObservacao(request.observacao());
        decoracao.setIsAtivo(true);

        Decoracao salva = decoracaoRepository.save(decoracao);
        return DecoracaoResponseDTO.fromEntity(salva);
    }

    public List<DecoracaoResponseDTO> listarAtivas() {
        return decoracaoRepository.findByIsAtivoTrue()
                .stream()
                .map(DecoracaoResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public DecoracaoResponseDTO buscarPorId(Integer id) {
        Decoracao decoracao = decoracaoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Decoração não encontrada"));
        return DecoracaoResponseDTO.fromEntity(decoracao);
    }

    public void desativar(Integer id) {
        Decoracao decoracao = decoracaoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Decoração não encontrada"));
        decoracao.setIsAtivo(false);
        decoracaoRepository.save(decoracao);
    }

    public void reativar(Integer id) {
        Decoracao decoracao = decoracaoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Decoração não encontrada"));
        decoracao.setIsAtivo(true);
        decoracaoRepository.save(decoracao);
    }

    public DecoracaoResponseDTO atualizar(Integer id, DecoracaoRequestDTO request) {
        Decoracao decoracao = decoracaoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Decoração não encontrada"));

        decoracao.setImagemReferencia(request.imagemReferencia());
        decoracao.setObservacao(request.observacao());

        return DecoracaoResponseDTO.fromEntity(decoracaoRepository.save(decoracao));
    }
}
