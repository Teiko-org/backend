package com.carambolos.carambolosapi.controller;

import com.carambolos.carambolosapi.controller.dto.LoginRequestDTO;
import com.carambolos.carambolosapi.controller.dto.UsuarioRequestDTO;
import com.carambolos.carambolosapi.controller.dto.UsuarioResponseDTO;
import com.carambolos.carambolosapi.model.Usuario;
import com.carambolos.carambolosapi.service.UsuarioService;
import jakarta.validation.Valid;
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
    public ResponseEntity<List<UsuarioResponseDTO>> listar(){
        List<Usuario> usuarios = usuarioService.listar();
        if (usuarios.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        List<UsuarioResponseDTO> usuariosResponse = usuarios.stream().map(UsuarioResponseDTO::toResponseDTO).toList();
        return ResponseEntity.status(200).body(usuariosResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Integer id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        UsuarioResponseDTO usuarioResponse = UsuarioResponseDTO.toResponseDTO(usuario);
        return ResponseEntity.status(200).body(usuarioResponse);
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> cadastrar(@Valid @RequestBody UsuarioRequestDTO usuarioRequest) {
        Usuario usuario = UsuarioRequestDTO.toEntity(usuarioRequest);
        Usuario usuarioRegistrado = usuarioService.cadastrar(usuario);
        UsuarioResponseDTO usuarioResponse = UsuarioResponseDTO.toResponseDTO(usuarioRegistrado);
        return ResponseEntity.status(201).body(usuarioResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        usuarioService.login(loginRequestDTO.getEmail(), loginRequestDTO.getSenha());
        return ResponseEntity.status(200).body("Usuário autenticado com sucesso");
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizar(@PathVariable Integer id, @Valid @RequestBody UsuarioRequestDTO usuarioRequest) {
        Usuario usuario = UsuarioRequestDTO.toEntity(usuarioRequest);
        Usuario usuarioAtualizado = usuarioService.atualizar(id, usuario);
        UsuarioResponseDTO usuarioResponse = UsuarioResponseDTO.toResponseDTO(usuarioAtualizado);
        return ResponseEntity.status(200).body(usuarioResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        usuarioService.deletar(id);
        return ResponseEntity.status(204).build();
    }
}
