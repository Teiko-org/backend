package com.carambolos.carambolosapi.service;

import com.carambolos.carambolosapi.controller.request.FornadaDaVezRequestDTO;
import com.carambolos.carambolosapi.controller.request.FornadaDaVezUpdateRequestDTO;
import com.carambolos.carambolosapi.exception.EntidadeImprocessavelException;
import com.carambolos.carambolosapi.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.model.Fornada;
import com.carambolos.carambolosapi.model.FornadaDaVez;
import com.carambolos.carambolosapi.model.ProdutoFornada;
import com.carambolos.carambolosapi.repository.FornadaDaVezRepository;
import com.carambolos.carambolosapi.repository.FornadaRepository;
import com.carambolos.carambolosapi.repository.ProdutoFornadaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FornadaDaVezServiceTest {

    private FornadaDaVezRepository fornadaDaVezRepository;
    private ProdutoFornadaRepository produtoFornadaRepository;
    private FornadaRepository fornadaRepository;
    private FornadaDaVezService service;

    @BeforeEach
    void setup() {
        fornadaDaVezRepository = mock(FornadaDaVezRepository.class);
        produtoFornadaRepository = mock(ProdutoFornadaRepository.class);
        fornadaRepository = mock(FornadaRepository.class);
        service = new FornadaDaVezService(fornadaDaVezRepository, produtoFornadaRepository, fornadaRepository);
    }

    @Test
    @DisplayName("criarFornadaDaVez deve salvar quando tudo válido")
    void criarFornadaDaVezDeveSalvarQuandoTudoValido() {
        // Arrange
        FornadaDaVezRequestDTO dto = new FornadaDaVezRequestDTO(1, 2, 50);

        ProdutoFornada produto = new ProdutoFornada();
        produto.setAtivo(true);

        Fornada fornada = new Fornada();
        fornada.setAtivo(true);

        when(produtoFornadaRepository.findById(1)).thenReturn(Optional.of(produto));
        when(fornadaRepository.findById(2)).thenReturn(Optional.of(fornada));

        FornadaDaVez saved = new FornadaDaVez();
        when(fornadaDaVezRepository.save(any(FornadaDaVez.class))).thenReturn(saved);

        // Act
        FornadaDaVez result = service.criarFornadaDaVez(dto);

        // Assert
        assertNotNull(result);
        verify(produtoFornadaRepository).findById(1);
        verify(fornadaRepository).findById(2);
        verify(fornadaDaVezRepository).save(any(FornadaDaVez.class));
    }

    @Test
    @DisplayName("criarFornadaDaVez deve lançar exceção quando produto não encontrado")
    void criarFornadaDaVezDeveLancarExceptionQuandoProdutoNaoEncontrado() {
        FornadaDaVezRequestDTO dto = new FornadaDaVezRequestDTO(1, 2, 50);

        when(produtoFornadaRepository.findById(1)).thenReturn(Optional.empty());

        EntidadeNaoEncontradaException ex = assertThrows(EntidadeNaoEncontradaException.class,
                () -> service.criarFornadaDaVez(dto));
        assertTrue(ex.getMessage().contains("ProdutoFornada com ID 1 não encontrado."));
    }

    @Test
    @DisplayName("criarFornadaDaVez deve lançar exceção quando produto inativo")
    void criarFornadaDaVezDeveLancarExceptionQuandoFornadaNaoEncontrada() {
        FornadaDaVezRequestDTO dto = new FornadaDaVezRequestDTO(1, 2, 50);

        ProdutoFornada produto = new ProdutoFornada();
        produto.setAtivo(true);

        when(produtoFornadaRepository.findById(1)).thenReturn(Optional.of(produto));
        when(fornadaRepository.findById(2)).thenReturn(Optional.empty());

        EntidadeNaoEncontradaException ex = assertThrows(EntidadeNaoEncontradaException.class,
                () -> service.criarFornadaDaVez(dto));
        assertTrue(ex.getMessage().contains("Fornada com ID 2 não encontrada."));
    }

    @Test
    @DisplayName("criarFornadaDaVez deve lançar exceção quando fornada inativa")
    void listarFornadasDaVezDeveRetornarApenasAtivos() {
        FornadaDaVez ativo = new FornadaDaVez();
        ativo.setAtivo(true);
        FornadaDaVez inativo = new FornadaDaVez();
        inativo.setAtivo(false);

        when(fornadaDaVezRepository.findAll()).thenReturn(List.of(ativo, inativo));

        List<FornadaDaVez> lista = service.listarFornadasDaVez();

        assertEquals(1, lista.size());
        assertTrue(lista.getFirst().isAtivo());
    }

    @Test
    @DisplayName("listarFornadasDaVez deve retornar lista vazia quando não houver ativos")
    void buscarFornadaDaVezDeveRetornarQuandoAtivo() {
        FornadaDaVez fornadaDaVez = new FornadaDaVez();
        fornadaDaVez.setAtivo(true);

        when(fornadaDaVezRepository.findById(1)).thenReturn(Optional.of(fornadaDaVez));

        FornadaDaVez resultado = service.buscarFornadaDaVez(1);

        assertNotNull(resultado);
        assertTrue(resultado.isAtivo());
    }

    @Test
    @DisplayName("buscarFornadaDaVez deve lançar exceção quando não encontrado ou inativo")
    void buscarFornadaDaVezDeveLancarExceptionQuandoNaoEncontradoOuInativo() {
        when(fornadaDaVezRepository.findById(1)).thenReturn(Optional.empty());

        EntidadeNaoEncontradaException ex = assertThrows(EntidadeNaoEncontradaException.class,
                () -> service.buscarFornadaDaVez(1));
        assertTrue(ex.getMessage().contains("FornadaDaVez com ID 1 não encontrada."));
    }

    @Test
    @DisplayName("excluirFornadaDaVez deve marcar como inativo")
    void excluirFornadaDaVezDeveMarcarComoInativo() {
        FornadaDaVez fornadaDaVez = new FornadaDaVez();
        fornadaDaVez.setAtivo(true);

        when(fornadaDaVezRepository.findById(1)).thenReturn(Optional.of(fornadaDaVez));
        when(fornadaDaVezRepository.save(any())).thenReturn(fornadaDaVez);

        service.excluirFornadaDaVez(1);

        assertFalse(fornadaDaVez.isAtivo());
        verify(fornadaDaVezRepository).save(fornadaDaVez);
    }

    @Test
    @DisplayName("atualizarQuantidade deve atualizar quando quantidade válida")
    void atualizarQuantidadeDveAtualizarQuandoQuantidadeValida() {
        FornadaDaVez fornadaDaVez = new FornadaDaVez();
        fornadaDaVez.setAtivo(true);
        fornadaDaVez.setQuantidade(10);

        when(fornadaDaVezRepository.findById(1)).thenReturn(Optional.of(fornadaDaVez));
        when(fornadaDaVezRepository.save(any())).thenReturn(fornadaDaVez);

        FornadaDaVezUpdateRequestDTO dto = new FornadaDaVezUpdateRequestDTO(20);

        FornadaDaVez atualizado = service.atualizarQuantidade(1, dto);

        assertEquals(20, atualizado.getQuantidade());
    }

    @Test
    @DisplayName("atualizarQuantidade deve lançar exceção quando quantidade inválida")
    void atualizarQuantidadeDeveLancarExceptionQuandoQuantidadeInvalida() {
        FornadaDaVez fornadaDaVez = new FornadaDaVez();
        fornadaDaVez.setAtivo(true);

        when(fornadaDaVezRepository.findById(1)).thenReturn(Optional.of(fornadaDaVez));

        FornadaDaVezUpdateRequestDTO dto = new FornadaDaVezUpdateRequestDTO(0);

        EntidadeImprocessavelException ex = assertThrows(EntidadeImprocessavelException.class,
                () -> service.atualizarQuantidade(1, dto));
        assertTrue(ex.getMessage().contains("Quantidade deve ser maior que zero."));
    }
}
