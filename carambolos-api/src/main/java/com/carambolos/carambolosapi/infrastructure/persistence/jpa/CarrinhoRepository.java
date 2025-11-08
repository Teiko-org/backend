package com.carambolos.carambolosapi.infrastructure.persistence.jpa;

import com.carambolos.carambolosapi.infrastructure.persistence.entity.CarrinhoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarrinhoRepository extends JpaRepository<CarrinhoEntity, Integer> {
    Optional<CarrinhoEntity> findByUsuarioId(Integer usuarioId);
    void deleteByUsuarioId(Integer usuarioId);
}