package com.carambolos.carambolosapi.controller;

import com.carambolos.carambolosapi.model.Endereco;
import com.carambolos.carambolosapi.service.EnderecoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/enderecos")
public class EnderecoController {

    @Autowired
    private EnderecoService enderecoService;

    @PostMapping
    public ResponseEntity<Endereco> cadastrar(
        @RequestBody Endereco endereco
    ) {
        return ResponseEntity.status(201).body(
                enderecoService.cadastrar(endereco)
        );
    }
}
