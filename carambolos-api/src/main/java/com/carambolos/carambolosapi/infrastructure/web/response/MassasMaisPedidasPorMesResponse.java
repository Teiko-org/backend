package com.carambolos.carambolosapi.infrastructure.web.response;

import java.util.List;
import java.util.Map;

public record MassasMaisPedidasPorMesResponse(
    List<String> labels,
    List<SerieData> serie,
    String massaSelecionada
) {
    public record SerieData(
        String name,
        List<Integer> data
    ) {}
}
