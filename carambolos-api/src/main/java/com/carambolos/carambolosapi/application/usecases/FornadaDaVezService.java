package com.carambolos.carambolosapi.application.usecases;

import com.carambolos.carambolosapi.infrastructure.web.request.FornadaDaVezRequestDTO;
import com.carambolos.carambolosapi.infrastructure.web.request.FornadaDaVezUpdateRequestDTO;
import com.carambolos.carambolosapi.application.exception.EntidadeImprocessavelException;
import com.carambolos.carambolosapi.application.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.Fornada;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.FornadaDaVez;
import com.carambolos.carambolosapi.domain.entity.ProdutoFornada;
import com.carambolos.carambolosapi.domain.projection.ProdutoFornadaDaVezProjection;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.FornadaDaVezRepository;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.FornadaRepository;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.ProdutoFornadaRepository;
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

        // Verificar se já existe este produto nesta fornada
        FornadaDaVez fornadaDaVezExistente = fornadaDaVezRepository
                .findByFornadaAndProdutoFornadaAndIsAtivoTrue(request.fornadaId(), request.produtoFornadaId());

        if (fornadaDaVezExistente != null) {
            // Se já existe, somar a quantidade
            int novaQuantidade = fornadaDaVezExistente.getQuantidade() + request.quantidade();
            fornadaDaVezExistente.setQuantidade(novaQuantidade);
            
            System.out.println("✅ PRODUTO JÁ EXISTE NA FORNADA: Produto " + request.produtoFornadaId() +
                    " | Quantidade anterior: " + (fornadaDaVezExistente.getQuantidade() - request.quantidade()) +
                    " | Adicionando: " + request.quantidade() +
                    " | Nova quantidade total: " + novaQuantidade);
            
            return fornadaDaVezRepository.save(fornadaDaVezExistente);
        } else {
            // Se não existe, criar novo registro
            FornadaDaVez fornadaDaVez = request.toEntity(request);
            
            System.out.println("✅ NOVO PRODUTO NA FORNADA: Produto " + request.produtoFornadaId() +
                    " | Quantidade: " + request.quantidade());
            
            return fornadaDaVezRepository.save(fornadaDaVez);
        }
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

    public List<ProdutoFornadaDaVezProjection> buscarProdutosPorFornadaId(Integer fornadaId) {
        // Para listagem histórica/KPI, aceitar fornadas encerradas
        return fornadaDaVezRepository.findResumoKpiByFornadaId(fornadaId);
    }
}