package com.carambolos.carambolosapi.application;

import com.carambolos.carambolosapi.application.exception.EntidadeJaExisteException;
import com.carambolos.carambolosapi.application.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.application.usecases.UsuarioUseCase;
import com.carambolos.carambolosapi.domain.entity.Usuario;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.UsuarioEntity;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioUseCaseTest {

    @Mock
    private UsuarioRepository repository;

    @InjectMocks
    private UsuarioUseCase usuarioUseCase;

    @Test
    @DisplayName("Deve listar usuários ativos com sucessos")
    void deveListarUsuariosAtivosComSucesso() {
        List<UsuarioEntity> usuarioEntities = List.of(new UsuarioEntity(), new UsuarioEntity());

        when(repository.findAllByIsAtivoTrue()).thenReturn(usuarioEntities);

        List<Usuario> resultado = usuarioUseCase.listar();

        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(Usuario::isAtivo));
    }

    @Test
    @DisplayName("Não deve listar se não tiver usuários ativos")
    void NaoDeveListarSeNaoTiverUsuariosAtivos() {
        when(repository.findAllByIsAtivoTrue()).thenReturn(Collections.emptyList());

        List<Usuario> resultado = usuarioUseCase.listar();

        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("BuscarPorId quando acionado com id válido deve retornar usuário")
    void buscarPorIdQuandoAcionadoComIdValidoDeveRetornarUsuario() {
        UsuarioEntity usuarioEntity = new UsuarioEntity();

        when(repository.findByIdAndIsAtivoTrue(anyInt())).thenReturn(usuarioEntity);

        Usuario resultado = usuarioUseCase.buscarPorId(1);

        assertEquals(usuarioEntity.getId(), resultado.getId());
    }

    @Test
    @DisplayName("BuscarPorId quando acionado com id inválido deve retornar exception")
    void buscarPorIdQuandoAcionadoComIdInvalidoDeveRetornarException() {
        when(repository.findByIdAndIsAtivoTrue(anyInt())).thenReturn(null);

        assertThrows(EntidadeNaoEncontradaException.class, () -> usuarioUseCase.buscarPorId(1));
    }

    @Test
    @DisplayName("Deve lançar EntidadeJaExisteException se usuário já existir")
    void deveLancarExcecaoSeUsuarioJaExistir() {
        int id = 1;
        String contato = "123456789";

        Usuario usuario = new Usuario();
        usuario.setContato(contato);

        UsuarioEntity usuarioEntityExistente = new UsuarioEntity();
        usuarioEntityExistente.setId(id);

        when(repository.findByIdAndIsAtivoTrue(id)).thenReturn(usuarioEntityExistente);
        when(repository.existsByContatoAndIdNotAndIsAtivoTrue(contato, id)).thenReturn(true);

        assertThrows(EntidadeJaExisteException.class, () -> usuarioUseCase.atualizar(id, usuario));
    }

    @Test
    @DisplayName("Quando deletar por id é acionado com id válido deve remover usuário")
    void deletarPorIdQuandoAcionadoComIdValidoDeveRemoverUsuario() {
        int id = 1;

        UsuarioEntity usuarioEntityExistente = new UsuarioEntity();
        usuarioEntityExistente.setId(id);

        when(repository.findByIdAndIsAtivoTrue(id)).thenReturn(usuarioEntityExistente);
        when(repository.save(usuarioEntityExistente)).thenReturn(usuarioEntityExistente);

        usuarioUseCase.deletar(id);

        assertFalse(usuarioEntityExistente.isAtivo());
        verify(repository).save(usuarioEntityExistente);
    }

    @Test
    @DisplayName("Deve lançar Entidade Nao Encontrada Exception quando usuário não existir")
    void deveLancarExcecaoQuandoUsuarioNaoExistir() {
        int id = 1;

        when(repository.findByIdAndIsAtivoTrue(id)).thenReturn(null);

        assertThrows(EntidadeNaoEncontradaException.class, () -> usuarioUseCase.deletar(id));
    }
}