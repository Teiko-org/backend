package com.carambolos.carambolosapi.infrastructure.gateways.impl;

import com.carambolos.carambolosapi.application.gateways.AdicionalDecoracaoGateway;
import com.carambolos.carambolosapi.domain.entity.AdicionalDecoracao;
import com.carambolos.carambolosapi.domain.entity.AdicionalDecoracaoSummary;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.AdicionalDecoracaoMapper;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.AdicionalDecoracaoEntity;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.AdicionalDecoracaoRepository;
import com.carambolos.carambolosapi.infrastructure.persistence.projection.AdicionalDecoracaoProjection;

import java.util.List;

public class AdicionalDecoracaoGatewayImpl implements AdicionalDecoracaoGateway {
    private final AdicionalDecoracaoRepository repository;
    private final AdicionalDecoracaoMapper mapper;

    public AdicionalDecoracaoGatewayImpl(AdicionalDecoracaoRepository repository, AdicionalDecoracaoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<AdicionalDecoracaoSummary> buscarTodosAdicionaisPorDecoracao() {
        List<AdicionalDecoracaoProjection> projections = repository.findAllAdicionaisByDecoracao();

        return mapper.toDomain(projections);
    }

    @Override
    public AdicionalDecoracao salvar(Integer decoracaoId, Integer adicionalId) {
        AdicionalDecoracaoEntity entity = new AdicionalDecoracaoEntity();
        entity.setDecoracaoId(decoracaoId);
        entity.setAdicionalId(adicionalId);

        AdicionalDecoracaoEntity entitySalva = repository.save(entity);

        return mapper.toDomain(entitySalva);
    }
}
