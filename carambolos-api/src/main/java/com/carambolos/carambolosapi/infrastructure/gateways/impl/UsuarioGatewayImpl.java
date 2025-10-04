package com.carambolos.carambolosapi.infrastructure.gateways.impl;

import com.carambolos.carambolosapi.application.exception.CredenciaisInvalidasException;
import com.carambolos.carambolosapi.application.exception.EntidadeJaExisteException;
import com.carambolos.carambolosapi.application.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.application.gateways.UsuarioGateway;
import com.carambolos.carambolosapi.application.usecases.TokenBlacklistService;
import com.carambolos.carambolosapi.domain.entity.Usuario;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.UsuarioMapper;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.UsuarioEntity;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.UsuarioRepository;
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

public class UsuarioGatewayImpl implements UsuarioGateway {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    private final PasswordEncoder passwordEncoder;
    private final GerenciadorTokenJwt gerenciadorTokenJwt;
    private final AuthenticationManager authenticationManager;
    private final TokenBlacklistService tokenBlacklistService;

    public UsuarioGatewayImpl(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper, PasswordEncoder passwordEncoder, GerenciadorTokenJwt gerenciadorTokenJwt, AuthenticationManager authenticationManager, TokenBlacklistService tokenBlacklistService) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.passwordEncoder = passwordEncoder;
        this.gerenciadorTokenJwt = gerenciadorTokenJwt;
        this.authenticationManager = authenticationManager;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @Override
    public List<Usuario> listar() {
        List<UsuarioEntity> usuarios = usuarioRepository.findAllByIsAtivoTrue();
        return usuarioMapper.toDomain(usuarios);
    }

    @Override
    public Usuario buscarPorId(Integer id) {
        UsuarioEntity usuarioExistente = usuarioRepository.findByIdAndIsAtivoTrue(id);
        if (usuarioExistente != null) {
            return usuarioMapper.toDomain(usuarioExistente);
        }
        throw new EntidadeNaoEncontradaException("Usuario com Id %d não encontrado.".formatted(id));
    }

    @Override
    public Usuario atualizar(Integer id, Usuario usuario) {
        UsuarioEntity usuarioExistente = usuarioRepository.findByIdAndIsAtivoTrue(id);

        if (usuarioExistente == null) {
            throw new EntidadeNaoEncontradaException("Usuario com Id %d não encontrado.".formatted(id));
        }

        UsuarioEntity usuarioEntity = usuarioMapper.toEntity(usuario);
        boolean existePorContato = usuarioRepository.existsByContatoAndIdNotAndIsAtivoTrue(usuarioEntity.getContato(), id);

        if (existePorContato) {
            throw new EntidadeJaExisteException("Esse contato já existe no sistema.");
        }

        if (usuarioEntity.getSenha() == null || usuarioEntity.getSenha().trim().isEmpty()) {
            usuarioEntity.setSenha(usuarioExistente.getSenha());
        } else if (!usuarioEntity.getSenha().equals(usuarioExistente.getSenha())) {
            String senhaCriptografada = passwordEncoder.encode(usuarioEntity.getSenha());
            usuario.setSenha(senhaCriptografada);
        }

        usuarioEntity.setId(id);
        UsuarioEntity entidadeSalva =  usuarioRepository.save(usuarioEntity);
        return  usuarioMapper.toDomain(entidadeSalva);
    }

    @Override
    public Usuario atualizarDadosPessoais(Integer id, Usuario usuario) {
        UsuarioEntity usuarioExistente = usuarioRepository.findByIdAndIsAtivoTrue(id);

        if (usuarioExistente == null) {
            throw new EntidadeNaoEncontradaException("Usuario com Id %d não encontrado.".formatted(id));
        }

        UsuarioEntity usuarioEntity = usuarioMapper.toEntity(usuario);
        boolean existePorContato = usuarioRepository.existsByContatoAndIdNotAndIsAtivoTrue(usuarioEntity.getContato(), id);

        if (existePorContato) {
            throw new EntidadeJaExisteException("Esse contato já existe no sistema.");
        }

        // Atualizar apenas dados pessoais, mantendo senha e outros campos
        usuarioExistente.setNome(usuarioEntity.getNome());
        usuarioExistente.setContato(usuarioEntity.getContato());
        usuarioExistente.setDataNascimento(usuarioEntity.getDataNascimento());
        usuarioExistente.setGenero(usuarioEntity.getGenero());

        UsuarioEntity entidadeSalva =  usuarioRepository.save(usuarioExistente);
        return usuarioMapper.toDomain(entidadeSalva);
    }

