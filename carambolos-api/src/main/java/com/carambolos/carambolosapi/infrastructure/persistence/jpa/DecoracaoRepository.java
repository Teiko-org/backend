package com.carambolos.carambolosapi.infrastructure.persistence.jpa;

import com.carambolos.carambolosapi.domain.entity.Decoracao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DecoracaoRepository extends JpaRepository<Decoracao, Integer> {
    List<Decoracao> findByIsAtivoTrue();

    boolean existsByIdAndIsAtivoTrue(Integer id);

    List<Decoracao> findByIsAtivoTrueAndCategoriaIsNotNull();
}
