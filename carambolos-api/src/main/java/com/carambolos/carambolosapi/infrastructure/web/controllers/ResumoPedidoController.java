package com.carambolos.carambolosapi.infrastructure.web.controllers;

import com.carambolos.carambolosapi.infrastructure.web.request.ResumoPedidoRequestDTO;
import com.carambolos.carambolosapi.infrastructure.web.response.DetalhePedidoBoloDTO;
import com.carambolos.carambolosapi.infrastructure.web.response.DetalhePedidoFornadaDTO;
import com.carambolos.carambolosapi.infrastructure.web.response.ResumoPedidoMensagemResponseDTO;
import com.carambolos.carambolosapi.infrastructure.web.response.ResumoPedidoResponseDTO;
import com.carambolos.carambolosapi.domain.entity.ResumoPedido;
import com.carambolos.carambolosapi.domain.enums.StatusEnum;
import com.carambolos.carambolosapi.application.usecases.ResumoPedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.carambolos.carambolosapi.infrastructure.web.request.MensagensResumoRequestDTO;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/resumo-pedido")
@Tag(name = "Resumo Pedido Controller", description = "Gerencia resumos de pedidos (bolos e fornadas)")
@SecurityRequirement(name = "Bearer")
public class ResumoPedidoController {

    @Autowired
    private ResumoPedidoService resumoPedidoService;

