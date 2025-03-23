package com.carambolos.carambolosapi.controller;

import com.carambolos.carambolosapi.model.Endereco;
import com.carambolos.carambolosapi.model.Usuario;
import com.carambolos.carambolosapi.service.EnderecoService;
import com.carambolos.carambolosapi.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/enderecos")
public class EnderecoController {

    @Autowired
    private EnderecoService enderecoService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<Endereco>> listar() {
        List<Endereco> enderecos = enderecoService.listar();
        if (enderecos.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(200).body(enderecos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Endereco> buscarPorId(
            @PathVariable Integer id
    ) {
        return ResponseEntity.status(200).body(enderecoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Endereco> cadastrar(
            @Valid @RequestBody Endereco endereco
    ) {
        return ResponseEntity.status(201).body(enderecoService.cadastrar(endereco));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Endereco> atualizar(
            @PathVariable Integer id,
            @RequestBody Endereco endereco
    ) {
            return ResponseEntity.status(200).body(enderecoService.atualizar(id, endereco));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Integer id
    ) {
        enderecoService.deletar(id);
        return ResponseEntity.status(204).build();
    }
}
