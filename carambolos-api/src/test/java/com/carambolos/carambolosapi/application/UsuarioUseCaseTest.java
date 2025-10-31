package com.carambolos.carambolosapi.application;

import com.carambolos.carambolosapi.application.exception.EntidadeJaExisteException;
import com.carambolos.carambolosapi.application.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.application.gateways.UsuarioGateway;
import com.carambolos.carambolosapi.application.usecases.TokenBlacklistService;
import com.carambolos.carambolosapi.application.usecases.UsuarioUseCase;
import com.carambolos.carambolosapi.domain.entity.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.carambolos.carambolosapi.system.security.GerenciadorTokenJwt;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioUseCaseTest {

    @Mock private UsuarioGateway usuarioGateway;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private GerenciadorTokenJwt gerenciadorTokenJwt;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private TokenBlacklistService tokenBlacklistService;

    private UsuarioUseCase usuarioUseCase;

    @BeforeEach
    void setup() {
        usuarioUseCase = new UsuarioUseCase(usuarioGateway, passwordEncoder, gerenciadorTokenJwt, authenticationManager, tokenBlacklistService);
    }

    @Test
    @DisplayName("Deve listar usuários ativos com sucessos")
    void deveListarUsuariosAtivosComSucesso() {
        List<Usuario> usuarios = List.of(new Usuario(), new Usuario());

        when(usuarioGateway.listar()).thenReturn(usuarios);

        List<Usuario> resultado = usuarioUseCase.listar();

        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(Usuario::isAtivo));
    }

    @Test
    @DisplayName("Não deve listar se não tiver usuários ativos")
    void NaoDeveListarSeNaoTiverUsuariosAtivos() {
        when(usuarioGateway.listar()).thenReturn(Collections.emptyList());

        List<Usuario> resultado = usuarioUseCase.listar();

        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("BuscarPorId quando acionado com id válido deve retornar usuário")
    void buscarPorIdQuandoAcionadoComIdValidoDeveRetornarUsuario() {
        Usuario usuarioEntity = new Usuario();

        when(usuarioGateway.buscarPorId(anyInt())).thenReturn(usuarioEntity);

        Usuario resultado = usuarioUseCase.buscarPorId(1);

        assertEquals(usuarioEntity.getId(), resultado.getId());
    }

    @Test
    @DisplayName("BuscarPorId quando acionado com id inválido deve retornar exception")
    void buscarPorIdQuandoAcionadoComIdInvalidoDeveRetornarException() {
        when(usuarioGateway.buscarPorId(anyInt())).thenReturn(null);

        assertThrows(EntidadeNaoEncontradaException.class, () -> usuarioUseCase.buscarPorId(1));
    }

    @Test
    @DisplayName("Deve lançar EntidadeJaExisteException se usuário já existir")
    void deveLancarExcecaoSeUsuarioJaExistir() {
        int id = 1;
        String contato = "123456789";

        Usuario usuario = new Usuario();
        usuario.setContato(contato);

        Usuario usuarioExistente = new Usuario();
        when(usuarioGateway.buscarPorId(id)).thenReturn(usuarioExistente);
        when(usuarioGateway.existePorContatoExcluindoId(contato, id)).thenReturn(true);

        assertThrows(EntidadeJaExisteException.class, () -> usuarioUseCase.atualizar(id, usuario));
    }

    @Test
    @DisplayName("Quando deletar por id é acionado com id válido deve remover usuário")
    void deletarPorIdQuandoAcionadoComIdValidoDeveRemoverUsuario() {
        int id = 1;

        Usuario usuarioExistente = new Usuario();
        when(usuarioGateway.buscarPorId(id)).thenReturn(usuarioExistente);

        usuarioUseCase.deletar(id);

        verify(usuarioGateway).deletar(id);
    }

    @Test
    @DisplayName("Deve lançar Entidade Nao Encontrada Exception quando usuário não existir")
    void deveLancarExcecaoQuandoUsuarioNaoExistir() {
        int id = 1;

        when(usuarioGateway.buscarPorId(id)).thenReturn(null);

        assertThrows(EntidadeNaoEncontradaException.class, () -> usuarioUseCase.deletar(id));
    }
}