package com.carambolos.carambolosapi.application.gateways;

import com.carambolos.carambolosapi.domain.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioGateway {
    List<Usuario> listar();
    Usuario buscarPorId(Integer id);
    Optional<Usuario> findById(Integer id);
    Usuario atualizar(Integer id, Usuario usuario);
    Usuario atualizarDadosPessoais(Integer id, Usuario usuario);
    Usuario cadastrar(Usuario usuario);
    void deletar(Integer id);
    Usuario atualizarImagemPerfil(Integer id, String imagemUrl);
    boolean existePorContatoExcluindoId(String contato, Integer id);
    boolean existePorContato(String contato);
    Usuario buscarPorContato(String contato);
    void atualizarSenha(Integer id, String novaSenha);
}
