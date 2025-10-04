package com.carambolos.carambolosapi.infrastructure.gateways.impl;

import com.carambolos.carambolosapi.application.gateways.FornadaDaVezGateway;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.FornadaDaVez;
import com.carambolos.carambolosapi.infrastructure.gateways.mapperEntity.FornadaDaVezEntityMapper;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.FornadaDaVezRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class FornadaDaVezGatewayImpl implements FornadaDaVezGateway {
    private final FornadaDaVezRepository repository;

    public FornadaDaVezGatewayImpl(FornadaDaVezRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<FornadaDaVez> findById(Integer id) {
        return repository.findById(id).map(FornadaDaVezEntityMapper::toDomain);
    }

    @Override
    public FornadaDaVez save(FornadaDaVez fornadaDaVez) {
        var saved = repository.save(FornadaDaVezEntityMapper.toEntity(fornadaDaVez));
        return FornadaDaVezEntityMapper.toDomain(saved);
    }
}
