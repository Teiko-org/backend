package com.carambolos.carambolosapi.service;

import com.carambolos.carambolosapi.exception.EntidadeJaExisteException;
import com.carambolos.carambolosapi.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.model.Usuario;
import com.carambolos.carambolosapi.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService service;

    @Test
    @DisplayName("Deve listar usuários ativos com sucessos")
    void deveListarUsuariosAtivosComSucesso() {
        List<Usuario> usuarios = List.of(new Usuario(), new Usuario());

        when(repository.findAllByIsAtivoTrue()).thenReturn(usuarios);

        List<Usuario> resultado = service.listar();

        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(Usuario::isAtivo));
    }

    @Test
    @DisplayName("Não deve listar se não tiver usuários ativos")
    void NaoDeveListarSeNaoTiverUsuariosAtivos() {
        when(repository.findAllByIsAtivoTrue()).thenReturn(Collections.emptyList());

        List<Usuario> resultado = service.listar();

        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("BuscarPorId quando acionado com id válido deve retornar usuário")
    void buscarPorIdQuandoAcionadoComIdValidoDeveRetornarUsuario() {
        Usuario usuario = new Usuario();

        when(repository.findByIdAndIsAtivoTrue(anyInt())).thenReturn(usuario);

        Usuario resultado = service.buscarPorId(1);

        assertEquals(usuario.getId(), resultado.getId());
    }

    @Test
    @DisplayName("BuscarPorId quando acionado com id inválido deve retornar exception")
    void buscarPorIdQuandoAcionadoComIdInvalidoDeveRetornarException() {
        when(repository.findByIdAndIsAtivoTrue(anyInt())).thenReturn(null);

        assertThrows(EntidadeNaoEncontradaException.class, () -> service.buscarPorId(1));
    }

    @Test
    @DisplayName("Deve atualizar usuário com sucesso")
    void deveAtualizarUsuarioComSucesso() {
        int id = 1;
        String contato = "123456789";

        Usuario usuarioAtualizado = new Usuario();
        usuarioAtualizado.setContato(contato);

        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(id);

        when(repository.findByIdAndIsAtivoTrue(id)).thenReturn(usuarioExistente);
        when(repository.existsByContatoAndIdNotAndIsAtivoTrue(contato, id)).thenReturn(false);
        when(repository.save(usuarioAtualizado)).thenReturn(usuarioAtualizado);

        Usuario resultado = service.atualizar(id, usuarioAtualizado);

        assertEquals(id, resultado.getId());
        assertEquals(contato, resultado.getContato());
        assertTrue(resultado.isAtivo());
    }

    @Test
    @DisplayName("Deve lançar EntidadeJaExisteException se usuário já existir")
    void deveLancarExcecaoSeUsuarioJaExistir() {
        int id = 1;
        String contato = "123456789";

        Usuario usuario = new Usuario();
        usuario.setContato(contato);

        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(id);

        when(repository.findByIdAndIsAtivoTrue(id)).thenReturn(usuarioExistente);
        when(repository.existsByContatoAndIdNotAndIsAtivoTrue(contato, id)).thenReturn(true);

        assertThrows(EntidadeJaExisteException.class, () -> service.atualizar(id, usuario));
    }

    @Test
    @DisplayName("Quando deletar por id é acionado com id válido deve remover usuário")
    void deletarPorIdQuandoAcionadoComIdValidoDeveRemoverUsuario() {
        int id = 1;

        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(id);

        when(repository.findByIdAndIsAtivoTrue(id)).thenReturn(usuarioExistente);
        when(repository.save(usuarioExistente)).thenReturn(usuarioExistente);

        service.deletar(id);

        assertFalse(usuarioExistente.isAtivo());
        verify(repository).save(usuarioExistente);
    }

    @Test
    @DisplayName("Deve lançar Entidade Nao Encontrada Exception quando usuário não existir")
    void deveLancarExcecaoQuandoUsuarioNaoExistir() {
        int id = 1;

        when(repository.findByIdAndIsAtivoTrue(id)).thenReturn(null);

        assertThrows(EntidadeNaoEncontradaException.class, () -> service.deletar(id));
    }


}