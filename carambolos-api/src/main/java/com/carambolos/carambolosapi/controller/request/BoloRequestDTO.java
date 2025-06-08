package com.carambolos.carambolosapi.controller.request;

import com.carambolos.carambolosapi.model.Bolo;
import com.carambolos.carambolosapi.model.enums.FormatoEnum;
import com.carambolos.carambolosapi.model.enums.TamanhoEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO para requisição de cadastro de bolo.")
public record BoloRequestDTO(
        @Schema(description = "ID do recheio", example = "1")
        @NotNull
        Integer recheioPedidoId,

        @Schema(description = "ID da massa", example = "1")
        @NotNull
        Integer massaId,

        @Schema(description = "ID da cobertura", example = "1")
        @NotNull
        Integer coberturaId,

        Integer decoracaoId,

        @Schema(description = "Formato do bolo", example = "CIRCULO")
        @NotNull
        FormatoEnum formato,

        @Schema(description = "Tamanho do bolo em cm", example = "TAMANHO_12")
        @NotNull
        TamanhoEnum tamanho,

        @Schema(description = "Categoria do bolo", example = "bolo de páscoa")
        String categoria
) {
    @Schema(description = "Método para converter o DTO em uma entidade FornadaDaVez")
    public static Bolo toBolo(BoloRequestDTO request) {
        if (request == null) {
            return null;
        }
        Bolo bolo = new Bolo();
        bolo.setRecheioPedido(request.recheioPedidoId);
        bolo.setMassa(request.massaId);
        bolo.setCobertura(request.coberturaId);
        bolo.setFormato(request.formato);
        bolo.setTamanho(request.tamanho);
        bolo.setCategoria(request.categoria);
        return bolo;
    }
}
