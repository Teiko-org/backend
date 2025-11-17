package com.carambolos.carambolosapi.application.usecases;

import com.carambolos.carambolosapi.application.gateways.DecoracaoGateway;
import com.carambolos.carambolosapi.application.gateways.StorageGateway;
import com.carambolos.carambolosapi.domain.entity.Decoracao;
import com.carambolos.carambolosapi.domain.entity.ImagemDecoracao;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.ImagemDecoracaoEntity;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.DecoracaoEntity;
import com.carambolos.carambolosapi.infrastructure.web.response.DecoracaoResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DecoracaoUseCase {
    private final DecoracaoGateway decoracaoGateway;
    private final StorageGateway storageGateway;

    public DecoracaoUseCase(DecoracaoGateway decoracaoGateway, StorageGateway storageGateway) {
        this.decoracaoGateway = decoracaoGateway;
        this.storageGateway = storageGateway;
    }

    public Decoracao cadastrar(String nome, String observacao, String categoria, MultipartFile[] arquivos) {
        Decoracao decoracao = new Decoracao();
        decoracao.setNome(nome);
        decoracao.setObservacao(observacao);
        decoracao.setCategoria(categoria);
        decoracao.setIsAtivo(true);

        List<ImagemDecoracao> imagens = new ArrayList<>();

        for (MultipartFile arquivo : arquivos) {
            String url = storageGateway.upload(arquivo);
            ImagemDecoracao imagem = new ImagemDecoracao();
            imagem.setUrl(url);
            imagem.setDecoracao(decoracao);
            imagens.add(imagem);
        }

        decoracao.setImagens(imagens);
        return decoracaoGateway.save(decoracao);
    }

    public List<Decoracao> listarAtivas() {
        return decoracaoGateway.findByIsAtivoTrue();
    }

    public List<Decoracao> listarAtivasComCategoria() {
        return decoracaoRepository.findByIsAtivoTrueAndCategoriaIsNotNull()
                .stream()
                .map(DecoracaoResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
