package com.carambolos.carambolosapi.service;

import com.carambolos.carambolosapi.exception.CredenciaisInvalidasException;
import com.carambolos.carambolosapi.exception.EntidadeJaExisteException;
import com.carambolos.carambolosapi.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.model.Usuario;
import com.carambolos.carambolosapi.repository.UsuarioRepository;
import com.carambolos.carambolosapi.utils.JwtUtil;
import com.carambolos.carambolosapi.utils.PasswordEncoderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoderUtil passwordEncoderUtil;

    @Autowired
    private JwtUtil jwtUtil;

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuario com Id %d não encontrado.".formatted(id)));
    }

    public Usuario atualizar(Integer id, Usuario usuario) {
       Usuario usuarioExistente = usuarioRepository.findById(id)
               .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuario com Id %d não encontrado.".formatted(id)));

       boolean existePorEmail = usuarioRepository.existsByEmailAndIdNot(usuario.getEmail(), id);
       boolean existePorContato = usuarioRepository.existsByContatoAndIdNot(usuario.getContato(), id);

       if (existePorEmail) {
           throw new EntidadeJaExisteException("Esse e-mail já existe no sistema.");
       }

       if (existePorContato) {
           throw new EntidadeJaExisteException("Esse contato já existe no sistema.");
       }

       if (usuario.getNome() != null) {
            usuarioExistente.setNome(usuario.getNome());
       }
       if (usuario.getEmail() != null) {
            usuarioExistente.setEmail(usuario.getEmail());
       }
       if (usuario.getContato() != null) {
            usuarioExistente.setContato(usuario.getContato());
       }
       if (usuario.getSenha() != null) {
            usuarioExistente.setSenha(usuario.getSenha());
       }

       usuario.setId(id);
       return usuarioRepository.save(usuarioExistente);
    }

    public Usuario cadastrar(Usuario usuario)  {
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new EntidadeJaExisteException("Esse e-mail já está em uso.");
        }

        if (usuarioRepository.findByContato(usuario.getContato()).isPresent()) {
            throw new EntidadeJaExisteException("Esse telefone já está em uso.");
        }

        return usuarioRepository.save(usuario);
    }

    public Usuario login(String email, String senha) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new CredenciaisInvalidasException("Usuário com e-mail %s não encontrado".formatted(email)));

        if (!usuario.getSenha().equals(senha)) {
            throw new CredenciaisInvalidasException("Senha incorreta");
        }

        return usuario;
    }

    public void deletar(Integer id) {
        if (!usuarioRepository.existsById(id)) {
            throw new EntidadeNaoEncontradaException("Usuario com Id %d não encontrado.".formatted(id));
        }
            usuarioRepository.deleteById(id);
    }

}
