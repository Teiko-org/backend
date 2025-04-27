package com.carambolos.carambolosapi.controller.request;

import com.carambolos.carambolosapi.model.Cobertura;

public record CoberturaRequestDTO(
        Integer id,
        String cor,
        String descricao
) {
    public static Cobertura toCobertura(CoberturaRequestDTO request) {
        if (request == null) {
            return null;
        }
        Cobertura cobertura = new Cobertura();

        cobertura.setCor(request.cor);
        cobertura.setDescricao(request.descricao);
        return cobertura;
    }
}
