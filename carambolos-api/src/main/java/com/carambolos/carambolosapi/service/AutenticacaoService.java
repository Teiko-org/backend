package com.carambolos.carambolosapi.service;

import com.carambolos.carambolosapi.controller.request.UsuarioDetalhesDto;
import com.carambolos.carambolosapi.model.Usuario;
import com.carambolos.carambolosapi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Usuario> usuarioOpt = usuarioRepository.findByContatoAndIsAtivoTrue(username);

        if (usuarioOpt.isEmpty()) {
            throw new UsernameNotFoundException(String.format("contato: %s não encontrado", username));
        }

        return new UsuarioDetalhesDto(usuarioOpt.get());
    }

}
