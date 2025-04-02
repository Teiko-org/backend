package com.carambolos.carambolosapi.model;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class FornadaDaVez {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

}
