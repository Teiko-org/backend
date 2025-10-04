package com.carambolos.carambolosapi.application.gateways;

import com.carambolos.carambolosapi.infrastructure.persistence.entity.FornadaDaVez;

import java.util.Optional;

public interface FornadaDaVezGateway {
    Optional<FornadaDaVez> findById(Integer id);
    FornadaDaVez save(FornadaDaVez fornadaDaVez);
}


