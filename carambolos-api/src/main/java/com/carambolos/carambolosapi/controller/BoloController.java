package com.carambolos.carambolosapi.controller;

import com.carambolos.carambolosapi.controller.request.RecheioPedidoRequestDTO;
import com.carambolos.carambolosapi.controller.response.RecheioPedidoResponseDTO;
import com.carambolos.carambolosapi.model.projection.RecheioExclusivoProjection;
import com.carambolos.carambolosapi.controller.request.RecheioExclusivoRequestDTO;
import com.carambolos.carambolosapi.controller.request.RecheioUnitarioRequestDTO;
import com.carambolos.carambolosapi.controller.response.RecheioExclusivoResponseDTO;
import com.carambolos.carambolosapi.controller.response.RecheioUnitarioResponseDTO;
import com.carambolos.carambolosapi.model.RecheioExclusivo;
import com.carambolos.carambolosapi.model.RecheioPedido;
import com.carambolos.carambolosapi.model.RecheioUnitario;
import com.carambolos.carambolosapi.model.projection.RecheioPedidoProjection;
import com.carambolos.carambolosapi.service.BoloService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.carambolos.carambolosapi.controller.request.RecheioUnitarioRequestDTO.toRecheioUnitario;

@RestController
@RequestMapping("/bolos")
public class BoloController {

    @Autowired
    private BoloService boloService;

    @PostMapping("/recheio-unitario")
    public ResponseEntity<RecheioUnitarioResponseDTO> cadastrarRecheioUnitario(
            @Valid
            @RequestBody RecheioUnitarioRequestDTO request
    ) {
        RecheioUnitario recheioUnitario = toRecheioUnitario(request);
        RecheioUnitario recheioSalvo = boloService.cadastrarRecheioUnitario(recheioUnitario);
        return ResponseEntity.status(201).body(
                RecheioUnitarioResponseDTO.toRecheioUnitarioResponse(recheioSalvo)
        );
    }

    @GetMapping("/recheio-unitario")
    public ResponseEntity<List<RecheioUnitarioResponseDTO>> listarRecheioUnitario() {
        List<RecheioUnitario> recheiosUnitarios = boloService.listarRecheiosUnitarios();
        return ResponseEntity.status(200).body(
                RecheioUnitarioResponseDTO.toRecheioUnitarioResponse(recheiosUnitarios)
        );
    }

    @GetMapping("/recheio-unitario/{id}")
    public ResponseEntity<RecheioUnitarioResponseDTO> buscarRecheioUnitarioPorId(
            @PathVariable Integer id
    ) {
        RecheioUnitario recheioUnitario = boloService.buscarPorId(id);
        return ResponseEntity.status(200).body(
                RecheioUnitarioResponseDTO.toRecheioUnitarioResponse(recheioUnitario)
        );
    }

