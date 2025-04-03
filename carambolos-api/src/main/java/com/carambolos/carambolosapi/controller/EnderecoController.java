package com.carambolos.carambolosapi.controller;

import com.carambolos.carambolosapi.controller.dto.EnderecoRequestDTO;
import com.carambolos.carambolosapi.controller.dto.EnderecoResponseDTO;
import com.carambolos.carambolosapi.model.Endereco;
import com.carambolos.carambolosapi.service.EnderecoService;
import com.carambolos.carambolosapi.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enderecos")
public class EnderecoController {

    @Autowired
    private EnderecoService enderecoService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<EnderecoResponseDTO>> listar() {
        List<Endereco> enderecos = enderecoService.listar();
        if (enderecos.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        List<EnderecoResponseDTO> enderecosResponse = enderecos.stream().map(EnderecoResponseDTO::toResponseDTO).toList();
        return ResponseEntity.status(200).body(enderecosResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnderecoResponseDTO> buscarPorId(
            @PathVariable Integer id
    ) {
        Endereco endereco = enderecoService.buscarPorId(id);
        EnderecoResponseDTO enderecoResponse = EnderecoResponseDTO.toResponseDTO(endereco);
        return ResponseEntity.status(200).body(enderecoResponse);
    }

    @PostMapping
    public ResponseEntity<EnderecoResponseDTO> cadastrar(
            @Valid @RequestBody EnderecoRequestDTO enderecoRequest
    ) {
        Endereco endereco = EnderecoRequestDTO.toEntity(enderecoRequest);
        Endereco enderecoRegistrado = enderecoService.cadastrar(endereco);
        EnderecoResponseDTO enderecoResponse = EnderecoResponseDTO.toResponseDTO(enderecoRegistrado);
        return ResponseEntity.status(201).body(enderecoResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnderecoResponseDTO> atualizar(
            @PathVariable Integer id,
            @RequestBody EnderecoRequestDTO enderecoRequest
    ) {
        Endereco endereco = EnderecoRequestDTO.toEntity(enderecoRequest);
        Endereco enderecoAtualizado = enderecoService.atualizar(id, endereco);
        EnderecoResponseDTO enderecoResponse = EnderecoResponseDTO.toResponseDTO(enderecoAtualizado);
        return ResponseEntity.status(200).body(enderecoResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Integer id
    ) {
        enderecoService.deletar(id);
        return ResponseEntity.status(204).build();
    }
}
