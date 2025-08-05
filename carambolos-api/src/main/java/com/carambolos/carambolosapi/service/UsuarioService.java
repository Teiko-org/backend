package com.carambolos.carambolosapi.service;

import com.carambolos.carambolosapi.controller.response.UsuarioTokenDTO;
import com.carambolos.carambolosapi.exception.CredenciaisInvalidasException;
import com.carambolos.carambolosapi.exception.EntidadeJaExisteException;
import com.carambolos.carambolosapi.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.model.Usuario;
import com.carambolos.carambolosapi.repository.UsuarioRepository;
import com.carambolos.carambolosapi.utils.GerenciadorTokenJwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private GerenciadorTokenJwt gerenciadorTokenJwt;

    @Autowired
    private AuthenticationManager authenticationManager;

    public List<Usuario> listar() {
        return usuarioRepository.findAllByIsAtivoTrue();
    }

    public Usuario buscarPorId(Integer id) {
        Usuario usuarioExistente = usuarioRepository.findByIdAndIsAtivoTrue(id);
        if (usuarioExistente != null) {
            return usuarioExistente;
        }
        throw new EntidadeNaoEncontradaException("Usuario com Id %d não encontrado.".formatted(id));
    }

    public Usuario atualizar(Integer id, Usuario usuario) {
       Usuario usuarioExistente = usuarioRepository.findByIdAndIsAtivoTrue(id);

       if (usuarioExistente == null) {
           throw new EntidadeNaoEncontradaException("Usuario com Id %d não encontrado.".formatted(id));
       }

       boolean existePorContato = usuarioRepository.existsByContatoAndIdNotAndIsAtivoTrue(usuario.getContato(), id);

       if (existePorContato) {
           throw new EntidadeJaExisteException("Esse contato já existe no sistema.");
       }

        if (usuario.getSenha() == null || usuario.getSenha().trim().isEmpty()) {
            usuario.setSenha(usuarioExistente.getSenha());
        } else if (!usuario.getSenha().equals(usuarioExistente.getSenha())) {
            String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
            usuario.setSenha(senhaCriptografada);
        }

       usuario.setId(id);
       return usuarioRepository.save(usuario);
    }

    public Usuario atualizarDadosPessoais(Integer id, Usuario usuario) {
        Usuario usuarioExistente = usuarioRepository.findByIdAndIsAtivoTrue(id);

        if (usuarioExistente == null) {
            throw new EntidadeNaoEncontradaException("Usuario com Id %d não encontrado.".formatted(id));
        }

        boolean existePorContato = usuarioRepository.existsByContatoAndIdNotAndIsAtivoTrue(usuario.getContato(), id);

        if (existePorContato) {
            throw new EntidadeJaExisteException("Esse contato já existe no sistema.");
        }

        // Atualizar apenas dados pessoais, mantendo senha e outros campos
        usuarioExistente.setNome(usuario.getNome());
        usuarioExistente.setContato(usuario.getContato());
        usuarioExistente.setDataNascimento(usuario.getDataNascimento());
        usuarioExistente.setGenero(usuario.getGenero());

        return usuarioRepository.save(usuarioExistente);
    }

    public Usuario cadastrar(Usuario usuario)  {
        if (usuarioRepository.findByContatoAndIsAtivoTrue(usuario.getContato()).isPresent()) {
            throw new EntidadeJaExisteException("Esse telefone já está em uso.");
        }

        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);

        return usuarioRepository.save(usuario);
    }

    public UsuarioTokenDTO autenticar(Usuario usuario) {

        final UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(
                usuario.getContato(), usuario.getSenha());

        final Authentication authentication = this.authenticationManager.authenticate(credentials);

        Usuario usuarioAutenticado =
                usuarioRepository.findByContatoAndIsAtivoTrue(usuario.getContato())
                        .orElseThrow(
                                () -> new EntidadeNaoEncontradaException("Contato do usuário não cadastrado")
                        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final String token = gerenciadorTokenJwt.generateToken(authentication);

        return UsuarioTokenDTO.toTokenDTO(usuarioAutenticado, token);
    }

    public void alterarSenha(Integer id, String senhaAtual, String novaSenha) {
        Usuario usuario = usuarioRepository.findByIdAndIsAtivoTrue(id);
        if (usuario == null) {
            throw new EntidadeNaoEncontradaException("Usuário não encontrado.");
        }
        if (!passwordEncoder.matches(senhaAtual, usuario.getSenha())) {
            throw new CredenciaisInvalidasException("Senha atual incorreta.");
        }
        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuarioRepository.save(usuario);
    }

    public void deletar(Integer id) {
        Usuario usuario = usuarioRepository.findByIdAndIsAtivoTrue(id);
        if (usuario != null) {
            usuario.setAtivo(false);
            usuarioRepository.save(usuario);
            return;
        }
        throw new EntidadeNaoEncontradaException("Usuario com Id %d não encontrado.".formatted(id));
    }

    public Usuario atualizarImagemPerfil(Integer id, String imagemUrl) {
        Usuario usuarioExistente = usuarioRepository.findByIdAndIsAtivoTrue(id);

        if (id == null || imagemUrl == null || imagemUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("ID do usuário e URL da imagem são obrigatórios");
        }

        if (usuarioExistente == null) {
            throw new EntidadeNaoEncontradaException("Usuario com Id %d não encontrado.".formatted(id));
        }

        usuarioExistente.setImagemUrl(imagemUrl);
        return usuarioRepository.save(usuarioExistente);
    }

}