    @PutMapping("/recheio-unitario/{id}")
    public ResponseEntity<RecheioUnitarioResponseDTO> atualizarRecheioUnitario(
            @RequestBody RecheioUnitarioRequestDTO request,
            @PathVariable Integer id
    ) {
        RecheioUnitario recheioUnitario = RecheioUnitarioRequestDTO.toRecheioUnitario(request);
        RecheioUnitario recheioCadastrado = boloService.atualizarRecheioUnitario(recheioUnitario, id);
        return ResponseEntity.status(200).body(
                RecheioUnitarioResponseDTO.toRecheioUnitarioResponse(recheioCadastrado)
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
    public ResponseEntity<RecheioExclusivoResponseDTO> cadastrarRecheioExclusivo(
            @RequestBody RecheioExclusivoRequestDTO request
    ) {
        RecheioExclusivo recheioExclusivo = RecheioExclusivoRequestDTO.toRecheioExclusivo(request);
        RecheioExclusivoProjection recheioSalvo = boloService.cadastrarRecheioExclusivo(recheioExclusivo);
        return ResponseEntity.status(200).body(
                RecheioExclusivoResponseDTO.toRecheioExclusivoResponse(recheioSalvo)
        );
    }

    @GetMapping("/recheio-exclusivo/{id}")
    public ResponseEntity<RecheioExclusivoResponseDTO> buscarRecheioExclusivoPorId(
            @PathVariable Integer id
    ) {
        RecheioExclusivoProjection projection = boloService.buscarRecheioExclusivoPorId(id);
        return ResponseEntity.status(200).body(
                RecheioExclusivoResponseDTO.toRecheioExclusivoResponse(projection)
        );
    }

    @GetMapping("/recheio-exclusivo")
    public ResponseEntity<List<RecheioExclusivoResponseDTO>> listarRecheiosExclusivos() {
        List<RecheioExclusivoProjection> recheiosEncontrados = boloService.listarRecheiosExclusivos();
        return ResponseEntity.status(200).body(
                RecheioExclusivoResponseDTO.toRecheioExclusivoResponse(recheiosEncontrados)
        );
    }

    @PutMapping("/recheio-exclusivo/{id}")
    public ResponseEntity<RecheioExclusivoResponseDTO> atualizarRecheioExclusivo(
            @PathVariable Integer id,
            @RequestBody RecheioExclusivoRequestDTO request
    ) {
        RecheioExclusivo recheioExclusivo = RecheioExclusivoRequestDTO.toRecheioExclusivo(request);
        RecheioExclusivoProjection recheioSalvo = boloService.editarRecheioExclusivo(recheioExclusivo, id);
        return ResponseEntity.status(200).body(
                RecheioExclusivoResponseDTO.toRecheioExclusivoResponse(recheioSalvo)
        );
    }

    @DeleteMapping("/recheio-exclusivo/{id}")
    public ResponseEntity<Void> excluirRecheioExclusivo(
            @PathVariable Integer id
    ) {
        boloService.excluirRecheioExclusivo(id);
        return ResponseEntity.status(204).build();
    }

    @PostMapping("/recheio-pedido")
    public ResponseEntity<RecheioPedidoResponseDTO> cadastrarRecheioPedido(
            @RequestBody RecheioPedidoRequestDTO request
    ) {
        RecheioPedido recheioPedido = RecheioPedidoRequestDTO.toRecheioPedido(request);
        RecheioPedidoProjection projection = boloService.cadastrarRecheioPedido(recheioPedido);
        RecheioPedidoResponseDTO response = RecheioPedidoResponseDTO.toResponse(projection);
        return ResponseEntity.status(201).body(
                response
        );
    }

    @PutMapping("/recheio-pedido/{id}")
    public ResponseEntity<RecheioPedidoResponseDTO> atualizarRecheioPedido(
            @PathVariable Integer id,
            @RequestBody RecheioPedidoRequestDTO request
    ) {
        RecheioPedido recheioPedido = RecheioPedidoRequestDTO.toRecheioPedido(request);
        RecheioPedidoResponseDTO response = RecheioPedidoResponseDTO.toResponse(boloService.atualizarRecheioPedido(recheioPedido, id));
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/recheio-pedido/{id}")
    public ResponseEntity<RecheioPedidoResponseDTO> buscarRecheioPedidoPorId(
            @PathVariable Integer id
    ) {
        RecheioPedidoProjection projection =  boloService.buscarRecheioPedidoPorId(id);
        return ResponseEntity.status(200).body(RecheioPedidoResponseDTO.toResponse(projection));
    }

    @GetMapping("/recheio-pedido")
    public ResponseEntity<List<RecheioPedidoResponseDTO>> listarRecheiosPedido() {
        List<RecheioPedidoResponseDTO> response = RecheioPedidoResponseDTO.toResponse(
                boloService.listarRecheiosPedido()
        );

        if (response.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(response);
    }

    @DeleteMapping("/recheio-pedido/{id}")
    public ResponseEntity<Void> deletarRecheioPedido(
            @PathVariable Integer id
    ) {
        boloService.deletarRecheioPedido(id);
        return ResponseEntity.status(204).build();
    }


}
