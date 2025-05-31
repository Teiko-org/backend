package com.carambolos.carambolosapi.service;

import com.carambolos.carambolosapi.controller.request.ProdutoFornadaRequestDTO;
import com.carambolos.carambolosapi.exception.EntidadeJaExisteException;
import com.carambolos.carambolosapi.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.model.ProdutoFornada;
import com.carambolos.carambolosapi.repository.ProdutoFornadaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class ProdutoFornadaServiceTest {

    private ProdutoFornadaRepository produtoFornadaRepository;
    private ProdutoFornadaService produtoFornadaService;

    @BeforeEach
    void setUp() {
        produtoFornadaRepository = mock(ProdutoFornadaRepository.class);
        produtoFornadaService = new ProdutoFornadaService(produtoFornadaRepository);
    }

    @Test
    @DisplayName("Deve criar ProdutoFornada com sucesso")
    void deveCriarProdutoFornadaComSucesso() {
        ProdutoFornadaRequestDTO request = new ProdutoFornadaRequestDTO(null, "Brigadeiro", "Doce tradicional", 2.5, "Doces");
        ProdutoFornada produto = request.toEntity();

        when(produtoFornadaRepository.existsByProdutoAndIsAtivoTrue("Brigadeiro")).thenReturn(false);
        when(produtoFornadaRepository.save(any())).thenReturn(produto);

        ProdutoFornada result = produtoFornadaService.criarProdutoFornada(request);

        assertThat(result).isEqualTo(produto);
        verify(produtoFornadaRepository).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar ProdutoFornada duplicado")
    void deveLancarExcecaoAoCriarProdutoDuplicado() {
        ProdutoFornadaRequestDTO request = new ProdutoFornadaRequestDTO(null, "Brigadeiro", "Doce tradicional", 2.5, "Doces");

        when(produtoFornadaRepository.existsByProdutoAndIsAtivoTrue("Brigadeiro")).thenReturn(true);

        assertThatThrownBy(() -> produtoFornadaService.criarProdutoFornada(request))
                .isInstanceOf(EntidadeJaExisteException.class)
                .hasMessageContaining("Brigadeiro");
    }

    @Test
    @DisplayName("Deve listar produtos por categorias")
    void deveListarProdutosPorCategorias() {
        ProdutoFornada ativo = new ProdutoFornada();
        ativo.setAtivo(true);

        when(produtoFornadaRepository.findByCategoriaIn(List.of("Doces"))).thenReturn(List.of(ativo));

        List<ProdutoFornada> result = produtoFornadaService.listarProdutosFornada(List.of("Doces"));

        assertThat(result).containsExactly(ativo);
    }

    @Test
    @DisplayName("Deve listar todos os produtos se categorias estiver vazia")
    void deveListarTodosProdutosQuandoCategoriasEstiverVazia() {
        ProdutoFornada ativo = new ProdutoFornada();
        ativo.setAtivo(true);

        when(produtoFornadaRepository.findAll()).thenReturn(List.of(ativo));

        List<ProdutoFornada> result = produtoFornadaService.listarProdutosFornada(List.of());

        assertThat(result).containsExactly(ativo);
    }

    @Test
    @DisplayName("Deve buscar ProdutoFornada por ID com sucesso")
    void deveBuscarProdutoFornadaPorIdComSucesso() {
        ProdutoFornada produto = new ProdutoFornada();
        produto.setAtivo(true);

        when(produtoFornadaRepository.findById(1)).thenReturn(Optional.of(produto));

        ProdutoFornada result = produtoFornadaService.buscarProdutoFornada(1);

        assertThat(result).isEqualTo(produto);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar ProdutoFornada inativo ou inexistente")
    void deveLancarExcecaoAoBuscarProdutoInativoOuInexistente() {
        when(produtoFornadaRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> produtoFornadaService.buscarProdutoFornada(1))
                .isInstanceOf(EntidadeNaoEncontradaException.class);
    }

    @Test
    @DisplayName("Deve excluir ProdutoFornada com sucesso")
    void deveExcluirProdutoFornadaComSucesso() {
        ProdutoFornada produto = new ProdutoFornada();
        produto.setAtivo(true);

        when(produtoFornadaRepository.findById(1)).thenReturn(Optional.of(produto));

        produtoFornadaService.excluirProdutoFornada(1);

        assertThat(produto.isAtivo()).isFalse();
        verify(produtoFornadaRepository).save(produto);
    }

    @Test
    @DisplayName("Deve atualizar ProdutoFornada com sucesso")
    void deveAtualizarProdutoFornadaComSucesso() {
        ProdutoFornada produto = new ProdutoFornada();
        produto.setAtivo(true);

        ProdutoFornadaRequestDTO request = new ProdutoFornadaRequestDTO(null, "Cupcake", "Descrição", 5.0, "Categoria");

        when(produtoFornadaRepository.findById(1)).thenReturn(Optional.of(produto));
        when(produtoFornadaRepository.existsByProdutoAndIsAtivoTrueAndIdNot("Cupcake", 1)).thenReturn(false);
        when(produtoFornadaRepository.save(any())).thenReturn(produto);

        ProdutoFornada result = produtoFornadaService.atualizarProdutoFornada(1, request);

        assertThat(result.getProduto()).isEqualTo("Cupcake");
        assertThat(result.getDescricao()).isEqualTo("Descrição");
        assertThat(result.getValor()).isEqualTo(5.0);
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar ProdutoFornada para nome duplicado")
    void deveLancarExcecaoAoAtualizarProdutoParaNomeDuplicado() {
        ProdutoFornada produto = new ProdutoFornada();
        produto.setAtivo(true);

        ProdutoFornadaRequestDTO request = new ProdutoFornadaRequestDTO(null, "Cupcake", "Descrição", 5.0, "Categoria");

        when(produtoFornadaRepository.findById(1)).thenReturn(Optional.of(produto));
        when(produtoFornadaRepository.existsByProdutoAndIsAtivoTrueAndIdNot("Cupcake", 1)).thenReturn(true);

        assertThatThrownBy(() -> produtoFornadaService.atualizarProdutoFornada(1, request))
                .isInstanceOf(EntidadeJaExisteException.class)
                .hasMessageContaining("Cupcake");
    }
}
