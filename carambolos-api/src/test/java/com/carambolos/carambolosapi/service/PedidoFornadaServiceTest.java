package com.carambolos.carambolosapi.service;

import com.carambolos.carambolosapi.controller.request.PedidoFornadaRequestDTO;
import com.carambolos.carambolosapi.controller.request.PedidoFornadaUpdateRequestDTO;
import com.carambolos.carambolosapi.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.model.Endereco;
import com.carambolos.carambolosapi.model.FornadaDaVez;
import com.carambolos.carambolosapi.model.PedidoFornada;
import com.carambolos.carambolosapi.model.Usuario;
import com.carambolos.carambolosapi.repository.EnderecoRepository;
import com.carambolos.carambolosapi.repository.FornadaDaVezRepository;
import com.carambolos.carambolosapi.repository.PedidoFornadaRepository;
import com.carambolos.carambolosapi.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PedidoFornadaServiceTest {

    private PedidoFornadaRepository pedidoFornadaRepository;
    private FornadaDaVezRepository fornadaDaVezRepository;
    private EnderecoRepository enderecoRepository;
    private UsuarioRepository usuarioRepository;
    private PedidoFornadaService pedidoFornadaService;

    @BeforeEach
    void setup() {
        pedidoFornadaRepository = mock(PedidoFornadaRepository.class);
        fornadaDaVezRepository = mock(FornadaDaVezRepository.class);
        enderecoRepository = mock(EnderecoRepository.class);
        usuarioRepository = mock(UsuarioRepository.class);
        pedidoFornadaService = new PedidoFornadaService(
                pedidoFornadaRepository,
                fornadaDaVezRepository,
                enderecoRepository,
                usuarioRepository
        );
    }

    @Test
    @DisplayName("Deve criar pedido fornada com sucesso")
    void deveCriarPedidoFornadaComSucesso() {
        FornadaDaVez fornadaDaVez = new FornadaDaVez();
        fornadaDaVez.setAtivo(true);

        Endereco endereco = new Endereco();
        endereco.setAtivo(true);

        Usuario usuario = new Usuario();
        usuario.setAtivo(true);

        PedidoFornadaRequestDTO request = new PedidoFornadaRequestDTO(
                1, 2, 3, 10, LocalDate.of(2025, 5, 10)
        );

        when(fornadaDaVezRepository.findById(1)).thenReturn(Optional.of(fornadaDaVez));
        when(enderecoRepository.findById(2)).thenReturn(Optional.of(endereco));
        when(usuarioRepository.findById(3)).thenReturn(Optional.of(usuario));
        when(pedidoFornadaRepository.save(any(PedidoFornada.class))).thenAnswer(i -> i.getArgument(0));

        PedidoFornada resultado = pedidoFornadaService.criarPedidoFornada(request);

        assertNotNull(resultado);
        assertEquals(10, resultado.getQuantidade());
        assertEquals(LocalDate.of(2025, 5, 10), resultado.getDataPrevisaoEntrega());
        assertEquals(fornadaDaVez, resultado.getFornadaDaVez());
        assertEquals(endereco, resultado.getEndereco());
        assertEquals(usuario, resultado.getUsuario());

        verify(fornadaDaVezRepository).findById(1);
        verify(enderecoRepository).findById(2);
        verify(usuarioRepository).findById(3);
        verify(pedidoFornadaRepository).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando FornadaDaVez não for encontrada")
    void deveLancarExcecaoQuandoFornadaDaVezNaoEncontrada() {
        PedidoFornadaRequestDTO request = new PedidoFornadaRequestDTO(1, 2, 3, 10, LocalDate.now());
        when(fornadaDaVezRepository.findById(1)).thenReturn(Optional.empty());

        EntidadeNaoEncontradaException ex = assertThrows(EntidadeNaoEncontradaException.class,
                () -> pedidoFornadaService.criarPedidoFornada(request));

        assertEquals("FornadaDaVez com ID 1 não encontrada.", ex.getMessage());
        verify(fornadaDaVezRepository).findById(1);
        verify(enderecoRepository, never()).findById(any());
        verify(pedidoFornadaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando Endereco não for encontrado")
    void deveLancarExcecaoQuandoEnderecoNaoEncontrado() {
        FornadaDaVez fornadaDaVez = new FornadaDaVez();
        fornadaDaVez.setAtivo(true);

        PedidoFornadaRequestDTO request = new PedidoFornadaRequestDTO(1, 2, 3, 10, LocalDate.now());

        when(fornadaDaVezRepository.findById(1)).thenReturn(Optional.of(fornadaDaVez));
        when(enderecoRepository.findById(2)).thenReturn(Optional.empty());

        EntidadeNaoEncontradaException ex = assertThrows(EntidadeNaoEncontradaException.class,
                () -> pedidoFornadaService.criarPedidoFornada(request));

        assertEquals("Endereço com ID 2 não encontrado.", ex.getMessage());

        verify(fornadaDaVezRepository).findById(1);
        verify(enderecoRepository).findById(2);
        verify(usuarioRepository, never()).findById(any());
        verify(pedidoFornadaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando Usuario não for encontrado e ID fornecido")
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        FornadaDaVez fornadaDaVez = new FornadaDaVez();
        fornadaDaVez.setAtivo(true);

        Endereco endereco = new Endereco();
        endereco.setAtivo(true);

        PedidoFornadaRequestDTO request = new PedidoFornadaRequestDTO(1, 2, 3, 10, LocalDate.now());

        when(fornadaDaVezRepository.findById(1)).thenReturn(Optional.of(fornadaDaVez));
        when(enderecoRepository.findById(2)).thenReturn(Optional.of(endereco));
        when(usuarioRepository.findById(3)).thenReturn(Optional.empty());

        EntidadeNaoEncontradaException ex = assertThrows(EntidadeNaoEncontradaException.class,
                () -> pedidoFornadaService.criarPedidoFornada(request));

        assertEquals("Usuário com ID 3 não encontrado.", ex.getMessage());

        verify(fornadaDaVezRepository).findById(1);
        verify(enderecoRepository).findById(2);
        verify(usuarioRepository).findById(3);
        verify(pedidoFornadaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve criar pedido sem usuário quando usuarioId for nulo")
    void deveCriarPedidoSemUsuarioQuandoUsuarioIdNulo() {
        FornadaDaVez fornadaDaVez = new FornadaDaVez();
        fornadaDaVez.setAtivo(true);

        Endereco endereco = new Endereco();
        endereco.setAtivo(true);

        PedidoFornadaRequestDTO request = new PedidoFornadaRequestDTO(1, 2, null, 10, LocalDate.now());

        when(fornadaDaVezRepository.findById(1)).thenReturn(Optional.of(fornadaDaVez));
        when(enderecoRepository.findById(2)).thenReturn(Optional.of(endereco));
        when(pedidoFornadaRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        PedidoFornada resultado = pedidoFornadaService.criarPedidoFornada(request);

        assertNotNull(resultado);
        assertNull(resultado.getUsuario());
        assertEquals(fornadaDaVez, resultado.getFornadaDaVez());
        assertEquals(endereco, resultado.getEndereco());

        verify(fornadaDaVezRepository).findById(1);
        verify(enderecoRepository).findById(2);
        verify(usuarioRepository, never()).findById(any());
        verify(pedidoFornadaRepository).save(any());
    }

    @Test
    @DisplayName("Deve buscar pedido fornada com sucesso")
    void deveBuscarPedidoFornadaComSucesso() {
        PedidoFornada pedido = new PedidoFornada();
        pedido.setAtivo(true);

        when(pedidoFornadaRepository.findById(1)).thenReturn(Optional.of(pedido));

        PedidoFornada resultado = pedidoFornadaService.buscarPedidoFornada(1);

        assertEquals(pedido, resultado);
        verify(pedidoFornadaRepository).findById(1);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar pedido fornada não encontrado")
    void deveLancarExcecaoAoBuscarPedidoFornadaNaoEncontrado() {
        when(pedidoFornadaRepository.findById(1)).thenReturn(Optional.empty());

        EntidadeNaoEncontradaException ex = assertThrows(EntidadeNaoEncontradaException.class,
                () -> pedidoFornadaService.buscarPedidoFornada(1));

        assertEquals("PedidoFornada com ID 1 não encontrado.", ex.getMessage());
        verify(pedidoFornadaRepository).findById(1);
    }

    @Test
    @DisplayName("Deve listar pedidos fornada ativos")
    void deveListarPedidosFornadaAtivos() {
        PedidoFornada ativo = new PedidoFornada();
        ativo.setAtivo(true);

        PedidoFornada inativo = new PedidoFornada();
        inativo.setAtivo(false);

        when(pedidoFornadaRepository.findAll()).thenReturn(List.of(ativo, inativo));

        List<PedidoFornada> resultado = pedidoFornadaService.listarPedidosFornada();

        assertEquals(1, resultado.size());
        assertTrue(resultado.contains(ativo));
        assertFalse(resultado.contains(inativo));
        verify(pedidoFornadaRepository).findAll();
    }

    @Test
    @DisplayName("Deve excluir pedido fornada com sucesso")
    void deveExcluirPedidoFornadaComSucesso() {
        PedidoFornada pedido = new PedidoFornada();
        pedido.setAtivo(true);

        when(pedidoFornadaRepository.findById(1)).thenReturn(Optional.of(pedido));
        when(pedidoFornadaRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        pedidoFornadaService.excluirPedidoFornada(1);

        assertFalse(pedido.isAtivo());
        verify(pedidoFornadaRepository).findById(1);
        verify(pedidoFornadaRepository).save(pedido);
    }

    @Test
    @DisplayName("Deve atualizar pedido fornada com sucesso")
    void deveAtualizarPedidoFornadaComSucesso() {
        PedidoFornada pedido = new PedidoFornada();
        pedido.setAtivo(true);

        PedidoFornadaUpdateRequestDTO request = new PedidoFornadaUpdateRequestDTO(15, LocalDate.of(2025, 6, 1));

        when(pedidoFornadaRepository.findById(1)).thenReturn(Optional.of(pedido));
        when(pedidoFornadaRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        PedidoFornada resultado = pedidoFornadaService.atualizarPedidoFornada(1, request);

        assertEquals(15, resultado.getQuantidade());
        assertEquals(LocalDate.of(2025, 6, 1), resultado.getDataPrevisaoEntrega());
        verify(pedidoFornadaRepository).findById(1);
        verify(pedidoFornadaRepository).save(pedido);
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar com quantidade nula ou menor ou igual a zero")
    void deveLancarExcecaoAoAtualizarComQuantidadeInvalida() {
        PedidoFornada pedido = new PedidoFornada();
        pedido.setAtivo(true);

        when(pedidoFornadaRepository.findById(1)).thenReturn(Optional.of(pedido));

        PedidoFornadaUpdateRequestDTO requestNulo = new PedidoFornadaUpdateRequestDTO(null, LocalDate.now());
        IllegalArgumentException exNulo = assertThrows(IllegalArgumentException.class,
                () -> pedidoFornadaService.atualizarPedidoFornada(1, requestNulo));
        assertEquals("A quantidade deve ser maior que zero.", exNulo.getMessage());

        PedidoFornadaUpdateRequestDTO requestZero = new PedidoFornadaUpdateRequestDTO(0, LocalDate.now());
        IllegalArgumentException exZero = assertThrows(IllegalArgumentException.class,
                () -> pedidoFornadaService.atualizarPedidoFornada(1, requestZero));
        assertEquals("A quantidade deve ser maior que zero.", exZero.getMessage());

        verify(pedidoFornadaRepository, times(2)).findById(1);
        verify(pedidoFornadaRepository, never()).save(any());
    }
}
