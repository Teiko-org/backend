package com.carambolos.carambolosapi.application.usecases;

import com.carambolos.carambolosapi.infrastructure.web.request.UsuarioDetalhesDto;
import com.carambolos.carambolosapi.domain.entity.Usuario;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.UsuarioRepository;
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
