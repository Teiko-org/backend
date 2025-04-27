package com.carambolos.carambolosapi.controller;

import com.carambolos.carambolosapi.controller.request.*;
import com.carambolos.carambolosapi.controller.response.*;
import com.carambolos.carambolosapi.model.*;
import com.carambolos.carambolosapi.model.projection.RecheioExclusivoProjection;
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

//    @GetMapping
//    public ResponseEntity<List<Bolo>> listarBolos() {
//        return ResponseEntity.status(200).body(boloService.listarBolos());
//    }

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
        RecheioPedidoProjection projection = boloService.buscarRecheioPedidoPorId(id);
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

    @PostMapping("/cobertura")
    public ResponseEntity<CoberturaResponseDTO> cadastrarCobertura(
            @RequestBody CoberturaRequestDTO request
    ) {
        Cobertura cobertura = CoberturaRequestDTO.toCobertura(request);
        boloService.cadastrarCobertura(cobertura);
        return ResponseEntity.status(201).body(
                CoberturaResponseDTO.toResponse(cobertura)
        );
    }

    @PutMapping("/cobertura/{id}")
    public ResponseEntity<CoberturaResponseDTO> atualizarCobertura(
            @PathVariable Integer id,
            @RequestBody CoberturaRequestDTO request
    ) {
        Cobertura cobertura = CoberturaRequestDTO.toCobertura(request);
        CoberturaResponseDTO response = CoberturaResponseDTO.toResponse(
                boloService.atualizarCobertura(cobertura, id)
        );
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/cobertura")
    public ResponseEntity<List<CoberturaResponseDTO>> listarCoberturas() {
        List<Cobertura> coberturas = boloService.listarCoberturas();
        if (coberturas.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(
                CoberturaResponseDTO.toResponse(coberturas)
        );
    }

    @GetMapping("/cobertura/{id}")
    public ResponseEntity<CoberturaResponseDTO> buscarCoberturaPorId(
            @PathVariable Integer id
    ) {
        Cobertura cobertura = boloService.buscarCoberturaPorId(id);
        return ResponseEntity.status(200).body(
                CoberturaResponseDTO.toResponse(cobertura)
        );
    }

    @DeleteMapping("/cobertura/{id}")
    public ResponseEntity<Void> deletarCobertura(
            @PathVariable Integer id
    ) {
        boloService.deletarCobertura(id);
        return ResponseEntity.status(200).build();
    }

    @PostMapping("/massa")
    public ResponseEntity<MassaResponseDTO> cadastrarMassa(
            @Valid @RequestBody MassaRequestDTO request
    ) {
        Massa massa = MassaRequestDTO.toMassa(request);
        boloService.cadastrarMassa(massa);
        return ResponseEntity.status(201).body(
                MassaResponseDTO.toMassaResponse(massa)
        );
    }

    @PutMapping("/massa/{id}")
    public ResponseEntity<MassaResponseDTO> atualizarMassa(
            @PathVariable Integer id,
            @RequestBody MassaRequestDTO request
    ) {
        Massa massa = MassaRequestDTO.toMassa(request);
        Massa cadastrada = boloService.atualizarMassa(massa, id);
        return ResponseEntity.status(200).body(
          MassaResponseDTO.toMassaResponse(cadastrada)
        );
    }

    @GetMapping("/massa")
    public ResponseEntity<List<MassaResponseDTO>> listarMassas() {
        List<Massa> massas = boloService.listarMassas();

        if (massas.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(200).body(
                MassaResponseDTO.toMassaResponse(massas)
        );
    }

    @GetMapping("/massa/{id}")
    public ResponseEntity<MassaResponseDTO> buscarMassaPorId(
            @PathVariable Integer id
    ) {
        Massa massa = boloService.buscarMassaPorId(id);
        return ResponseEntity.status(200).body(
            MassaResponseDTO.toMassaResponse(massa)
        );
    }

    @DeleteMapping("/massa/{id}")
    public ResponseEntity<Void> deletarMassa(
            @PathVariable Integer id
    ) {
        boloService.deletarMassa(id);
        return ResponseEntity.status(200).build();
    }
}
