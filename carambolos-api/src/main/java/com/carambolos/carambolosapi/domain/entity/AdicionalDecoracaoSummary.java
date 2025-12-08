package com.carambolos.carambolosapi.domain.entity;

import java.util.List;

public class AdicionalDecoracaoSummary {
    private Integer decoracaoId;
    private String nomeDecoracao;
    private List<AdicionalItem> adicionaisPossiveis;

    public AdicionalDecoracaoSummary() {
    }

    public AdicionalDecoracaoSummary(Integer decoracaoId, String nomeDecoracao, List<AdicionalItem> adicionaisPossiveis) {
        this.decoracaoId = decoracaoId;
        this.nomeDecoracao = nomeDecoracao;
        this.adicionaisPossiveis = adicionaisPossiveis;
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

    public List<AdicionalItem> getAdicionaisPossiveis() {
        return adicionaisPossiveis;
    }

    public void setAdicionaisPossiveis(List<AdicionalItem> adicionaisPossiveis) {
        this.adicionaisPossiveis = adicionaisPossiveis;
    }
}
