package com.carambolos.carambolosapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "fornada_da_vez")
@Schema(description = "Entidade que representa a produção de um produto específico em uma fornada.")
public class FornadaDaVez {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único da FornadaDaVez", example = "1")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "produto_fornada_id", referencedColumnName = "id", nullable = false)
    @NotNull
    @Schema(description = "Produto relacionado à fornada da vez", implementation = ProdutoFornada.class)
    private ProdutoFornada produtoFornada;

    @ManyToOne
    @JoinColumn(name = "fornada_id", referencedColumnName = "id", nullable = false)
    @NotNull
    @Schema(description = "Fornada à qual o produto pertence", implementation = Fornada.class)
    private Fornada fornada;

    @NotNull
    @Min(1)
    @Schema(description = "Quantidade de produtos produzidos na fornada", example = "100")
    private Integer quantidade;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ProdutoFornada getProdutoFornada() {
        return produtoFornada;
    }

    public void setProdutoFornada(ProdutoFornada produtoFornada) {
        this.produtoFornada = produtoFornada;
    }

    public Fornada getFornada() {
        return fornada;
    }

    public void setFornada(Fornada fornada) {
        this.fornada = fornada;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}
