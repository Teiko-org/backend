package com.carambolos.carambolosapi.infrastructure.persistence.jpa;

import com.carambolos.carambolosapi.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByContatoAndIsAtivoTrue(String contato);
    List<Usuario> findAllByIsAtivoTrue();
    Usuario findByIdAndIsAtivoTrue(Integer id);
    boolean existsById(Integer id);
    boolean existsByContatoAndIdNotAndIsAtivoTrue(String contato, Integer id);
}
