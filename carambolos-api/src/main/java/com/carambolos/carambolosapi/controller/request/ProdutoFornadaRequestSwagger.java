package com.carambolos.carambolosapi.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

public class ProdutoFornadaRequestSwagger {
    @Schema(description = "Nome do produto", example = "Cupcake de Chocolate")
    public String produto;

    @Schema(description = "Descrição do produto", example = "Cupcake artesanal de chocolate belga")
    public String descricao;

    @Schema(description = "Preço unitário do produto", example = "7.50")
    public Double valor;

    @Schema(description = "Categoria da fornada", example = "Fornada natalina")
    public String categoria;

    @Schema(description = "Imagens do produto da fornada")
    public MultipartFile[] imagens;
}
