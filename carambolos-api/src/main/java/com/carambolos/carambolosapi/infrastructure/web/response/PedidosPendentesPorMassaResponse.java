package com.carambolos.carambolosapi.infrastructure.web.response;

import java.util.Arrays;
import java.util.List;

public record PedidosPendentesPorMassaResponse(
    String nome,
    Integer quantidade,
    List<Integer> pedidos
) {
    public static PedidosPendentesPorMassaResponse fromMap(java.util.Map<String, Object> map) {
        String pedidosStr = (String) map.get("pedidos");
        List<Integer> pedidosList = pedidosStr != null && !pedidosStr.isEmpty()
            ? Arrays.stream(pedidosStr.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .toList()
            : List.of();
        
        return new PedidosPendentesPorMassaResponse(
            (String) map.get("nome"),
            ((Number) map.get("quantidade")).intValue(),
            pedidosList
        );
    }
}
