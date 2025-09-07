package com.carambolos.carambolosapi.repository;

import com.carambolos.carambolosapi.model.Fornada;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FornadaRepository extends JpaRepository<Fornada, Integer> {
    boolean existsByIdAndIsAtivoTrue(Integer id);

    List<Fornada> findAllByIsAtivoTrueOrderByDataInicioAsc();

    Optional<Fornada> findTop1ByIsAtivoTrueOrderByDataInicioDesc();

    Optional<Fornada> findTop1ByIsAtivoTrueAndDataInicioAfterOrderByDataInicioAsc(LocalDate dataInicio);

    List<Fornada> findByIsAtivoTrueAndDataInicioBetweenOrderByDataInicioAsc(LocalDate inicio, LocalDate fim);

    // Histórico completo: incluir ativas e encerradas
    List<Fornada> findByDataInicioBetweenOrderByDataInicioAsc(LocalDate inicio, LocalDate fim);
}
