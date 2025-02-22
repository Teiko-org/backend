package com.carambolos.carambolosapi.service;

import com.carambolos.carambolosapi.model.Usuario;
import com.carambolos.carambolosapi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(Integer id) {
        return usuarioRepository.findById(id);
    }

    public Usuario atualizar(Integer id, Usuario usuario) {
       return usuarioRepository.findById(id)
               .map(usuarioExistente -> {
                   usuario.setId(id);
                   return usuarioRepository.save(usuario);
               }).orElse(null);
    }

    public Usuario cadastrar(Usuario usuario)  {
        return usuarioRepository.save(usuario);
    }

    public void deletar(Integer id) {
        usuarioRepository.deleteById(id);
    }

}
