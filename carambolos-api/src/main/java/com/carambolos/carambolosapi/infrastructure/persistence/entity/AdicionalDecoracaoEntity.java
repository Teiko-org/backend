package com.carambolos.carambolosapi.infrastructure.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "adicional_decoracao")
public class AdicionalDecoracaoEntity {
    @Id
    private Integer id;
    private Integer decoracaoId;
    private Integer adicionalId;

    public AdicionalDecoracaoEntity() {
    }

    public AdicionalDecoracaoEntity(Integer id, Integer decoracaoId, Integer adicionalId) {
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
