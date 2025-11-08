package com.carambolos.carambolosapi.infrastructure.web.response;

import com.carambolos.carambolosapi.infrastructure.web.request.CartItemDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "DTO de resposta contendo informações do carrinho do usuário")
public class CarrinhoResponseDTO {
    @Schema(description = "Lista de itens do carrinho")
    private List<CartItemDTO> itens;

    @Schema(description = "Data e hora da última atualização do carrinho")
    private LocalDateTime dataUltimaAtualizacao;

    public CarrinhoResponseDTO() {
    }

    public CarrinhoResponseDTO(List<CartItemDTO> itens, LocalDateTime dataUltimaAtualizacao) {
        this.itens = itens;
        this.dataUltimaAtualizacao = dataUltimaAtualizacao;
    }

    public List<CartItemDTO> getItens() {
        return itens;
    }

    public void setItens(List<CartItemDTO> itens) {
        this.itens = itens;
    }

    public LocalDateTime getDataUltimaAtualizacao() {
        return dataUltimaAtualizacao;
    }

    public void setDataUltimaAtualizacao(LocalDateTime dataUltimaAtualizacao) {
        this.dataUltimaAtualizacao = dataUltimaAtualizacao;
    }
}