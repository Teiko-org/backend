package com.carambolos.carambolosapi.controller.dto;

import com.carambolos.carambolosapi.model.RecheioPedido;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para requisição de criação ou atualização de Recheio Pedido")
public record RecheioPedidoRequestDTO(

        @Schema(description = "ID do recheio exclusivo escolhido", example = "5")
        Integer idExclusivo,

        @Schema(description = "ID do primeiro recheio unitário escolhido", example = "10")
        Integer idUnitario1,

        @Schema(description = "ID do segundo recheio unitário escolhido", example = "11")
        Integer idUnitario2
){
    public static RecheioPedido toRecheioPedido(RecheioPedidoRequestDTO request) {
        if(request == null) {
            return null;
        }

        RecheioPedido recheioPedido = new RecheioPedido();
        recheioPedido.setRecheioExclusivo(request.idExclusivo());
        recheioPedido.setRecheioUnitarioId1(request.idUnitario1());
        recheioPedido.setRecheioUnitarioId2(request.idUnitario2);

        return recheioPedido;
    }
}
