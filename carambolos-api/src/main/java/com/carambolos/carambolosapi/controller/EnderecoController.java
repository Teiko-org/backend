package com.carambolos.carambolosapi.controller;

import com.carambolos.carambolosapi.model.Endereco;
import com.carambolos.carambolosapi.model.Usuario;
import com.carambolos.carambolosapi.service.EnderecoService;
import com.carambolos.carambolosapi.service.UsuarioService;
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
        return ResponseEntity.of(enderecoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Endereco> buscarPorId(
            @PathVariable Integer id
    ) {
        return ResponseEntity.of(enderecoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Endereco> cadastrar(
            @RequestBody Endereco endereco
    ) {
        try {
            if (endereco.getUsuario() != null) {
                Optional<Usuario> usuarioExiste = usuarioService.buscarPorId(endereco.getUsuario());

                if (usuarioExiste.isEmpty()) {
                    return ResponseEntity.status(400).build();
                }
            }

            return ResponseEntity.status(201).body(enderecoService.cadastrar(endereco));
        } catch (Exception e) {
            return ResponseEntity.status(409).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Endereco> atualizar(
            @PathVariable Integer id,
            @RequestBody Endereco endereco
    ) {
        try {
            endereco.setId(id);

            if (!enderecoService.existePorId(id)) {
                return ResponseEntity.status(404).build();
            }

            return ResponseEntity.status(200).body(enderecoService.atualizar(endereco));
        } catch (Exception e) {
            return ResponseEntity.status(409).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Integer id
    ) {
        if (!enderecoService.existePorId(id)) {
            return ResponseEntity.status(404).build();
        }

        enderecoService.deletar(id);
        return ResponseEntity.status(200).build();
    }
}
