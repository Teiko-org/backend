package com.carambolos.carambolosapi.infrastructure.gateways.mapper;

import com.carambolos.carambolosapi.application.gateways.BoloGateway;
import com.carambolos.carambolosapi.application.gateways.EnderecoGateway;
import com.carambolos.carambolosapi.application.gateways.UsuarioGateway;
import com.carambolos.carambolosapi.domain.entity.Bolo;
import com.carambolos.carambolosapi.domain.entity.Endereco;
import com.carambolos.carambolosapi.domain.entity.PedidoBolo;
import com.carambolos.carambolosapi.domain.entity.ResumoPedido;
import com.carambolos.carambolosapi.domain.entity.Usuario;
import com.carambolos.carambolosapi.domain.enums.StatusEnum;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.ResumoPedidoRepository;
import com.carambolos.carambolosapi.infrastructure.web.response.PedidoBoloCompletoResponseDTO;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PedidoBoloCompletoMapper {
    private final BoloGateway boloGateway;
    private final EnderecoGateway enderecoGateway;
    private final UsuarioGateway usuarioGateway;
    private final BoloMapper boloMapper;
    private final ResumoPedidoRepository resumoPedidoRepository;

    public PedidoBoloCompletoMapper(
            BoloGateway boloGateway,
            EnderecoGateway enderecoGateway,
            UsuarioGateway usuarioGateway,
            BoloMapper boloMapper,
            ResumoPedidoRepository resumoPedidoRepository
    ) {
        this.boloGateway = boloGateway;
        this.enderecoGateway = enderecoGateway;
        this.usuarioGateway = usuarioGateway;
        this.boloMapper = boloMapper;
        this.resumoPedidoRepository = resumoPedidoRepository;
    }

    public List<PedidoBoloCompletoResponseDTO> toResponse(List<PedidoBolo> pedidos) {
        if (pedidos.isEmpty()) {
            return List.of();
        }

        Map<Integer, StatusEnum> statusByPedidoBoloId = carregarStatusPorPedidoBoloId(pedidos);
        return pedidos.stream()
                .map(pedido -> toResponse(pedido, statusByPedidoBoloId.get(pedido.getId())))
                .toList();
    }

    private PedidoBoloCompletoResponseDTO toResponse(PedidoBolo pedido, StatusEnum status) {
        Bolo bolo = null;
        if (pedido.getBoloId() != null && Boolean.TRUE.equals(boloGateway.existsByIdAndIsAtivoTrue(pedido.getBoloId()))) {
            bolo = boloGateway.findById(pedido.getBoloId());
        }

        Endereco endereco = null;
        if (pedido.getEnderecoId() != null) {
            endereco = enderecoGateway.buscarPorId(pedido.getEnderecoId());
        }

        Usuario usuario = null;
        if (pedido.getUsuarioId() != null) {
            usuario = usuarioGateway.buscarPorId(pedido.getUsuarioId());
        }

        return new PedidoBoloCompletoResponseDTO(
                pedido.getId(),
                bolo == null ? null : boloMapper.toBoloResponse(bolo),
                EnderecoMapper.toResponseDTO(endereco),
                usuario == null ? null : UsuarioMapper.toResponseDTO(usuario),
                status,
                pedido.getObservacao(),
                pedido.getDataPrevisaoEntrega(),
                pedido.getDataUltimaAtualizacao(),
                pedido.getTipoEntrega(),
                pedido.getNomeCliente(),
                pedido.getTelefoneCliente()
        );
    }

    private Map<Integer, StatusEnum> carregarStatusPorPedidoBoloId(List<PedidoBolo> pedidos) {
        List<Integer> pedidoIds = pedidos.stream().map(PedidoBolo::getId).toList();
        List<ResumoPedido> resumos = resumoPedidoRepository
                .findByPedidoBoloIdInAndIsAtivoTrueOrderByDataPedidoDesc(pedidoIds);

        Map<Integer, StatusEnum> statusByPedidoBoloId = new LinkedHashMap<>();
        for (ResumoPedido resumo : resumos) {
            statusByPedidoBoloId.putIfAbsent(resumo.getPedidoBoloId(), resumo.getStatus());
        }
        return statusByPedidoBoloId;
    }
}

