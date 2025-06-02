package com.carambolos.carambolosapi.repository;

import com.carambolos.carambolosapi.model.Decoracao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DecoracaoRepository extends JpaRepository<Decoracao, Integer> {
    List<Decoracao> findByIsAtivoTrue();
}
