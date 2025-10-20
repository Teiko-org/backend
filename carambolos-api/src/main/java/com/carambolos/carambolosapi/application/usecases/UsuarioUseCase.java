package com.carambolos.carambolosapi.application.usecases;

import com.carambolos.carambolosapi.application.exception.CredenciaisInvalidasException;
import com.carambolos.carambolosapi.application.exception.EntidadeJaExisteException;
import com.carambolos.carambolosapi.application.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.application.gateways.UsuarioGateway;
import com.carambolos.carambolosapi.domain.entity.Usuario;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.UsuarioMapper;
import com.carambolos.carambolosapi.infrastructure.web.response.UsuarioTokenDTO;
import com.carambolos.carambolosapi.system.security.GerenciadorTokenJwt;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

public class UsuarioUseCase {
    private final UsuarioGateway usuarioGateway;

    private final PasswordEncoder passwordEncoder;
    private final GerenciadorTokenJwt gerenciadorTokenJwt;
    private final AuthenticationManager authenticationManager;
    private final TokenBlacklistService tokenBlacklistService;

    public UsuarioUseCase(UsuarioGateway usuarioGateway, PasswordEncoder passwordEncoder, GerenciadorTokenJwt gerenciadorTokenJwt, AuthenticationManager authenticationManager, TokenBlacklistService tokenBlacklistService) {
        this.usuarioGateway = usuarioGateway;
        this.passwordEncoder = passwordEncoder;
        this.gerenciadorTokenJwt = gerenciadorTokenJwt;
        this.authenticationManager = authenticationManager;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    public List<Usuario> listar() {
        return usuarioGateway.listar();
    }

    public Usuario buscarPorId(Integer id) {
        Usuario usuario = usuarioGateway.buscarPorId(id);
        if (usuario == null) {
            throw new EntidadeNaoEncontradaException("Usuario com Id %d não encontrado.".formatted(id));
        }
        return usuario;
    }

    public Usuario atualizar(Integer id, Usuario usuario) {
        Usuario usuarioExistente = usuarioGateway.buscarPorId(id);

        if (usuarioExistente == null) {
            throw new EntidadeNaoEncontradaException("Usuario com Id %d não encontrado.".formatted(id));
        }

        if (usuarioGateway.existePorContatoExcluindoId(usuario.getContato(), id)) {
            throw new EntidadeJaExisteException("Esse contato já existe no sistema.");
        }

        if (usuario.getSenha() == null || usuario.getSenha().trim().isEmpty()) {
            usuario.setSenha(usuarioExistente.getSenha());
        } else if (!usuario.getSenha().equals(usuarioExistente.getSenha())) {
            String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
            usuario.setSenha(senhaCriptografada);
        }

        return usuarioGateway.atualizar(id, usuario);
    }

    public Usuario atualizarDadosPessoais(Integer id, Usuario usuario) {
        Usuario usuarioExistente = usuarioGateway.buscarPorId(id);

        if (usuarioExistente == null) {
            throw new EntidadeNaoEncontradaException("Usuario com Id %d não encontrado.".formatted(id));
        }

        if (usuarioGateway.existePorContatoExcluindoId(usuario.getContato(), id)) {
            throw new EntidadeJaExisteException("Esse contato já existe no sistema.");
        }

        return usuarioGateway.atualizarDadosPessoais(id, usuario);
    }

    public Usuario cadastrar(Usuario usuario) {
        if (usuarioGateway.existePorContato(usuario.getContato())) {
            throw new EntidadeJaExisteException("Este telefone já está cadastrado. Tente fazer login ou use outro número.");
        }

        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);

        return usuarioGateway.cadastrar(usuario);
    }

    public UsuarioTokenDTO autenticar(Usuario usuario, HttpServletResponse httpServletResponse) {
        final UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(
                usuario.getContato(), usuario.getSenha());

        final Authentication authentication = this.authenticationManager.authenticate(credentials);

        Usuario usuarioAutenticado = usuarioGateway.buscarPorContato(usuario.getContato());
        if (usuarioAutenticado == null) {
            throw new EntidadeNaoEncontradaException("Contato do usuário não cadastrado");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final String token = gerenciadorTokenJwt.generateToken(authentication, usuarioAutenticado.getSysAdmin());

        Cookie cookie = new Cookie("authToken", token);
        cookie.setHttpOnly(true);
        // cookie.setSecure(true); // Descomentar caso seja usado HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        httpServletResponse.addCookie(cookie);

        return UsuarioMapper.toTokenDTO(usuarioAutenticado, token);
    }

    public void logOut(HttpServletResponse response, String token) {
        String jti = gerenciadorTokenJwt.getClaimForToken(token, Claims::getId);
        long exp = gerenciadorTokenJwt.getExpirationDateFromToken(token).getTime();
        tokenBlacklistService.blacklistToken(token);

        Cookie cookie = new Cookie("authToken", null);
        cookie.setHttpOnly(true);
        // cookie.setSecure(true); // Descomentar caso seja usado HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    public void alterarSenha(Integer id, String senhaAtual, String novaSenha) {
        Usuario usuario = usuarioGateway.buscarPorId(id);
        if (usuario == null) {
            throw new EntidadeNaoEncontradaException("Usuário não encontrado.");
        }
        if (!passwordEncoder.matches(senhaAtual, usuario.getSenha())) {
            throw new CredenciaisInvalidasException("Senha atual incorreta.");
        }

        String novaSenhaCriptografada = passwordEncoder.encode(novaSenha);
        usuarioGateway.atualizarSenha(id, novaSenhaCriptografada);
    }

    public void deletar(Integer id) {
        Usuario usuario = usuarioGateway.buscarPorId(id);
        if (usuario == null) {
            throw new EntidadeNaoEncontradaException("Usuario com Id %d não encontrado.".formatted(id));
        }
        usuarioGateway.deletar(id);
    }

    public Usuario atualizarImagemPerfil(Integer id, String imagemUrl) {
        if (id == null || imagemUrl == null || imagemUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("ID do usuário e URL da imagem são obrigatórios");
        }

        Usuario usuario = usuarioGateway.buscarPorId(id);
        if (usuario == null) {
            throw new EntidadeNaoEncontradaException("Usuario com Id %d não encontrado.".formatted(id));
        }

        return usuarioGateway.atualizarImagemPerfil(id, imagemUrl);
    }

}
