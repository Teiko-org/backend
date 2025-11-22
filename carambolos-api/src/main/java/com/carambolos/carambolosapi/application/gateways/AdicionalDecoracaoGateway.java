package com.carambolos.carambolosapi.application.gateways;

import com.carambolos.carambolosapi.domain.entity.AdicionalDecoracao;

import java.util.List;

public interface AdicionalDecoracaoGateway {
    List<AdicionalDecoracao> buscarTodosAdicionaisPorDecoracao();
}
