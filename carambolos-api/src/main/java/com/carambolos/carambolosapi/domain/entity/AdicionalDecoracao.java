package com.carambolos.carambolosapi.domain.entity;

import java.util.List;

public class AdicionalDecoracao {
    private Integer decoracaoId;
    private String nomeDecoracao;
    private List<String> adicionaisPossiveis;

    public AdicionalDecoracao() {
    }

    public AdicionalDecoracao(Integer decoracaoId, String nomeDecoracao, List<String> adicionaisPossiveis) {
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

    public List<String> getAdicionaisPossiveis() {
        return adicionaisPossiveis;
    }

    public void setAdicionaisPossiveis(List<String> adicionaisPossiveis) {
        this.adicionaisPossiveis = adicionaisPossiveis;
    }
}
