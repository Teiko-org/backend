package com.carambolos.carambolosapi.service;

import com.carambolos.carambolosapi.model.Usuario;
import com.carambolos.carambolosapi.repository.UsuarioRepository;
import com.carambolos.carambolosapi.utils.JwtUtil;
import com.carambolos.carambolosapi.utils.PasswordEncoderUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Optional<Usuario> buscarPorId(Integer id) {
        return usuarioRepository.findById(id);
    }

    public Usuario atualizar(Integer id, Usuario usuario) {
       return usuarioRepository.findById(id)
               .map(usuarioExistente -> {
                   usuarioExistente.setNome(usuario.getNome());
                   usuarioExistente.setEmail(usuario.getEmail());

                   String senhaAntiga = usuarioExistente.getSenha();
                   String senhaNova = usuario.getSenha();

                   if (senhaNova != null && !senhaNova.equals(senhaAntiga)) {
                       String senhaCriptografada = passwordEncoderUtil.senhaCodificada().encode(senhaNova);
                       usuarioExistente.setSenha(senhaCriptografada);
                   }

                   return usuarioRepository.save(usuarioExistente);
               }).orElse(null);
    }

    public Usuario cadastrar(Usuario usuario)  {
        String senhaCriptografada = passwordEncoderUtil.senhaCodificada().encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);
        return usuarioRepository.save(usuario);
    }

    public Usuario login(String email, String senha) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoderUtil.senhaCodificada().matches(senha, usuario.getSenha())) {
            throw new RuntimeException("Senha incorreta");
        }

        return usuario;

    }

    public void deletar(Integer id) {
        usuarioRepository.deleteById(id);
    }

}
