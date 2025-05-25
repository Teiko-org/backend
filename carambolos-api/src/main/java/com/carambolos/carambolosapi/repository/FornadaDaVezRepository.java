package com.carambolos.carambolosapi.repository;

import com.carambolos.carambolosapi.model.FornadaDaVez;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FornadaDaVezRepository extends JpaRepository<FornadaDaVez, Integer> {
    List<FornadaDaVez> findByFornada(Integer id);
}