    @Operation(summary = "Listar resumos de pedidos", description = "Retorna todos os resumos de pedidos ativos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de resumos retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResumoPedidoResponseDTO.class))),
            @ApiResponse(responseCode = "204", description = "Nenhum resumo encontrado",
                    content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping
    public ResponseEntity<Page<ResumoPedidoResponseDTO>> listarResumosPedidos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "99999") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ResumoPedido> resumosPedidos = resumoPedidoService.listarResumosPedidos(pageable);
        if (resumosPedidos.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(200).body(ResumoPedidoResponseDTO.toResumoPedidoResponse(resumosPedidos));
    }

    @Operation(summary = "Buscar resumo de pedido por ID", description = "Retorna um resumo de pedido específico com base no ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resumo de pedido encontrado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResumoPedidoResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Resumo de pedido não encontrado",
                    content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ResumoPedidoResponseDTO> buscarResumoPedidoPorId(@PathVariable Integer id) {
        ResumoPedido resumoPedido = resumoPedidoService.buscarResumoPedidoPorId(id);
        return ResponseEntity.status(200).body(ResumoPedidoResponseDTO.toResumoPedidoResponse(resumoPedido));
    }

    @Operation(summary = "Buscar resumos por data de pedido", description = "Retorna resumos de pedidos para uma data de pedido específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de resumos retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResumoPedidoResponseDTO.class))),
            @ApiResponse(responseCode = "204", description = "Nenhum resumo encontrado para a data especificada",
                    content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/data-pedido/{dataPedido}")
    public ResponseEntity<List<ResumoPedidoResponseDTO>> buscarResumosPedidosPorDataPedido(
            @PathVariable LocalDate dataPedido
    ) {
        List<ResumoPedido> resumosPedidos = resumoPedidoService.buscarResumosPedidosPorDataPedido(dataPedido);
        if (resumosPedidos.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(200).body(ResumoPedidoResponseDTO.toResumoPedidoResponse(resumosPedidos));
    }

    @Operation(summary = "Buscar resumos por status", description = "Retorna resumos de pedidos com um status específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de resumos retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResumoPedidoResponseDTO.class))),
            @ApiResponse(responseCode = "204", description = "Nenhum resumo encontrado para o status especificado",
                    content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ResumoPedidoResponseDTO>> buscarResumosPorStatus(
            @PathVariable StatusEnum status
    ) {
        List<ResumoPedido> resumosPedidos = resumoPedidoService.buscarResumosPedidosPorStatus(status);
        if (resumosPedidos.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(200).body(ResumoPedidoResponseDTO.toResumoPedidoResponse(resumosPedidos));
    }

    @Operation(summary = "Cadastrar resumo de pedido", description = "Cadastra um novo resumo de pedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Resumo de pedido cadastrado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResumoPedidoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para cadastro",
                    content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping
    public ResponseEntity<ResumoPedidoMensagemResponseDTO> cadastrarResumoPedido(
            @Valid @RequestBody ResumoPedidoRequestDTO request
    ) {
        ResumoPedido resumoPedido = ResumoPedidoRequestDTO.toResumoPedido(request);
        ResumoPedido resumoSalvo = resumoPedidoService.cadastrarResumoPedido(resumoPedido);
        return ResponseEntity.status(201).body(ResumoPedidoMensagemResponseDTO.toResumoPedidoMensagemResponse(resumoSalvo));
    }

    @Operation(summary = "Atualizar resumo de pedido", description = "Atualiza um resumo de pedido existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resumo de pedido atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResumoPedidoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para atualização",
                    content = @Content()),
            @ApiResponse(responseCode = "404", description = "Resumo de pedido não encontrado",
                    content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ResumoPedidoResponseDTO> atualizarResumoPedido(
            @PathVariable Integer id,
            @Valid @RequestBody ResumoPedidoRequestDTO request
    ) {
        ResumoPedido resumoPedido = ResumoPedidoRequestDTO.toResumoPedido(request);
        ResumoPedido resumoAtualizado = resumoPedidoService.atualizarResumoPedido(id, resumoPedido);
        return ResponseEntity.status(200).body(ResumoPedidoResponseDTO.toResumoPedidoResponse(resumoAtualizado));
    }

    @Operation(summary = "Deletar resumo de pedido", description = "Remove um resumo de pedido pelo ID (desativa)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Resumo de pedido removido com sucesso",
                    content = @Content()),
            @ApiResponse(responseCode = "404", description = "Resumo de pedido não encontrado",
                    content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarResumoPedido(@PathVariable Integer id) {
        resumoPedidoService.deletarResumoPedido(id);
        return ResponseEntity.status(204).build();
    }

    @Operation(summary = "Marcar resumo de pedido como pago", description = "Atualiza o status do resumo de pedido para 'PAGO'")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resumo de pedido atualizado para 'PAGO' com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResumoPedidoResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Resumo de pedido não encontrado",
                    content = @Content()),
            @ApiResponse(responseCode = "422", description = "Resumo de pedido não pode ser pago",
                    content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PatchMapping("/{id}/pago")
    public ResponseEntity<ResumoPedidoResponseDTO> marcarPedidoComoPago(
            @PathVariable Integer id
    ) {
        ResumoPedido resumoPedido = resumoPedidoService.alterarStatus(id, StatusEnum.PAGO);
        return ResponseEntity.status(200).body(ResumoPedidoResponseDTO.toResumoPedidoResponse(resumoPedido));
    }

    @Operation(summary = "Marcar resumo de pedido como concluído", description = "Atualiza o status do resumo de pedido para 'CONCLUIDO'")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resumo de pedido atualizado para 'CONCLUIDO' com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResumoPedidoResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Resumo de pedido não encontrado",
                    content = @Content()),
            @ApiResponse(responseCode = "422", description = "Resumo de pedido não pode ser concluído",
                    content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PatchMapping("/{id}/concluido")
    public ResponseEntity<ResumoPedidoResponseDTO> marcarPedidoComoConcluido(
            @PathVariable Integer id
    ) {
        ResumoPedido resumoPedido = resumoPedidoService.alterarStatus(id, StatusEnum.CONCLUIDO);
        return ResponseEntity.status(200).body(ResumoPedidoResponseDTO.toResumoPedidoResponse(resumoPedido));
    }

    @Operation(summary = "Marcar resumo de pedido como cancelado", description = "Atualiza o status do resumo de pedido para 'PENDENTE'")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resumo de pedido atualizado para 'PENDENTE' com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResumoPedidoResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Resumo de pedido não encontrado",
                    content = @Content()),
            @ApiResponse(responseCode = "422", description = "Resumo de pedido não pode ser pendente",
                    content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PatchMapping("/{id}/pendente")
    public ResponseEntity<ResumoPedidoResponseDTO> marcarPedidoComoPendente(
            @PathVariable Integer id
    ) {
        ResumoPedido resumoPedido = resumoPedidoService.alterarStatus(id, StatusEnum.PENDENTE);
        return ResponseEntity.status(200).body(ResumoPedidoResponseDTO.toResumoPedidoResponse(resumoPedido));
    }

    @Operation(summary = "Marcar resumo de pedido como cancelado", description = "Atualiza o status do resumo de pedido para 'CANCELADO'")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resumo de pedido atualizado para 'CANCELADO' com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResumoPedidoResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Resumo de pedido não encontrado",
                    content = @Content()),
            @ApiResponse(responseCode = "422", description = "Resumo de pedido não pode ser cancelado",
                    content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PatchMapping("/{id}/cancelado")
    public ResponseEntity<ResumoPedidoResponseDTO> marcarPedidoComoCancelado(
            @PathVariable Integer id
    ) {
        ResumoPedido resumoPedido = resumoPedidoService.alterarStatus(id, StatusEnum.CANCELADO);
        return ResponseEntity.status(200).body(ResumoPedidoResponseDTO.toResumoPedidoResponse(resumoPedido));
    }

    @Operation(summary = "Buscar resumos de pedidos de bolo", description = "Retorna todos os resumos de pedidos que possuem pedido de bolo associado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de resumos retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResumoPedidoResponseDTO.class))),
            @ApiResponse(responseCode = "204", description = "Nenhum resumo de pedido de bolo encontrado",
                    content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/pedido-bolo")
    public ResponseEntity<List<ResumoPedidoResponseDTO>> listarResumosPedidosBolo() {
        List<ResumoPedido> resumosPedidos = resumoPedidoService.listarResumosPedidosBolo();
        if (resumosPedidos.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(200).body(ResumoPedidoResponseDTO.toResumoPedidoResponse(resumosPedidos));
    }

    @Operation(summary = "Buscar resumos de pedidos de fornada", description = "Retorna todos os resumos de pedidos que possuem pedido de fornada associado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de resumos retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResumoPedidoResponseDTO.class))),
            @ApiResponse(responseCode = "204", description = "Nenhum resumo de pedido de fornada encontrado",
                    content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/pedido-fornada")
    public ResponseEntity<List<ResumoPedidoResponseDTO>> listarResumosPedidosFornada() {
        List<ResumoPedido> resumosPedidos = resumoPedidoService.listarResumosPedidosFornada();
        if (resumosPedidos.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(200).body(ResumoPedidoResponseDTO.toResumoPedidoResponse(resumosPedidos));
    }

    @Operation(summary = "Detalhe de um pedido fornada específico", description = "Busca detalhe de um pedido fornada específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalhe do pedido retornado com sucesso", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/pedido-bolo/detalhe/{id}")
    public ResponseEntity<DetalhePedidoBoloDTO> obterDetalhePedidoBolo(@PathVariable Integer id) {
        DetalhePedidoBoloDTO detalhe = resumoPedidoService.obterDetalhePedidoBolo(id);
        return ResponseEntity.ok(detalhe);
    }

    @Operation(summary = "Detalhe de um pedido bolo específico", description = "Busca detalhe de um pedido de bolo específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalhe do pedido retornado com sucesso", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/pedido-fornada/detalhe/{id}")
    public ResponseEntity<DetalhePedidoFornadaDTO> obterDetalhePedidoFornada(@PathVariable Integer id) {
        DetalhePedidoFornadaDTO detalhe = resumoPedidoService.obterDetalhePedidoFornada(id);
        return ResponseEntity.ok(detalhe);
    }

    @Operation(summary = "Gerar mensagem consolidada de múltiplos resumos", description = "Concatena as mensagens de WhatsApp de vários resumos de pedido")
    @PostMapping("/mensagens")
    public ResponseEntity<String> gerarMensagensConsolidadas(@RequestBody MensagensResumoRequestDTO request) {
        String mensagem = resumoPedidoService.gerarMensagensConsolidadas(request.idsResumo());
        return ResponseEntity.ok(mensagem);
    }
}
