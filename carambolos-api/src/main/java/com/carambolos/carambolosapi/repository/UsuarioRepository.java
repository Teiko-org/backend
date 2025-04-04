package com.carambolos.carambolosapi.repository;

import com.carambolos.carambolosapi.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByContato(String contato);
    boolean existsById(Integer id);
    boolean existsByEmailAndIdNot(String email, Integer id);
    boolean existsByContatoAndIdNot(String contato, Integer id);
}
