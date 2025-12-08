package com.carambolos.carambolosapi.application.gateways;

import com.carambolos.carambolosapi.domain.entity.AdicionalDecoracao;
import com.carambolos.carambolosapi.domain.entity.AdicionalDecoracaoSummary;

import java.util.List;
import java.util.Optional;

public interface AdicionalDecoracaoGateway {
    List<AdicionalDecoracaoSummary> buscarTodosAdicionaisPorDecoracao();
    Optional<AdicionalDecoracaoSummary> buscarAdicionaisPorDecoracaoId(Integer decoracaoId);
    AdicionalDecoracao salvar(Integer decoracaoId, Integer adicionalId);
}
