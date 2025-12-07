package com.carambolos.carambolosapi.infrastructure.web.response;

import java.util.Arrays;
import java.util.List;

public record PedidosPendentesPorRecheioResponse(
    String nome,
    Integer quantidade,
    List<Integer> pedidos
) {
    public static PedidosPendentesPorRecheioResponse fromMap(java.util.Map<String, Object> map) {
        String pedidosStr = (String) map.get("pedidos");
        List<Integer> pedidosList = pedidosStr != null && !pedidosStr.isEmpty()
            ? Arrays.stream(pedidosStr.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .toList()
            : List.of();
        
        return new PedidosPendentesPorRecheioResponse(
            (String) map.get("nome"),
            ((Number) map.get("quantidade")).intValue(),
            pedidosList
        );
    }
}
