package com.carambolos.carambolosapi.application.usecases;

import com.carambolos.carambolosapi.application.gateways.AdicionalDecoracaoGateway;
import com.carambolos.carambolosapi.application.gateways.DecoracaoGateway;
import com.carambolos.carambolosapi.application.gateways.StorageGateway;
import com.carambolos.carambolosapi.domain.entity.AdicionalDecoracao;
import com.carambolos.carambolosapi.domain.entity.AdicionalDecoracaoSummary;
import com.carambolos.carambolosapi.domain.entity.Decoracao;
import com.carambolos.carambolosapi.domain.entity.ImagemDecoracao;
import com.carambolos.carambolosapi.infrastructure.web.request.DecoracaoRequestDTO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public class DecoracaoUseCase {
    private final DecoracaoGateway decoracaoGateway;
    private final AdicionalDecoracaoGateway adicionalDecoracaoGateway;
    private final StorageGateway storageGateway;

    public DecoracaoUseCase(DecoracaoGateway decoracaoGateway, AdicionalDecoracaoGateway adicionalDecoracaoGateway, StorageGateway storageGateway) {
        this.decoracaoGateway = decoracaoGateway;
        this.adicionalDecoracaoGateway = adicionalDecoracaoGateway;
        this.storageGateway = storageGateway;
    }

    public Decoracao cadastrar(String nome, String observacao, String categoria, List<Integer> adicionais, MultipartFile[] arquivos) {
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

        Decoracao decoracaoSalva = decoracaoGateway.save(decoracao);
        salvarAdicionalDecoracao(decoracaoSalva, adicionais);

        return decoracaoSalva;
    }

    public Decoracao atualizar(
            Integer id,
            String nome,
            String observacao,
            String categoria,
            List<Integer> adicionais,
            MultipartFile[] arquivos
    ) {
        Decoracao decoracao = decoracaoGateway.findById(id);
        decoracao.setNome(nome);
        decoracao.setObservacao(observacao);
        decoracao.setCategoria(categoria);

        List<ImagemDecoracao> imagens = new ArrayList<>();

        for (MultipartFile arquivo : arquivos) {
            String url = storageGateway.upload(arquivo);
            ImagemDecoracao imagem = new ImagemDecoracao();
            imagem.setUrl(url);
            imagem.setDecoracao(decoracao);
            imagens.add(imagem);
        }
        decoracao.setImagens(imagens);

        try {
            salvarAdicionalDecoracao(decoracao, adicionais);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar adicionais de decoração: " + e.getMessage());
        }

        return decoracaoGateway.save(decoracao);
    }

    @Cacheable(cacheNames = "decoracoes:ativas")
    public List<Decoracao> listarAtivas() {
        return decoracaoGateway.findByIsAtivoTrue();
    }

    @Cacheable(cacheNames = "decoracoes:featured")
    public List<Decoracao> listarAtivasComCategoria() {
        return decoracaoGateway.findByIsAtivoTrueAndCategoriaIsNotNull();
    }

    @Cacheable(cacheNames = "decoracoes:porId", key = "#id")
    public Decoracao buscarPorId(Integer id) {
        return decoracaoGateway.findById(id);
    }

    public void desativar(Integer id) {
        Decoracao decoracao = decoracaoGateway.findById(id);
        decoracao.setIsAtivo(false);
        decoracaoGateway.save(decoracao);
    }

    public void reativar(Integer id) {
        Decoracao decoracao = decoracaoGateway.findById(id);
        decoracao.setIsAtivo(true);
        decoracaoGateway.save(decoracao);
    }

    private List<AdicionalDecoracao> salvarAdicionalDecoracao(Decoracao decoracao, List<Integer> adicionaisIds) {
        if (adicionaisIds == null || adicionaisIds.isEmpty()) {
            return List.of();
        }

        return adicionaisIds.stream()
                .map(adicionalId -> adicionalDecoracaoGateway.salvar(decoracao.getId(), adicionalId))
                .toList();
    }
}
