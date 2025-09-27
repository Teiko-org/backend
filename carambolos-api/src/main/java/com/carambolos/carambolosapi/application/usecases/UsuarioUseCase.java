package com.carambolos.carambolosapi.application.usecases;

import com.carambolos.carambolosapi.application.gateways.UsuarioGateway;
import com.carambolos.carambolosapi.domain.entity.Usuario;
import com.carambolos.carambolosapi.infrastructure.web.response.UsuarioTokenDTO;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public class UsuarioUseCase {
    private final UsuarioGateway usuarioGateway;

    public UsuarioUseCase(UsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    public List<Usuario> listar() {
        return usuarioGateway.listar();
    }

    public Usuario buscarPorId(Integer id) {
        return usuarioGateway.buscarPorId(id);
    }

    public Usuario atualizar(Integer id, Usuario usuario) {
        return usuarioGateway.atualizar(id, usuario);
    }

    public Usuario atualizarDadosPessoais(Integer id, Usuario usuario) {
        return usuarioGateway.atualizarDadosPessoais(id, usuario);
    }

    public Usuario cadastrar(Usuario usuario)  {
        return usuarioGateway.cadastrar(usuario);
    }

    public UsuarioTokenDTO autenticar(Usuario usuario, HttpServletResponse httpServletResponse) {
        return usuarioGateway.autenticar(usuario, httpServletResponse);
    }

    public void logOut(HttpServletResponse response , String token) {
        usuarioGateway.logOut(response, token);
    }

    public void alterarSenha(Integer id, String senhaAtual, String novaSenha) {
        usuarioGateway.alterarSenha(id, senhaAtual, novaSenha);
    }

    public void deletar(Integer id) {
        usuarioGateway.deletar(id);
    }

    public Usuario atualizarImagemPerfil(Integer id, String imagemUrl) {
        return usuarioGateway.atualizarImagemPerfil(id, imagemUrl);
    }

}