    @Override
    public Usuario cadastrar(Usuario usuario) {
        UsuarioEntity usuarioEntity = usuarioMapper.toEntity(usuario);
        if (usuarioRepository.findByContatoAndIsAtivoTrue(usuarioEntity.getContato()).isPresent()) {
            throw new EntidadeJaExisteException("Este telefone já está cadastrado. Tente fazer login ou use outro número.");
        }

        String senhaCriptografada = passwordEncoder.encode(usuarioEntity.getSenha());
        usuarioEntity.setSenha(senhaCriptografada);
        UsuarioEntity entidadeSalva = usuarioRepository.save(usuarioEntity);

        return usuarioMapper.toDomain(entidadeSalva);
    }

    @Override
    public UsuarioTokenDTO autenticar(Usuario usuario, HttpServletResponse httpServletResponse) {
        final UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(
                usuario.getContato(), usuario.getSenha());

        final Authentication authentication = this.authenticationManager.authenticate(credentials);

        UsuarioEntity usuarioAutenticado =
                usuarioRepository.findByContatoAndIsAtivoTrue(usuario.getContato())
                        .orElseThrow(
                                () -> new EntidadeNaoEncontradaException("Contato do usuário não cadastrado")
                        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final String token = gerenciadorTokenJwt.generateToken(authentication, usuarioAutenticado.getSysAdmin());

        Cookie cookie = new Cookie("authToken", token);
        cookie.setHttpOnly(true);
//        cookie.setSecure(true); // Descomentar caso seja usado HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        httpServletResponse.addCookie(cookie);

        return UsuarioMapper.toTokenDTO(usuarioAutenticado, token);
    }

    @Override
    public void logOut(HttpServletResponse response, String token) {
        String jti = gerenciadorTokenJwt.getClaimForToken(token, Claims::getId);
        long exp = gerenciadorTokenJwt.getExpirationDateFromToken(token).getTime();
        tokenBlacklistService.blacklistToken(token);

        Cookie cookie = new Cookie("authToken", null);
        cookie.setHttpOnly(true);
//        cookie.setSecure(true); // Descomentar caso seja usado HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    @Override
    public void alterarSenha(Integer id, String senhaAtual, String novaSenha) {
        UsuarioEntity usuarioEntity = usuarioRepository.findByIdAndIsAtivoTrue(id);
        if (usuarioEntity == null) {
            throw new EntidadeNaoEncontradaException("Usuário não encontrado.");
        }
        if (!passwordEncoder.matches(senhaAtual, usuarioEntity.getSenha())) {
            throw new CredenciaisInvalidasException("Senha atual incorreta.");
        }
        usuarioEntity.setSenha(passwordEncoder.encode(novaSenha));
        usuarioRepository.save(usuarioEntity);
    }

    @Override
    public void deletar(Integer id) {
        UsuarioEntity usuarioEntity = usuarioRepository.findByIdAndIsAtivoTrue(id);
        if (usuarioEntity != null) {
            usuarioEntity.setAtivo(false);
            usuarioRepository.save(usuarioEntity);
            return;
        }
        throw new EntidadeNaoEncontradaException("Usuario com Id %d não encontrado.".formatted(id));
    }

    @Override
    public Usuario atualizarImagemPerfil(Integer id, String imagemUrl) {
        UsuarioEntity usuarioExistente = usuarioRepository.findByIdAndIsAtivoTrue(id);

        if (id == null || imagemUrl == null || imagemUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("ID do usuário e URL da imagem são obrigatórios");
        }

        if (usuarioExistente == null) {
            throw new EntidadeNaoEncontradaException("Usuario com Id %d não encontrado.".formatted(id));
        }

        usuarioExistente.setImagemUrl(imagemUrl);
        UsuarioEntity entidadeSalva =  usuarioRepository.save(usuarioExistente);

        return usuarioMapper.toDomain(entidadeSalva);
    }
}
