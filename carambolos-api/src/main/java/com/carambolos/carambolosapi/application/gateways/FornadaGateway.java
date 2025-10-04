package com.carambolos.carambolosapi.application.gateways;

import com.carambolos.carambolosapi.domain.entity.Fornada;
import java.time.LocalDate;
import java.util.*;

public interface FornadaGateway {
    Fornada save(Fornada fornada);
    boolean existsAtivaById(Integer id);
    List<Fornada> findAll();
    List<Fornada> findAllAtivas();
    List<Fornada> findByDataInicioBetweenOrderByDataInicioAsc(LocalDate inicio, LocalDate fim);
    Optional<Fornada> findTop1ByAtivaTrueOrderByDataInicioDesc();
    List<Fornada> findAllByAtivaTrueOrderByDataInicioAsc();
    Optional<Fornada> findById(Integer id);
}