package com.carambolos.carambolosapi.infrastructure.persistence.jpa;

import com.carambolos.carambolosapi.infrastructure.persistence.entity.DecoracaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DecoracaoRepository extends JpaRepository<DecoracaoEntity, Integer> {
    List<DecoracaoEntity> findByIsAtivoTrue();

    boolean existsByIdAndIsAtivoTrue(Integer id);

    List<DecoracaoEntity> findByIsAtivoTrueAndCategoriaIsNotNull();
}
