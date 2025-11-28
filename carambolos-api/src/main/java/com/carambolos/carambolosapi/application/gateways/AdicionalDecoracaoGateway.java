package com.carambolos.carambolosapi.application.gateways;

import com.carambolos.carambolosapi.domain.entity.AdicionalDecoracao;
import com.carambolos.carambolosapi.domain.entity.AdicionalDecoracaoSummary;

import java.util.List;

public interface AdicionalDecoracaoGateway {
    List<AdicionalDecoracaoSummary> buscarTodosAdicionaisPorDecoracao();
    AdicionalDecoracao salvar(Integer decoracaoId, Integer adicionalId);
}
