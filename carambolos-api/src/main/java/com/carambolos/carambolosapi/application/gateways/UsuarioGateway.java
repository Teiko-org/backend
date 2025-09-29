package com.carambolos.carambolosapi.application.gateways;

import com.carambolos.carambolosapi.domain.entity.Usuario;
import com.carambolos.carambolosapi.infrastructure.web.response.UsuarioTokenDTO;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Optional;

public interface UsuarioGateway {
    List<Usuario> listar();
    Usuario buscarPorId(Integer id);
    Optional<Usuario> findById(Integer id);
    Usuario atualizar(Integer id, Usuario usuario);
    Usuario atualizarDadosPessoais(Integer id, Usuario usuario);
    Usuario cadastrar(Usuario usuario);
    UsuarioTokenDTO autenticar(Usuario usuario, HttpServletResponse httpServletResponse);
    void logOut(HttpServletResponse response , String token);
    void alterarSenha(Integer id, String senhaAtual, String novaSenha);
    void deletar(Integer id);
    Usuario atualizarImagemPerfil(Integer id, String imagemUrl);
}
