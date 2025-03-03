package com.carambolos.carambolosapi.controller;

import com.carambolos.carambolosapi.controller.request.LoginRequest;
import com.carambolos.carambolosapi.model.Usuario;
import com.carambolos.carambolosapi.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<Usuario>> listar(){
        List<Usuario> usuarios = usuarioService.listar();

        if (usuarios.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.of(usuarioService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario) {
        Usuario usuarioRegistrado = usuarioService.cadastrar(usuario);
        return ResponseEntity.status(201).body(usuarioRegistrado);
    }

    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestBody LoginRequest loginRequest) {
        try {
            Usuario usuarioAutenticado = usuarioService
                    .login(loginRequest.getEmail(), loginRequest.getSenha());

            return ResponseEntity.status(200).body(usuarioAutenticado);

        } catch (RuntimeException e) {
            String mensagemErro = e.getMessage();
            if (mensagemErro.contains("Usuário não encontrado") || mensagemErro.contains("Senha incorreta")) {
                return ResponseEntity.status(401).build();
            }
        }

        return ResponseEntity.status(500).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizar(@PathVariable Integer id, @RequestBody Usuario usuario) {
        Usuario usuarioAtualizado = usuarioService.atualizar(id, usuario);

        if (usuarioAtualizado == null) {
            return ResponseEntity.status(404).build();
        }

        return ResponseEntity.status(200).body(usuarioAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        Boolean usuarioDeletado = usuarioService.deletar(id);

        if (usuarioDeletado) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(404).build();
    }


}
