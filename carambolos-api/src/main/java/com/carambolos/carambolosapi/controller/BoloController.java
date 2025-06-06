package com.carambolos.carambolosapi.controller;

import com.carambolos.carambolosapi.controller.request.*;
import com.carambolos.carambolosapi.controller.response.*;
import com.carambolos.carambolosapi.model.*;
import com.carambolos.carambolosapi.model.projection.RecheioExclusivoProjection;
import com.carambolos.carambolosapi.model.projection.RecheioPedidoProjection;
import com.carambolos.carambolosapi.service.BoloService;
import com.carambolos.carambolosapi.service.PedidoBoloService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.carambolos.carambolosapi.controller.request.RecheioUnitarioRequestDTO.toRecheioUnitario;

@RestController
@RequestMapping("/bolos")
@SecurityRequirement(name = "Bearer")
@Tag(name = "Bolo Controller", description = "Gerencia bolos, recheios, coberturas e massas")
@SecurityRequirement(name = "Bearer")
public class BoloController {

    @Autowired
    private BoloService boloService;

    @Autowired
    private PedidoBoloService pedidoBoloService;

    @Operation(summary = "Listar bolos", description = "Retorna todos os bolos cadastrados no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de bolos retornada com sucesso.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BoloResponseDTO.class)
            )),
            @ApiResponse(responseCode = "204", description = "Nenhum bolo encontrado.", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    @GetMapping
    public ResponseEntity<List<Bolo>> listarBolos(
            @RequestParam(defaultValue = "") List<String> categorias
    ) {
        List<Bolo> bolos = boloService.listarBolos(categorias);
        if (bolos.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(200).body(bolos);
    }

    @Operation(summary = "Buscar bolo por ID", description = "Busca um bolo pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bolo encontrado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BoloResponseDTO.class)
            )),
            @ApiResponse(responseCode = "404", description = "Bolo não encontrado", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BoloResponseDTO> buscarPorId(@PathVariable Integer id) {
        Bolo bolo = boloService.buscarBoloPorId(id);
        BoloResponseDTO response = BoloResponseDTO.toBoloResponse(bolo);
        return ResponseEntity.status(200).body(response);
    }

    @Operation(summary = "Cadastrar bolo", description = "Cadastra um novo bolo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Bolo criado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BoloResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping
    public ResponseEntity<BoloResponseDTO> cadastrarBolo(@Valid @RequestBody BoloRequestDTO request) {
        Bolo bolo = BoloRequestDTO.toBolo(request);
        Bolo boloSalvo = boloService.cadastrarBolo(bolo);
        BoloResponseDTO response = BoloResponseDTO.toBoloResponse(boloSalvo);
        return ResponseEntity.status(201).body(response);
    }

    @Operation(summary = "Atualizar bolo", description = "Atualiza os dados de um bolo existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bolo atualizado com sucesso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BoloResponseDTO.class)
            )),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Bolo não encontrado", content = @Content()),
            @ApiResponse(responseCode = "409", description = "Bolo já existe"),
    })
    @PutMapping("/{id}")
    public ResponseEntity<BoloResponseDTO> atualizarBolo(
            @PathVariable Integer id,
            @Valid @RequestBody BoloRequestDTO request
    ) {
        Bolo bolo = BoloRequestDTO.toBolo(request);
        Bolo boloAtualizado = boloService.atualizarBolo(bolo, id);
        BoloResponseDTO response = BoloResponseDTO.toBoloResponse(boloAtualizado);
        return ResponseEntity.status(200).body(response);
    }

    @Operation(summary = "Deletar bolo", description = "Remove um bolo pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Bolo removido com sucesso", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Bolo não encontrado", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarBolo(@PathVariable Integer id) {
        boloService.deletarBolo(id);
        return ResponseEntity.status(204).build();
    }


    @Operation(summary = "Cadastrar recheio unitário", description = "Cadastra um novo recheio unitário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Recheio criado com sucesso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RecheioUnitarioResponseDTO.class)
            )),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/recheio-unitario")
    public ResponseEntity<RecheioUnitarioResponseDTO> cadastrarRecheioUnitario(
            @Valid @RequestBody RecheioUnitarioRequestDTO request
    ) {
        RecheioUnitario recheioUnitario = toRecheioUnitario(request);
        RecheioUnitario recheioSalvo = boloService.cadastrarRecheioUnitario(recheioUnitario);
        return ResponseEntity.status(201).body(
                RecheioUnitarioResponseDTO.toRecheioUnitarioResponse(recheioSalvo)
        );
    }

    @Operation(summary = "Listar recheios unitários", description = "Lista todos os recheios unitários cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RecheioUnitarioResponseDTO.class)
            )),
            @ApiResponse(responseCode = "204", description = "Nenhum recheio unitário encontrado", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/recheio-unitario")
    public ResponseEntity<List<RecheioUnitarioResponseDTO>> listarRecheioUnitario() {
        List<RecheioUnitario> recheiosUnitarios = boloService.listarRecheiosUnitarios();
        return ResponseEntity.status(200).body(
                RecheioUnitarioResponseDTO.toRecheioUnitarioResponse(recheiosUnitarios)
        );
    }

    @Operation(summary = "Buscar recheio unitário por ID", description = "Busca um recheio unitário pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recheio encontrado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RecheioUnitarioResponseDTO.class)
            )),
            @ApiResponse(responseCode = "404", description = "Recheio não encontrado", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/recheio-unitario/{id}")
    public ResponseEntity<RecheioUnitarioResponseDTO> buscarRecheioUnitarioPorId(
            @PathVariable Integer id
    ) {
        RecheioUnitario recheioUnitario = boloService.buscarPorId(id);
        return ResponseEntity.status(200).body(
                RecheioUnitarioResponseDTO.toRecheioUnitarioResponse(recheioUnitario)
        );
    }

    @Operation(summary = "Atualizar recheio unitário", description = "Atualiza os dados de um recheio unitário existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recheio atualizado com sucesso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RecheioUnitarioResponseDTO.class)
            )),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Recheio não encontrado", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
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

    @Operation(summary = "Deletar recheio unitário", description = "Remove um recheio unitário pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Recheio removido com sucesso", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Recheio não encontrado", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @DeleteMapping("/recheio-unitario/{id}")
    public ResponseEntity<Void> deletarRecheioUnitario(
            @PathVariable Integer id
    ) {
        boloService.deletarRecheioUnitario(id);
        return ResponseEntity.status(204).build();
    }

    @Operation(summary = "Cadastrar recheio exclusivo", description = "Cadastra um novo recheio exclusivo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recheio exclusivo cadastrado com sucesso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RecheioExclusivoResponseDTO.class)
            )),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para cadastro", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
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

    @Operation(summary = "Buscar recheio exclusivo por ID", description = "Busca um recheio exclusivo pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recheio exclusivo encontrado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RecheioExclusivoResponseDTO.class)
            )),
            @ApiResponse(responseCode = "404", description = "Recheio exclusivo não encontrado", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/recheio-exclusivo/{id}")
    public ResponseEntity<RecheioExclusivoResponseDTO> buscarRecheioExclusivoPorId(
            @PathVariable Integer id
    ) {
        RecheioExclusivoProjection projection = boloService.buscarRecheioExclusivoPorId(id);
        return ResponseEntity.status(200).body(
                RecheioExclusivoResponseDTO.toRecheioExclusivoResponse(projection)
        );
    }

    @Operation(summary = "Listar recheios exclusivos", description = "Retorna uma lista de todos os recheios exclusivos cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RecheioExclusivoResponseDTO.class)
            )),
            @ApiResponse(responseCode = "204", description = "Nenhum recheio exclusivo encontrado", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/recheio-exclusivo")
    public ResponseEntity<List<RecheioExclusivoResponseDTO>> listarRecheiosExclusivos() {
        List<RecheioExclusivoProjection> recheiosEncontrados = boloService.listarRecheiosExclusivos();
        return ResponseEntity.status(200).body(
                RecheioExclusivoResponseDTO.toRecheioExclusivoResponse(recheiosEncontrados)
        );
    }

    @Operation(summary = "Atualizar recheio exclusivo", description = "Atualiza um recheio exclusivo com base no ID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recheio exclusivo atualizado com sucesso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RecheioExclusivoResponseDTO.class)
            )),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para atualização", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Recheio exclusivo não encontrado", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
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

    @Operation(summary = "Excluir recheio exclusivo", description = "Exclui um recheio exclusivo pelo ID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Recheio exclusivo removido com sucesso", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Recheio exclusivo não encontrado", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @DeleteMapping("/recheio-exclusivo/{id}")
    public ResponseEntity<Void> excluirRecheioExclusivo(
            @PathVariable Integer id
    ) {
        boloService.excluirRecheioExclusivo(id);
        return ResponseEntity.status(204).build();
    }

    @Operation(summary = "Cadastrar recheio do pedido", description = "Cadastra um novo recheio para um pedido de bolo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Recheio do pedido cadastrado com sucesso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RecheioPedidoResponseDTO.class)
            )),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para cadastro", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/recheio-pedido")
    public ResponseEntity<RecheioPedidoResponseDTO> cadastrarRecheioPedido(
            @RequestBody RecheioPedidoRequestDTO request
    ) {
        RecheioPedido recheioPedido = RecheioPedidoRequestDTO.toRecheioPedido(request);
        RecheioPedidoProjection projection = boloService.cadastrarRecheioPedido(recheioPedido);
        RecheioPedidoResponseDTO response = RecheioPedidoResponseDTO.toResponse(projection);
        return ResponseEntity.status(201).body(response);
    }

    @Operation(summary = "Atualizar recheio do pedido", description = "Atualiza um recheio de pedido com base no ID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recheio do pedido atualizado com sucesso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RecheioPedidoResponseDTO.class)
            )),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para atualização", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Recheio do pedido não encontrado", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PutMapping("/recheio-pedido/{id}")
    public ResponseEntity<RecheioPedidoResponseDTO> atualizarRecheioPedido(
            @PathVariable Integer id,
            @RequestBody RecheioPedidoRequestDTO request
    ) {
        RecheioPedido recheioPedido = RecheioPedidoRequestDTO.toRecheioPedido(request);
        RecheioPedidoResponseDTO response = RecheioPedidoResponseDTO.toResponse(boloService.atualizarRecheioPedido(recheioPedido, id));
        return ResponseEntity.status(200).body(response);
    }

    @Operation(summary = "Buscar recheio do pedido por ID", description = "Busca um recheio de pedido pelo ID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recheio do pedido encontrado com sucesso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RecheioPedidoResponseDTO.class)
            )),
            @ApiResponse(responseCode = "404", description = "Recheio do pedido não encontrado", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/recheio-pedido/{id}")
    public ResponseEntity<RecheioPedidoResponseDTO> buscarRecheioPedidoPorId(
            @PathVariable Integer id
    ) {
        RecheioPedidoProjection projection = boloService.buscarRecheioPedidoPorId(id);
        return ResponseEntity.status(200).body(RecheioPedidoResponseDTO.toResponse(projection));
    }

    @Operation(summary = "Listar todos os recheios de pedido", description = "Lista todos os recheios de pedido cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RecheioPedidoResponseDTO.class)
            )),
            @ApiResponse(responseCode = "204", description = "Nenhum recheio de pedido encontrado", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
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

    @Operation(summary = "Deletar recheio do pedido", description = "Remove um recheio de pedido com base no ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Recheio do pedido deletado com sucesso", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Recheio do pedido não encontrado", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @DeleteMapping("/recheio-pedido/{id}")
    public ResponseEntity<Void> deletarRecheioPedido(
            @PathVariable Integer id
    ) {
        boloService.deletarRecheioPedido(id);
        return ResponseEntity.status(204).build();
    }

    @Operation(summary = "Cadastrar cobertura", description = "Cadastra uma nova cobertura para bolo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cobertura cadastrada com sucesso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CoberturaResponseDTO.class)
            )),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para cadastro", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/cobertura")
    public ResponseEntity<CoberturaResponseDTO> cadastrarCobertura(
            @RequestBody CoberturaRequestDTO request
    ) {
        Cobertura cobertura = CoberturaRequestDTO.toCobertura(request);
        boloService.cadastrarCobertura(cobertura);
        return ResponseEntity.status(201).body(CoberturaResponseDTO.toResponse(cobertura));
    }

    @Operation(summary = "Atualizar cobertura", description = "Atualiza uma cobertura existente pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cobertura atualizada com sucesso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CoberturaResponseDTO.class)
            )),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para atualização", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Cobertura não encontrada", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
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

    @Operation(summary = "Listar coberturas", description = "Retorna uma lista com todas as coberturas cadastradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de coberturas retornada com sucesso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CoberturaResponseDTO.class)
            )),
            @ApiResponse(responseCode = "204", description = "Nenhuma cobertura encontrada", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/cobertura")
    public ResponseEntity<List<CoberturaResponseDTO>> listarCoberturas() {
        List<Cobertura> coberturas = boloService.listarCoberturas();
        if (coberturas.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(200).body(CoberturaResponseDTO.toResponse(coberturas));
    }

    @Operation(summary = "Buscar cobertura por ID", description = "Retorna uma cobertura específica com base no ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cobertura encontrada com sucesso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CoberturaResponseDTO.class)
            )),
            @ApiResponse(responseCode = "404", description = "Cobertura não encontrada", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/cobertura/{id}")
    public ResponseEntity<CoberturaResponseDTO> buscarCoberturaPorId(
            @PathVariable Integer id
    ) {
        Cobertura cobertura = boloService.buscarCoberturaPorId(id);
        return ResponseEntity.status(200).body(CoberturaResponseDTO.toResponse(cobertura));
    }

    @Operation(summary = "Deletar cobertura", description = "Remove uma cobertura pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cobertura deletada com sucesso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CoberturaResponseDTO.class)
            )),
            @ApiResponse(responseCode = "404", description = "Cobertura não encontrada", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @DeleteMapping("/cobertura/{id}")
    public ResponseEntity<Void> deletarCobertura(
            @PathVariable Integer id
    ) {
        boloService.deletarCobertura(id);
        return ResponseEntity.status(204).build();
    }

    @Operation(summary = "Cadastrar massa", description = "Cadastra uma nova massa para bolo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Massa cadastrada com sucesso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MassaResponseDTO.class)
            )),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para cadastro", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
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

    @Operation(summary = "Atualizar massa", description = "Atualiza uma massa existente pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Massa atualizada com sucesso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MassaResponseDTO.class)
            )),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para atualização", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Massa não encontrada", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
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

    @Operation(summary = "Listar massas", description = "Retorna uma lista com todas as massas cadastradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de massas retornada com sucesso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MassaResponseDTO.class)
            )),
            @ApiResponse(responseCode = "204", description = "Nenhuma massa encontrada", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
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

    @Operation(summary = "Buscar massa por ID", description = "Retorna uma massa específica com base no ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Massa encontrada com sucesso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MassaResponseDTO.class)
            )),
            @ApiResponse(responseCode = "404", description = "Massa não encontrada", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/massa/{id}")
    public ResponseEntity<MassaResponseDTO> buscarMassaPorId(
            @PathVariable Integer id
    ) {
        Massa massa = boloService.buscarMassaPorId(id);
        return ResponseEntity.status(200).body(
                MassaResponseDTO.toMassaResponse(massa)
        );
    }

    @Operation(summary = "Deletar massa", description = "Remove uma massa pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Massa deletada com sucesso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MassaResponseDTO.class)
            )),
            @ApiResponse(responseCode = "404", description = "Massa não encontrada", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @DeleteMapping("/massa/{id}")
    public ResponseEntity<Void> deletarMassa(
            @PathVariable Integer id
    ) {
        boloService.deletarMassa(id);
        return ResponseEntity.status(204).build();
    }

    //    @Operation(summary = "Cadastrar decoração", description = "Cadastra uma nova decoração de bolo")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", description = "Decoração cadastrada com sucesso"),
//            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
//            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
//    })
//    @PostMapping("/decoracao")
//    public ResponseEntity<DecoracaoResponseDTO> cadastrarDecoracao(@Valid @RequestBody DecoracaoRequestDTO request) {
//        Decoracao decoracao = DecoracaoRequestDTO.toDecoracao(request);
//        Decoracao decoracaoSalva = boloService.cadastrarDecoracao(decoracao);
//        DecoracaoResponseDTO response = DecoracaoResponseDTO.toDecoracaoResponse(decoracaoSalva);
//        return ResponseEntity.status(201).body(response);
//    }
    @Operation(summary = "Listar pedidos", description = "Retorna uma lista com todos os pedidos ativos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pedidos retornada com sucesso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PedidoBoloResponseDTO.class)
            )),
            @ApiResponse(responseCode = "204", description = "Nenhum pedido encontrado", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/pedido")
    public ResponseEntity<List<PedidoBoloResponseDTO>> listarPedidos() {
        List<PedidoBolo> pedidos = pedidoBoloService.listarPedidos();
        if (pedidos.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(200).body(
                PedidoBoloResponseDTO.toPedidoBoloResponse(pedidos)
        );
    }

    @Operation(summary = "Buscar pedido por ID", description = "Retorna um pedido específico com base no ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado com sucesso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PedidoBoloResponseDTO.class)
            )),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/pedido/{id}")
    public ResponseEntity<PedidoBoloResponseDTO> buscarPedidoPorId(
            @PathVariable Integer id
    ) {
        PedidoBolo pedido = pedidoBoloService.buscarPedidoPorId(id);
        return ResponseEntity.status(200).body(
                PedidoBoloResponseDTO.toPedidoBoloResponse(pedido)
        );
    }

    @Operation(summary = "Cadastrar pedido", description = "Cadastra um novo pedido de bolo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para cadastro"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/pedido")
    public ResponseEntity<PedidoBoloResponseDTO> cadastrarPedido(
            @Valid @RequestBody PedidoBoloRequestDTO request
    ) {
        PedidoBolo pedido = PedidoBoloRequestDTO.toPedidoBolo(request);
        PedidoBolo pedidoSalvo = pedidoBoloService.cadastrarPedido(pedido);
        return ResponseEntity.status(201).body(
                PedidoBoloResponseDTO.toPedidoBoloResponse(pedidoSalvo)
        );
    }

    @Operation(summary = "Atualizar pedido", description = "Atualiza os dados de um pedido existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido atualizado com sucesso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PedidoBoloResponseDTO.class)
            )),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para atualização", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PutMapping("/pedido/{id}")
    public ResponseEntity<PedidoBoloResponseDTO> atualizarPedido(
            @PathVariable Integer id,
            @RequestBody PedidoBoloRequestDTO request
    ) {
        PedidoBolo pedido = PedidoBoloRequestDTO.toPedidoBolo(request);
        PedidoBolo pedidoAtualizado = pedidoBoloService.atualizarPedido(pedido, id);
        return ResponseEntity.status(200).body(
                PedidoBoloResponseDTO.toPedidoBoloResponse(pedidoAtualizado)
        );
    }

    @Operation(summary = "Deletar pedido", description = "Remove um pedido pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pedido removido com sucesso", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @DeleteMapping("/pedido/{id}")
    public ResponseEntity<Void> deletarPedido(
            @PathVariable Integer id
    ) {
        pedidoBoloService.deletarPedido(id);
        return ResponseEntity.status(204).build();
    }
}

