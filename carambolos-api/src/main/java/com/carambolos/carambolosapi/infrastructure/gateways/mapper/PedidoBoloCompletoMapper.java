package com.carambolos.carambolosapi.infrastructure.gateways.mapper;

import com.carambolos.carambolosapi.application.gateways.BoloGateway;
import com.carambolos.carambolosapi.application.gateways.CoberturaGateway;
import com.carambolos.carambolosapi.application.gateways.DecoracaoGateway;
import com.carambolos.carambolosapi.application.gateways.EnderecoGateway;
import com.carambolos.carambolosapi.application.gateways.MassaGateway;
import com.carambolos.carambolosapi.application.gateways.RecheioPedidoGateway;
import com.carambolos.carambolosapi.application.gateways.UsuarioGateway;
import com.carambolos.carambolosapi.application.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.domain.entity.Bolo;
import com.carambolos.carambolosapi.domain.entity.Cobertura;
import com.carambolos.carambolosapi.domain.entity.Decoracao;
import com.carambolos.carambolosapi.domain.entity.Endereco;
import com.carambolos.carambolosapi.domain.entity.Massa;
import com.carambolos.carambolosapi.domain.entity.PedidoBolo;
import com.carambolos.carambolosapi.domain.entity.RecheioPedido;
import com.carambolos.carambolosapi.domain.entity.ResumoPedido;
import com.carambolos.carambolosapi.domain.entity.Usuario;
import com.carambolos.carambolosapi.domain.enums.StatusEnum;
import com.carambolos.carambolosapi.infrastructure.persistence.projection.RecheioPedidoProjection;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.ResumoPedidoRepository;
import com.carambolos.carambolosapi.infrastructure.web.response.BoloCompletoResponseDTO;
import com.carambolos.carambolosapi.infrastructure.web.response.CoberturaResponseDTO;
import com.carambolos.carambolosapi.infrastructure.web.response.DecoracaoResponseDTO;
import com.carambolos.carambolosapi.infrastructure.web.response.MassaResponseDTO;
import com.carambolos.carambolosapi.infrastructure.web.response.PedidoBoloCompletoResponseDTO;
import com.carambolos.carambolosapi.infrastructure.web.response.RecheioPedidoResponseDTO;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PedidoBoloCompletoMapper {
    private final BoloGateway boloGateway;
    private final EnderecoGateway enderecoGateway;
    private final UsuarioGateway usuarioGateway;
    private final RecheioPedidoGateway recheioPedidoGateway;
    private final MassaGateway massaGateway;
    private final CoberturaGateway coberturaGateway;
    private final DecoracaoGateway decoracaoGateway;
    private final RecheioPedidoMapper recheioPedidoMapper;
    private final MassaMapper massaMapper;
    private final CoberturaMapper coberturaMapper;
    private final DecoracaoMapper decoracaoMapper;
    private final ResumoPedidoRepository resumoPedidoRepository;

    public PedidoBoloCompletoMapper(
            BoloGateway boloGateway,
            EnderecoGateway enderecoGateway,
            UsuarioGateway usuarioGateway,
            RecheioPedidoGateway recheioPedidoGateway,
            MassaGateway massaGateway,
            CoberturaGateway coberturaGateway,
            DecoracaoGateway decoracaoGateway,
            RecheioPedidoMapper recheioPedidoMapper,
            MassaMapper massaMapper,
            CoberturaMapper coberturaMapper,
            DecoracaoMapper decoracaoMapper,
            ResumoPedidoRepository resumoPedidoRepository
    ) {
        this.boloGateway = boloGateway;
        this.enderecoGateway = enderecoGateway;
        this.usuarioGateway = usuarioGateway;
        this.recheioPedidoGateway = recheioPedidoGateway;
        this.massaGateway = massaGateway;
        this.coberturaGateway = coberturaGateway;
        this.decoracaoGateway = decoracaoGateway;
        this.recheioPedidoMapper = recheioPedidoMapper;
        this.massaMapper = massaMapper;
        this.coberturaMapper = coberturaMapper;
        this.decoracaoMapper = decoracaoMapper;
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
                toBoloCompletoResponse(bolo),
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

    private BoloCompletoResponseDTO toBoloCompletoResponse(Bolo bolo) {
        if (bolo == null) {
            return null;
        }

        return new BoloCompletoResponseDTO(
                bolo.getId(),
                toRecheioPedidoResponse(bolo.getRecheioPedido()),
                toMassaResponse(bolo.getMassa()),
                toCoberturaResponse(bolo.getCobertura()),
                toDecoracaoResponse(bolo.getDecoracao()),
                bolo.getFormato(),
                bolo.getTamanho()
        );
    }

    private RecheioPedidoResponseDTO toRecheioPedidoResponse(Integer recheioPedidoId) {
        if (recheioPedidoId == null || !Boolean.TRUE.equals(recheioPedidoGateway.existsByIdAndIsAtivoTrue(recheioPedidoId))) {
            return null;
        }

        try {
            RecheioPedido recheioPedido = recheioPedidoGateway.findById(recheioPedidoId);
            RecheioPedidoProjection projection = recheioPedido.getRecheioExclusivo() != null
                    ? recheioPedidoGateway.buscarRecheioPedidoExclusivoPorId(recheioPedidoId)
                    : recheioPedidoGateway.buscarRecheioPedidoUnitariosPorId(recheioPedidoId);

            return projection == null ? null : recheioPedidoMapper.toResponse(projection);
        } catch (EntidadeNaoEncontradaException e) {
            return null;
        }
    }

    private MassaResponseDTO toMassaResponse(Integer massaId) {
        if (massaId == null || !Boolean.TRUE.equals(massaGateway.existsByIdAndIsAtivo(massaId, true))) {
            return null;
        }

        try {
            Massa massa = massaGateway.findById(massaId);
            return massaMapper.toResponse(massa);
        } catch (EntidadeNaoEncontradaException e) {
            return null;
        }
    }

    private CoberturaResponseDTO toCoberturaResponse(Integer coberturaId) {
        if (coberturaId == null || !Boolean.TRUE.equals(coberturaGateway.existsByIdAndIsAtivoTrue(coberturaId))) {
            return null;
        }

        try {
            Cobertura cobertura = coberturaGateway.findById(coberturaId);
            return coberturaMapper.toResponse(cobertura);
        } catch (EntidadeNaoEncontradaException e) {
            return null;
        }
    }

    private DecoracaoResponseDTO toDecoracaoResponse(Integer decoracaoId) {
        if (decoracaoId == null) {
            return null;
        }

        try {
            Decoracao decoracao = decoracaoGateway.findById(decoracaoId);
            return decoracaoMapper.toResponse(decoracao);
        } catch (EntidadeNaoEncontradaException e) {
            return null;
        }
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

