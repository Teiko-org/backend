package com.carambolos.carambolosapi.infrastructure.persistence.jpa;

import com.carambolos.carambolosapi.infrastructure.persistence.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Integer> {
    Optional<UsuarioEntity> findByContatoAndIsAtivoTrue(String contato);
    List<UsuarioEntity> findAllByIsAtivoTrue();
    UsuarioEntity findByIdAndIsAtivoTrue(Integer id);
    boolean existsById(Integer id);
    boolean existsByContatoAndIdNotAndIsAtivoTrue(String contato, Integer id);
}
