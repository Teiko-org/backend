package com.carambolos.carambolosapi.service;

import com.carambolos.carambolosapi.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.model.Usuario;
import com.carambolos.carambolosapi.repository.UsuarioRepository;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService service;

    @Test
    void deveListarUsuariosAtivosComSucesso() {
        List<Usuario> usuarios = List.of(new Usuario(), new Usuario());

        when(repository.findAllByIsAtivoTrue()).thenReturn(usuarios);

        List<Usuario> resultado = service.listar();

        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(Usuario::isAtivo));
    }

    @Test
    void NaoDeveListarSeNaoTiverUsuariosAtivos() {
        when(repository.findAllByIsAtivoTrue()).thenReturn(Collections.emptyList());

        List<Usuario> resultado = service.listar();

        assertTrue(resultado.isEmpty());
    }

    @Test
    void buscarPorIdQuandoAcionadoComIdValidoDeveRetornarUsuario() {
        Usuario usuario = new Usuario();

        when(repository.findByIdAndIsAtivoTrue(anyInt())).thenReturn(usuario);

        Usuario resultado = service.buscarPorId(1);

        assertEquals(usuario.getId(), resultado.getId());
    }

    @Test
     void buscarPorIdQuandoAcionadoComIdInvalidoDeveRetornarException() {
        when(repository.findByIdAndIsAtivoTrue(anyInt())).thenReturn(null);

        assertThrows(EntidadeNaoEncontradaException.class, () -> service.buscarPorId(1));
    }

}