package com.carambolos.carambolosapi.infrastructure.gateways.mapper;

import com.carambolos.carambolosapi.application.gateways.AdicionalDecoracaoGateway;
import com.carambolos.carambolosapi.application.gateways.BoloGateway;
import com.carambolos.carambolosapi.application.gateways.CoberturaGateway;
import com.carambolos.carambolosapi.application.gateways.DecoracaoGateway;
import com.carambolos.carambolosapi.application.gateways.EnderecoGateway;
import com.carambolos.carambolosapi.application.gateways.MassaGateway;
import com.carambolos.carambolosapi.application.gateways.RecheioPedidoGateway;
import com.carambolos.carambolosapi.application.gateways.UsuarioGateway;
import com.carambolos.carambolosapi.domain.entity.AdicionalItem;
import com.carambolos.carambolosapi.domain.entity.Bolo;
import com.carambolos.carambolosapi.domain.entity.Cobertura;
import com.carambolos.carambolosapi.domain.entity.Decoracao;
import com.carambolos.carambolosapi.domain.entity.Endereco;
import com.carambolos.carambolosapi.domain.entity.Massa;
import com.carambolos.carambolosapi.domain.entity.PedidoBolo;
import com.carambolos.carambolosapi.domain.entity.RecheioPedido;
import com.carambolos.carambolosapi.domain.entity.ResumoPedido;
import com.carambolos.carambolosapi.domain.entity.Usuario;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.ResumoPedidoRepository;
import com.carambolos.carambolosapi.infrastructure.persistence.projection.RecheioPedidoProjection;
import com.carambolos.carambolosapi.infrastructure.web.response.AdicionalResponseDTO;
import com.carambolos.carambolosapi.infrastructure.web.response.BoloCompletoResponseDTO;
import com.carambolos.carambolosapi.infrastructure.web.response.CoberturaResponseDTO;
import com.carambolos.carambolosapi.infrastructure.web.response.DecoracaoResponseDTO;
import com.carambolos.carambolosapi.infrastructure.web.response.MassaResponseDTO;
import com.carambolos.carambolosapi.infrastructure.web.response.PedidoBoloCompletoResponseDTO;
import com.carambolos.carambolosapi.infrastructure.web.response.RecheioPedidoResponseDTO;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PedidoBoloCompletoMapper {
    private final BoloGateway boloGateway;
    private final EnderecoGateway enderecoGateway;
    private final UsuarioGateway usuarioGateway;
    private final MassaGateway massaGateway;
    private final RecheioPedidoGateway recheioPedidoGateway;
    private final CoberturaGateway coberturaGateway;
    private final DecoracaoGateway decoracaoGateway;
    private final AdicionalDecoracaoGateway adicionalDecoracaoGateway;
    private final MassaMapper massaMapper;
    private final RecheioPedidoMapper recheioPedidoMapper;
    private final CoberturaMapper coberturaMapper;
    private final DecoracaoMapper decoracaoMapper;
    private final ResumoPedidoRepository resumoPedidoRepository;

    public PedidoBoloCompletoMapper(
            BoloGateway boloGateway,
            EnderecoGateway enderecoGateway,
            UsuarioGateway usuarioGateway,
            MassaGateway massaGateway,
            RecheioPedidoGateway recheioPedidoGateway,
            CoberturaGateway coberturaGateway,
            DecoracaoGateway decoracaoGateway,
            AdicionalDecoracaoGateway adicionalDecoracaoGateway,
            MassaMapper massaMapper,
            RecheioPedidoMapper recheioPedidoMapper,
            CoberturaMapper coberturaMapper,
            DecoracaoMapper decoracaoMapper,
            ResumoPedidoRepository resumoPedidoRepository
    ) {
        this.boloGateway = boloGateway;
        this.enderecoGateway = enderecoGateway;
        this.usuarioGateway = usuarioGateway;
        this.massaGateway = massaGateway;
        this.recheioPedidoGateway = recheioPedidoGateway;
        this.coberturaGateway = coberturaGateway;
        this.decoracaoGateway = decoracaoGateway;
        this.adicionalDecoracaoGateway = adicionalDecoracaoGateway;
        this.massaMapper = massaMapper;
        this.recheioPedidoMapper = recheioPedidoMapper;
        this.coberturaMapper = coberturaMapper;
        this.decoracaoMapper = decoracaoMapper;
        this.resumoPedidoRepository = resumoPedidoRepository;
    }

    public List<PedidoBoloCompletoResponseDTO> toResponse(List<PedidoBolo> pedidos) {
        if (pedidos.isEmpty()) {
            return List.of();
        }

        Map<Integer, ResumoPedido> resumoByPedidoBoloId = carregarResumoPorPedidoBoloId(pedidos);
        Map<Integer, MassaResponseDTO> massaCache = new HashMap<>();
        Map<Integer, RecheioPedidoResponseDTO> recheioCache = new HashMap<>();
        Map<Integer, CoberturaResponseDTO> coberturaCache = new HashMap<>();
        Map<Integer, DecoracaoResponseDTO> decoracaoCache = new HashMap<>();
        Map<Integer, List<AdicionalResponseDTO>> adicionaisDecoracaoCache = new HashMap<>();

        return pedidos.stream()
                .map(pedido -> {
                    try {
                        return toResponse(
                                pedido,
                                resumoByPedidoBoloId.get(pedido.getId()),
                                massaCache,
                                recheioCache,
                                coberturaCache,
                                decoracaoCache,
                                adicionaisDecoracaoCache
                        );
                    } catch (Exception ignored) {
                        return toResponseFallback(pedido, resumoByPedidoBoloId.get(pedido.getId()));
                    }
                })
                .toList();
    }

    private PedidoBoloCompletoResponseDTO toResponse(
            PedidoBolo pedido,
            ResumoPedido resumo,
            Map<Integer, MassaResponseDTO> massaCache,
            Map<Integer, RecheioPedidoResponseDTO> recheioCache,
            Map<Integer, CoberturaResponseDTO> coberturaCache,
            Map<Integer, DecoracaoResponseDTO> decoracaoCache,
            Map<Integer, List<AdicionalResponseDTO>> adicionaisDecoracaoCache
    ) {
        BoloCompletoResponseDTO bolo = null;
        List<AdicionalResponseDTO> adicionaisDecoracao = List.of();

        if (pedido.getBoloId() != null && Boolean.TRUE.equals(boloGateway.existsByIdAndIsAtivoTrue(pedido.getBoloId()))) {
            Bolo boloDomain = boloGateway.findById(pedido.getBoloId());
            bolo = toBoloCompletoResponse(
                    boloDomain,
                    massaCache,
                    recheioCache,
                    coberturaCache,
                    decoracaoCache
            );
            if (boloDomain.getDecoracao() != null) {
                adicionaisDecoracao = carregarAdicionaisDecoracao(
                        boloDomain.getDecoracao(),
                        adicionaisDecoracaoCache
                );
            }
        }

        Endereco endereco = null;
        if (pedido.getEnderecoId() != null) {
            try {
                endereco = enderecoGateway.buscarPorId(pedido.getEnderecoId());
            } catch (Exception ignored) {
                endereco = null;
            }
        }

        Usuario usuario = null;
        if (pedido.getUsuarioId() != null) {
            usuario = usuarioGateway.buscarPorId(pedido.getUsuarioId());
        }

        return buildResponse(pedido, resumo, bolo, endereco, usuario, adicionaisDecoracao);
    }

    private PedidoBoloCompletoResponseDTO toResponseFallback(PedidoBolo pedido, ResumoPedido resumo) {
        return buildResponse(pedido, resumo, null, null, null, List.of());
    }

    private PedidoBoloCompletoResponseDTO buildResponse(
            PedidoBolo pedido,
            ResumoPedido resumo,
            BoloCompletoResponseDTO bolo,
            Endereco endereco,
            Usuario usuario,
            List<AdicionalResponseDTO> adicionaisDecoracao
    ) {
        return new PedidoBoloCompletoResponseDTO(
                pedido.getId(),
                resumo != null ? resumo.getId() : null,
                bolo,
                EnderecoMapper.toResponseDTO(endereco),
                usuario == null ? null : UsuarioMapper.toResponseDTO(usuario),
                resumo != null ? resumo.getStatus() : null,
                pedido.getObservacao(),
                pedido.getDataPrevisaoEntrega(),
                pedido.getDataUltimaAtualizacao(),
                pedido.getTipoEntrega(),
                pedido.getNomeCliente(),
                pedido.getTelefoneCliente(),
                pedido.getHorarioRetirada(),
                adicionaisDecoracao
        );
    }

    private BoloCompletoResponseDTO toBoloCompletoResponse(
            Bolo bolo,
            Map<Integer, MassaResponseDTO> massaCache,
            Map<Integer, RecheioPedidoResponseDTO> recheioCache,
            Map<Integer, CoberturaResponseDTO> coberturaCache,
            Map<Integer, DecoracaoResponseDTO> decoracaoCache
    ) {
        if (bolo == null) {
            return null;
        }

        MassaResponseDTO massa = resolveMassa(bolo.getMassa(), massaCache);
        RecheioPedidoResponseDTO recheioPedido = resolveRecheio(bolo.getRecheioPedido(), recheioCache);
        CoberturaResponseDTO cobertura = resolveCobertura(bolo.getCobertura(), coberturaCache);
        DecoracaoResponseDTO decoracao = resolveDecoracao(bolo.getDecoracao(), decoracaoCache);

        return new BoloCompletoResponseDTO(
                bolo.getId(),
                recheioPedido,
                massa,
                cobertura,
                decoracao,
                bolo.getFormato(),
                bolo.getTamanho()
        );
    }

    private MassaResponseDTO resolveMassa(Integer massaId, Map<Integer, MassaResponseDTO> cache) {
        if (massaId == null) {
            return null;
        }
        return cache.computeIfAbsent(massaId, id -> {
            try {
                Massa massaDomain = massaGateway.findById(id);
                return massaMapper.toResponse(massaDomain);
            } catch (Exception e) {
                return null;
            }
        });
    }

    private RecheioPedidoResponseDTO resolveRecheio(
            Integer recheioPedidoId,
            Map<Integer, RecheioPedidoResponseDTO> cache
    ) {
        if (recheioPedidoId == null) {
            return null;
        }
        return cache.computeIfAbsent(recheioPedidoId, this::carregarRecheioPedido);
    }

    private CoberturaResponseDTO resolveCobertura(Integer coberturaId, Map<Integer, CoberturaResponseDTO> cache) {
        if (coberturaId == null) {
            return null;
        }
        return cache.computeIfAbsent(coberturaId, id -> {
            try {
                Cobertura coberturaDomain = coberturaGateway.findById(id);
                return coberturaDomain != null ? coberturaMapper.toResponse(coberturaDomain) : null;
            } catch (Exception e) {
                return null;
            }
        });
    }

    private DecoracaoResponseDTO resolveDecoracao(Integer decoracaoId, Map<Integer, DecoracaoResponseDTO> cache) {
        if (decoracaoId == null) {
            return null;
        }
        return cache.computeIfAbsent(decoracaoId, id -> {
            try {
                Decoracao decoracaoDomain = decoracaoGateway.findById(id);
                return decoracaoDomain != null ? decoracaoMapper.toResponse(decoracaoDomain) : null;
            } catch (Exception e) {
                return null;
            }
        });
    }

    private RecheioPedidoResponseDTO carregarRecheioPedido(Integer recheioPedidoId) {
        try {
            if (!recheioPedidoGateway.existsById(recheioPedidoId)) {
                return null;
            }
            RecheioPedido recheio = recheioPedidoGateway.findById(recheioPedidoId);
            RecheioPedidoProjection projection;
            if (recheio.getRecheioExclusivo() != null) {
                projection = recheioPedidoGateway.buscarRecheioPedidoExclusivoPorId(recheio.getId());
            } else {
                projection = recheioPedidoGateway.buscarRecheioPedidoUnitariosPorId(recheio.getId());
            }
            return recheioPedidoMapper.toResponse(projection);
        } catch (Exception e) {
            return null;
        }
    }

    private List<AdicionalResponseDTO> carregarAdicionaisDecoracao(
            Integer decoracaoId,
            Map<Integer, List<AdicionalResponseDTO>> cache
    ) {
        return cache.computeIfAbsent(decoracaoId, id ->
                adicionalDecoracaoGateway.buscarAdicionaisPorDecoracaoId(id)
                        .map(summary -> {
                            if (summary.getAdicionaisPossiveis() == null) {
                                return List.<AdicionalResponseDTO>of();
                            }
                            return summary.getAdicionaisPossiveis().stream()
                                    .map(this::toAdicionalResponse)
                                    .toList();
                        })
                        .orElse(List.of())
        );
    }

    private AdicionalResponseDTO toAdicionalResponse(AdicionalItem item) {
        return new AdicionalResponseDTO(item.getId(), item.getDescricao(), true);
    }

    private Map<Integer, ResumoPedido> carregarResumoPorPedidoBoloId(List<PedidoBolo> pedidos) {
        List<Integer> pedidoIds = pedidos.stream().map(PedidoBolo::getId).toList();
        List<ResumoPedido> resumos = resumoPedidoRepository
                .findByPedidoBoloIdInAndIsAtivoTrueOrderByDataPedidoDesc(pedidoIds);

        Map<Integer, ResumoPedido> resumoByPedidoBoloId = new LinkedHashMap<>();
        for (ResumoPedido resumo : resumos) {
            resumoByPedidoBoloId.putIfAbsent(resumo.getPedidoBoloId(), resumo);
        }
        return resumoByPedidoBoloId;
    }
}
