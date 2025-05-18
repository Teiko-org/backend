package com.carambolos.carambolosapi.service;

import com.carambolos.carambolosapi.controller.request.DecoracaoRequestDTO;
import com.carambolos.carambolosapi.controller.response.DecoracaoResponseDTO;
import com.carambolos.carambolosapi.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.model.Decoracao;
import com.carambolos.carambolosapi.model.ImagemDecoracao;
import com.carambolos.carambolosapi.repository.DecoracaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DecoracaoService {

    @Autowired
    private DecoracaoRepository decoracaoRepository;

    @Autowired
    private AzureStorageService azureStorageService;

    public DecoracaoResponseDTO cadastrar(String observacao, MultipartFile[] arquivos) {
        Decoracao decoracao = new Decoracao();
        decoracao.setObservacao(observacao);
        decoracao.setIsAtivo(true);

        List<ImagemDecoracao> imagens = new ArrayList<>();

        for (MultipartFile arquivo : arquivos) {
            String url = azureStorageService.upload(arquivo);
            ImagemDecoracao imagem = new ImagemDecoracao();
            imagem.setUrl(url);
            imagem.setDecoracao(decoracao);
            imagens.add(imagem);
        }

        decoracao.setImagens(imagens);
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

        decoracao.setObservacao(request.observacao());

        return DecoracaoResponseDTO.fromEntity(decoracaoRepository.save(decoracao));
    }
}
