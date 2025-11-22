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
}
