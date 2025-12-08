package com.carambolos.carambolosapi.infrastructure.persistence.projection;

public class AdicionalDecoracaoProjection {
    private Integer decoracaoId;
    private String nomeDecoracao;
    private String adicionais;
    private String adicionaisIds;

    public AdicionalDecoracaoProjection() {
    }

    public AdicionalDecoracaoProjection(Integer decoracaoId, String nomeDecoracao, String adicionais, String adicionaisIds) {
        this.decoracaoId = decoracaoId;
        this.nomeDecoracao = nomeDecoracao;
        this.adicionais = adicionais;
        this.adicionaisIds = adicionaisIds;
    }

    public Integer getDecoracaoId() {
        return decoracaoId;
    }

    public void setDecoracaoId(Integer decoracaoId) {
        this.decoracaoId = decoracaoId;
    }

    public String getNomeDecoracao() {
        return nomeDecoracao;
    }

    public void setNomeDecoracao(String nomeDecoracao) {
        this.nomeDecoracao = nomeDecoracao;
    }

    public String getAdicionais() {
        return adicionais;
    }

    public void setAdicionais(String adicionais) {
        this.adicionais = adicionais;
    }

    public String getAdicionaisIds() {
        return adicionaisIds;
    }

    public void setAdicionaisIds(String adicionaisIds) {
        this.adicionaisIds = adicionaisIds;
    }
}
