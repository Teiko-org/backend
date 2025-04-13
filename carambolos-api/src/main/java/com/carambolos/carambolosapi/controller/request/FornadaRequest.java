package com.carambolos.carambolosapi.controller.request;

import com.carambolos.carambolosapi.model.Fornada;
import java.time.LocalDate;

public record FornadaRequest(
        Integer id,
        LocalDate dataInicio,
        LocalDate dataFim
){

    public Fornada toEntity() {
        Fornada fornada = new Fornada();
        fornada.setDataInicio(dataInicio);
        fornada.setDataFim(dataFim);
        return fornada;
    }

}
