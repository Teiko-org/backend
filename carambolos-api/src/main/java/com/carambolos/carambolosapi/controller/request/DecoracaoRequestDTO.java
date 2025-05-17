package com.carambolos.carambolosapi.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para criação ou atualização de uma decoração")
public record DecoracaoRequestDTO(
        @Schema(description = "URL da imagem de referência", example = "https://azure.com/decoracoes/tema-x.jpg")
        @NotBlank
        @Size(max = 500)
        String imagemReferencia,

        @Schema(description = "Observações da decoração", example = "Tema Frozen, tons de azul")
        @Size(max = 70)
        String observacao
) {}
