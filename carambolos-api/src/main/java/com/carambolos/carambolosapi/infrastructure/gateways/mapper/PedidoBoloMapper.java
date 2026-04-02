package com.carambolos.carambolosapi.infrastructure.gateways.mapper;

import com.carambolos.carambolosapi.application.gateways.BoloGateway;
import com.carambolos.carambolosapi.application.gateways.EnderecoGateway;
import com.carambolos.carambolosapi.application.gateways.UsuarioGateway;
import com.carambolos.carambolosapi.domain.entity.PedidoBolo;
import com.carambolos.carambolosapi.domain.entity.Bolo;
import com.carambolos.carambolosapi.domain.entity.Endereco;
import com.carambolos.carambolosapi.domain.entity.ResumoPedido;
import com.carambolos.carambolosapi.domain.entity.Usuario;
import com.carambolos.carambolosapi.domain.enums.StatusEnum;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.PedidoBoloEntity;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.ResumoPedidoRepository;
import com.carambolos.carambolosapi.infrastructure.web.request.PedidoBoloRequestDTO;
import com.carambolos.carambolosapi.infrastructure.web.response.PedidoBoloCompletoResponseDTO;
import com.carambolos.carambolosapi.infrastructure.web.response.PedidoBoloResponseDTO;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PedidoBoloMapper {
    private final BoloGateway boloGateway;
    private final EnderecoGateway enderecoGateway;
    private final UsuarioGateway usuarioGateway;
    private final BoloMapper boloMapper;
    private final ResumoPedidoRepository resumoPedidoRepository;

    public PedidoBoloMapper(
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

    public List<PedidoBolo> toDomain(List<com.carambolos.carambolosapi.infrastructure.persistence.entity.PedidoBoloEntity> entities) {
        return entities.stream()
                .map(this::toDomain)
                .toList();
    }

    public PedidoBolo toDomain(com.carambolos.carambolosapi.infrastructure.persistence.entity.PedidoBoloEntity entity) {
        if (entity == null) {
            return null;
        }

        return new PedidoBolo(
                entity.getId(),
                entity.getEnderecoId(),
                entity.getBoloId(),
                entity.getUsuarioId(),
                entity.getObservacao(),
                entity.getDataPrevisaoEntrega(),
                entity.getDataUltimaAtualizacao(),
                entity.getTipoEntrega(),
                entity.getNomeCliente(),
                entity.getTelefoneCliente(),
                entity.getHorarioRetirada(),
                entity.getAtivo()
        );
    }

    public PedidoBoloEntity toEntity(PedidoBolo pedidoBolo) {
        if (pedidoBolo == null) {
            return null;
        }

        PedidoBoloEntity entity = new PedidoBoloEntity();
        entity.setId(pedidoBolo.getId());
        entity.setEnderecoId(pedidoBolo.getEnderecoId());
        entity.setBoloId(pedidoBolo.getBoloId());
        entity.setUsuarioId(pedidoBolo.getUsuarioId());
        entity.setObservacao(pedidoBolo.getObservacao());
        entity.setDataPrevisaoEntrega(pedidoBolo.getDataPrevisaoEntrega());
        entity.setDataUltimaAtualizacao(pedidoBolo.getDataUltimaAtualizacao());
        entity.setTipoEntrega(pedidoBolo.getTipoEntrega());
        entity.setNomeCliente(pedidoBolo.getNomeCliente());
        entity.setTelefoneCliente(pedidoBolo.getTelefoneCliente());
        entity.setHorarioRetirada(pedidoBolo.getHorarioRetirada());
        entity.setAtivo(pedidoBolo.getAtivo());
        return entity;
    }

    public PedidoBoloResponseDTO toPedidoBoloResponse(PedidoBolo pedido) {
        return new PedidoBoloResponseDTO(
                pedido.getId(),
                pedido.getEnderecoId(),
                pedido.getBoloId(),
                pedido.getUsuarioId(),
                pedido.getObservacao(),
                pedido.getDataPrevisaoEntrega(),
                pedido.getDataUltimaAtualizacao(),
                pedido.getTipoEntrega(),
                pedido.getNomeCliente(),
                pedido.getTelefoneCliente()
        );
    }

    public List<PedidoBoloResponseDTO> toPedidoBoloResponse(List<PedidoBolo> pedidos) {
        return pedidos.stream().map(this::toPedidoBoloResponse).toList();
    }

    public PedidoBoloCompletoResponseDTO toPedidoBoloCompletoResponse(PedidoBolo pedido) {
        StatusEnum status = resumoPedidoRepository
                .findTop1ByPedidoBoloIdAndIsAtivoTrueOrderByDataPedidoDesc(pedido.getId())
                .map(ResumoPedido::getStatus)
                .orElse(null);

        return toPedidoBoloCompletoResponse(pedido, status);
    }

    private PedidoBoloCompletoResponseDTO toPedidoBoloCompletoResponse(PedidoBolo pedido, StatusEnum status) {
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

    public List<PedidoBoloCompletoResponseDTO> toPedidoBoloCompletoResponse(List<PedidoBolo> pedidos) {
        Map<Integer, StatusEnum> statusByPedidoBoloId = carregarStatusPorPedidoBoloId(pedidos);
        return pedidos.stream()
                .map(pedido -> toPedidoBoloCompletoResponse(pedido, statusByPedidoBoloId.get(pedido.getId())))
                .toList();
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

    public static PedidoBolo toPedidoBolo(PedidoBoloRequestDTO request) {
        if (request == null) {
            return null;
        }
        PedidoBolo pedidoBoloEntity = new PedidoBolo();
        pedidoBoloEntity.setEnderecoId(request.enderecoId());
        pedidoBoloEntity.setBoloId(request.boloId());
        pedidoBoloEntity.setUsuarioId(request.usuarioId());
        pedidoBoloEntity.setObservacao(request.observacao());
        pedidoBoloEntity.setDataPrevisaoEntrega(request.dataPrevisaoEntrega());
        pedidoBoloEntity.setDataUltimaAtualizacao(request.dataUltimaAtualizacao());
        pedidoBoloEntity.setTipoEntrega(request.tipoEntrega());
        pedidoBoloEntity.setNomeCliente(request.nomeCliente());
        pedidoBoloEntity.setTelefoneCliente(request.telefoneCliente());
        pedidoBoloEntity.setHorarioRetirada(request.horarioRetirada());
        return pedidoBoloEntity;
    }
}
