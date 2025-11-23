package com.carambolos.carambolosapi.domain.entity;

public class AdicionalDecoracao {
    private Integer id;
    private Integer decoracaoId;
    private Integer adicionalId;

    public AdicionalDecoracao() {
    }

    public AdicionalDecoracao(Integer id, Integer decoracaoId, Integer adicionalId) {
        this.id = id;
        this.decoracaoId = decoracaoId;
        this.adicionalId = adicionalId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDecoracaoId() {
        return decoracaoId;
    }

    public void setDecoracaoId(Integer decoracaoId) {
        this.decoracaoId = decoracaoId;
    }

    public Integer getAdicionalId() {
        return adicionalId;
    }

    public void setAdicionalId(Integer adicionalId) {
        this.adicionalId = adicionalId;
    }
}
