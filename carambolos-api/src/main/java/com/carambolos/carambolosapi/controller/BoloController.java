package com.carambolos.carambolosapi.controller;

import com.carambolos.carambolosapi.entities.RecheioExclusivoProjection;
import com.carambolos.carambolosapi.entities.request.RecheioExclusivoRequest;
import com.carambolos.carambolosapi.entities.request.RecheioUnitarioRequest;
import com.carambolos.carambolosapi.entities.response.RecheioExclusivoResponse;
import com.carambolos.carambolosapi.entities.response.RecheioUnitarioResponse;
import com.carambolos.carambolosapi.model.RecheioExclusivo;
import com.carambolos.carambolosapi.model.RecheioUnitario;
import com.carambolos.carambolosapi.service.BoloService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.carambolos.carambolosapi.entities.request.RecheioUnitarioRequest.toRecheioUnitario;

@RestController
@RequestMapping("/bolos")
public class BoloController {

    @Autowired
    private BoloService boloService;

    @PostMapping("/recheio-unitario")
    public ResponseEntity<RecheioUnitarioResponse> cadastrarRecheioUnitario(
            @Valid
            @RequestBody RecheioUnitarioRequest request
    ) {
        RecheioUnitario recheioUnitario = toRecheioUnitario(request);
        RecheioUnitario recheioSalvo = boloService.cadastrarRecheioUnitario(recheioUnitario);
        return ResponseEntity.status(201).body(
                RecheioUnitarioResponse.toRecheioUnitarioResponse(recheioSalvo)
        );
    }

    @GetMapping("/recheio-unitario")
    public ResponseEntity<List<RecheioUnitarioResponse>> listarRecheioUnitario() {
        List<RecheioUnitario> recheiosUnitarios = boloService.listarRecheiosUnitarios();
        return ResponseEntity.status(200).body(
                RecheioUnitarioResponse.toRecheioUnitarioResponse(recheiosUnitarios)
        );
    }

    @GetMapping("/recheio-unitario/{id}")
    public ResponseEntity<RecheioUnitarioResponse> buscarPorId(
            @PathVariable Integer id
    ) {
        RecheioUnitario recheioUnitario = boloService.buscarPorId(id);
        return ResponseEntity.status(200).body(
                RecheioUnitarioResponse.toRecheioUnitarioResponse(recheioUnitario)
        );
    }

    @PutMapping("/recheio-unitario/{id}")
    public ResponseEntity<RecheioUnitarioResponse> atualizarRecheioUnitario(
            @RequestBody RecheioUnitarioRequest request,
            @PathVariable Integer id
    ) {
        RecheioUnitario recheioUnitario = RecheioUnitarioRequest.toRecheioUnitario(request);
        RecheioUnitario recheioCadastrado = boloService.atualizarRecheioUnitario(recheioUnitario, id);
        return ResponseEntity.status(200).body(
                RecheioUnitarioResponse.toRecheioUnitarioResponse(recheioCadastrado)
        );
    }

    @DeleteMapping("/recheio-unitario/{id}")
    public ResponseEntity<Void> deletarRecheioUnitario(
            @PathVariable Integer id
    ) {
        boloService.deletarRecheioUnitario(id);
        return ResponseEntity.status(204).build();
    }

    @PostMapping("/recheio-exclusivo")
    public ResponseEntity<RecheioExclusivoResponse> cadastrarRecheioExclusivo(
            @RequestBody RecheioExclusivoRequest request
    ) {
        RecheioExclusivo recheioExclusivo = RecheioExclusivoRequest.toRecheioExclusivo(request);
        RecheioExclusivoProjection recheioSalvo = boloService.cadastrarRecheioExclusivo(recheioExclusivo);
        return ResponseEntity.status(200).body(
                RecheioExclusivoResponse.toRecheioExclusivoResponse(recheioSalvo)
        );
    }

    @GetMapping("/recheio-exclusivo/{id}")
    ResponseEntity<RecheioExclusivoResponse> buscarRecheioExclusivoPorId(
            @PathVariable Integer id
    ) {
        RecheioExclusivoProjection projection = boloService.buscarRecheioExclusivoPorId(id);
        return ResponseEntity.status(200).body(
                RecheioExclusivoResponse.toRecheioExclusivoResponse(projection)
        );
    }

    @GetMapping("/recheio-exclusivo")
    ResponseEntity<List<RecheioUnitarioResponse>> listarRecheiosExclusivos() {

    }
}
