package com.carambolos.carambolosapi.model;

import jakarta.persistence.*;

@Entity
public class RecheioPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private RecheioUnitario recheio_unitario_id1;

    @ManyToOne
    private RecheioUnitario recheio_unitario_id2;

    @OneToOne
    private RecheioExclusivo recheio_exclusivo;
}
