package com.carambolos.carambolosapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "produto_fornada")
@Schema(description = "Entidade que representa um produto feito em uma fornada.")
public class ProdutoFornada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID do produto da fornada", example = "1")
    private Integer id;

    @Column(name = "produto", unique = true)
    @NotBlank
    @Schema(description = "Nome do produto", example = "Cupcake de Chocolate")
    private String produto;

    @Column(name = "descricao")
    @Schema(description = "Descrição do produto", example = "Cupcake artesanal de chocolate belga")
    private String descricao;

    @Column(name = "valor")
    @NotNull
    @Schema(description = "Valor do produto", example = "7.50")
    private Double valor;

    @Column(name = "is_ativo")
    @Schema(description = "Indica se o produto está ativo ou não", example = "true")
    private boolean isAtivo = true;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public boolean isAtivo() {
        return isAtivo;
    }

    public void setAtivo(boolean ativo) {
        isAtivo = ativo;
    }
}
