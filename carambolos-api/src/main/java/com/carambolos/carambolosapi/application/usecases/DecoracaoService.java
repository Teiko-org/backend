package com.carambolos.carambolosapi.application.usecases;

import com.carambolos.carambolosapi.infrastructure.web.request.DecoracaoRequestDTO;
import com.carambolos.carambolosapi.infrastructure.web.response.DecoracaoResponseDTO;
import com.carambolos.carambolosapi.application.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.domain.entity.Decoracao;
import com.carambolos.carambolosapi.domain.entity.ImagemDecoracao;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.DecoracaoRepository;
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

    public DecoracaoResponseDTO cadastrar(String nome, String observacao, String categoria, MultipartFile[] arquivos) {
        Decoracao decoracao = new Decoracao();
        decoracao.setNome(nome);
        decoracao.setObservacao(observacao);
        decoracao.setCategoria(categoria);
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

    public List<DecoracaoResponseDTO> listarAtivasComCategoria() {
        return decoracaoRepository.findByIsAtivoTrueAndCategoriaIsNotNull()
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
        decoracao.setCategoria(request.categoria());

        return DecoracaoResponseDTO.fromEntity(decoracaoRepository.save(decoracao));
    }
}
