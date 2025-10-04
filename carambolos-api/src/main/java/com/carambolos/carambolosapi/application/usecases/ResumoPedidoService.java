package com.carambolos.carambolosapi.application.usecases;

import com.carambolos.carambolosapi.infrastructure.persistence.entity.FornadaDaVez;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.PedidoFornada;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.EnderecoMapper;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.EnderecoEntity;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.*;
import com.carambolos.carambolosapi.infrastructure.web.response.DetalhePedidoBoloDTO;
import com.carambolos.carambolosapi.infrastructure.web.response.DetalhePedidoFornadaDTO;
import com.carambolos.carambolosapi.infrastructure.web.response.EnderecoResponseDTO;
import com.carambolos.carambolosapi.domain.entity.*;
import com.carambolos.carambolosapi.application.exception.EntidadeImprocessavelException;
import com.carambolos.carambolosapi.application.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.domain.enums.StatusEnum;
import com.carambolos.carambolosapi.domain.enums.TipoEntregaEnum;
import com.carambolos.carambolosapi.infrastructure.web.response.ResumoPedidoMensagemResponseDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@SuppressWarnings("unused")
public class ResumoPedidoService {

    private final ResumoPedidoRepository resumoPedidoRepository;
    private final PedidoBoloRepository pedidoBoloRepository;
    private final PedidoFornadaRepository pedidoFornadaRepository;
    private final FornadaDaVezRepository fornadaDaVezRepository;
    private final ProdutoFornadaRepository produtoFornadaRepository;
    private final BoloRepository boloRepository;
    private final RecheioPedidoRepository recheioPedidoRepository;
    private final RecheioExclusivoRepository recheioExclusivoRepository;
    private final RecheioUnitarioRepository recheioUnitarioRepository;
    private final MassaRepository massaRepository;
    private final CoberturaRepository coberturaRepository;
    private final EnderecoRepository enderecoRepository;
    private final EnderecoMapper enderecoMapper;

    public ResumoPedidoService(
            ResumoPedidoRepository resumoPedidoRepository,
            PedidoBoloRepository pedidoBoloRepository,
            PedidoFornadaRepository pedidoFornadaRepository,
            FornadaDaVezRepository fornadaDaVezRepository,
            ProdutoFornadaRepository produtoFornadaRepository,
            BoloRepository boloRepository,
            RecheioPedidoRepository recheioPedidoRepository,
            RecheioExclusivoRepository recheioExclusivoRepository,
            RecheioUnitarioRepository recheioUnitarioRepository,
            MassaRepository massaRepository,
            CoberturaRepository coberturaRepository,
            EnderecoRepository enderecoRepository,
            EnderecoMapper enderecoMapper
    ) {
        this.resumoPedidoRepository = resumoPedidoRepository;
        this.pedidoBoloRepository = pedidoBoloRepository;
        this.pedidoFornadaRepository = pedidoFornadaRepository;
        this.fornadaDaVezRepository = fornadaDaVezRepository;
        this.produtoFornadaRepository = produtoFornadaRepository;
        this.boloRepository = boloRepository;
        this.recheioPedidoRepository = recheioPedidoRepository;
        this.recheioExclusivoRepository = recheioExclusivoRepository;
        this.recheioUnitarioRepository = recheioUnitarioRepository;
        this.massaRepository = massaRepository;
        this.coberturaRepository = coberturaRepository;
        this.enderecoRepository = enderecoRepository;
        this.enderecoMapper = enderecoMapper;
    }

    public List<ResumoPedido> listarResumosPedidos() {
        return resumoPedidoRepository.findAllByIsAtivoTrue();
    }

    public ResumoPedido buscarResumoPedidoPorId(Integer id) {
        return resumoPedidoRepository.findByIdAndIsAtivoTrue(id)
                .orElseThrow(() -> new RuntimeException("Resumo de pedido não encontrado"));
    }

    //LocalDateTime?
    public List<ResumoPedido> buscarResumosPedidosPorDataPedido(LocalDate dataPedido) {
        LocalDateTime comecoData = dataPedido.atStartOfDay();
        LocalDateTime fimData = dataPedido.atTime(23, 59, 59);
        return resumoPedidoRepository.findByDataPedidoBetweenAndIsAtivoTrue(comecoData, fimData);
    }

    public List<ResumoPedido> buscarResumosPedidosPorStatus(StatusEnum status) {
        return resumoPedidoRepository.findByStatusAndIsAtivoTrue(status);
    }

    public ResumoPedido cadastrarResumoPedido(ResumoPedido resumoPedido) {
        validarReferencias(resumoPedido);

        resumoPedido.setDataPedido(LocalDateTime.now());
        resumoPedido.setStatus(StatusEnum.PENDENTE);

        if (resumoPedido.getPedidoFornadaId() != null) {
            resumoPedido.setValor(calcularValorPedidoFornada(resumoPedido.getPedidoFornadaId()));
        } else if (resumoPedido.getPedidoBoloId() != null) {
            resumoPedido.setValor(calcularValorPedidoBolo(resumoPedido.getPedidoBoloId()));
        }

        return resumoPedidoRepository.save(resumoPedido);
    }

    public ResumoPedido atualizarResumoPedido(Integer id, ResumoPedido resumoPedido) {
        if (!resumoPedidoRepository.existsByIdAndIsAtivoTrue(id)) {
            throw new EntidadeNaoEncontradaException("Resumo de pedido não encontrado");
        }
        validarReferencias(resumoPedido);
        resumoPedido.setId(id);
        return resumoPedidoRepository.save(resumoPedido);
    }

    public void deletarResumoPedido(Integer id) {
        ResumoPedido resumoPedido = buscarResumoPedidoPorId(id);
        resumoPedido.setAtivo(false);
        resumoPedidoRepository.save(resumoPedido);
    }

    public List<ResumoPedido> listarResumosPedidosBolo() {
        return resumoPedidoRepository.findByPedidoBoloIdIsNotNullAndIsAtivoTrue();
    }

    public List<ResumoPedido> listarResumosPedidosFornada() {
        return resumoPedidoRepository.findByPedidoFornadaIdIsNotNullAndIsAtivoTrue();
    }

    public ResumoPedido alterarStatus(Integer id, StatusEnum novoStatus) {
        ResumoPedido resumoPedido = buscarResumoPedidoPorId(id);
        StatusEnum statusAtual = resumoPedido.getStatus();
        if (!isTransicaoStatusValida(statusAtual, novoStatus)) {
            throw new EntidadeImprocessavelException("Não é possível alterar o status de %s para %s".formatted(statusAtual, novoStatus));
        }

        try {
            if (resumoPedido.getPedidoFornadaId() != null) {
                var pedidoFornadaOpt = pedidoFornadaRepository.findById(resumoPedido.getPedidoFornadaId());
                if (pedidoFornadaOpt.isPresent()) {
                    var pedidoFornada = pedidoFornadaOpt.get();
                    var fdvOpt = fornadaDaVezRepository.findById(pedidoFornada.getFornadaDaVez());
                    if (fdvOpt.isPresent()) {
                        var fdv = fdvOpt.get();
                        if (novoStatus == StatusEnum.CANCELADO && statusAtual != StatusEnum.CANCELADO) {
                            int novaQtd = (fdv.getQuantidade() != null ? fdv.getQuantidade() : 0) + pedidoFornada.getQuantidade();
                            fdv.setQuantidade(novaQtd);
                            fornadaDaVezRepository.save(fdv);
                        }
                        if (statusAtual == StatusEnum.CANCELADO && novoStatus != StatusEnum.CANCELADO) {
                            int atual = (fdv.getQuantidade() != null ? fdv.getQuantidade() : 0);
                            int novaQtd = Math.max(0, atual - pedidoFornada.getQuantidade());
                            fdv.setQuantidade(novaQtd);
                            fornadaDaVezRepository.save(fdv);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[ESTOQUE] Falha ao ajustar estoque ao alterar status do pedido fornada: " + e.getMessage());
        }

        resumoPedido.setStatus(novoStatus);
        return resumoPedidoRepository.save(resumoPedido);
    }

    public DetalhePedidoBoloDTO obterDetalhePedidoBolo(Integer pedidoResumoId) {
        try {
            ResumoPedido resumoPedido = resumoPedidoRepository
                    .findTop1ByPedidoBoloIdAndIsAtivoTrueOrderByDataPedidoDesc(pedidoResumoId)
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Resumo de pedido (bolo) não encontrado"));

            if (resumoPedido.getPedidoBoloId() == null) {
                throw new EntidadeImprocessavelException("O resumo de pedido #" + pedidoResumoId + " não está vinculado a um pedido de bolo");
            }

            PedidoBolo pedido = pedidoBoloRepository.findById(resumoPedido.getPedidoBoloId())
                    .filter(PedidoBolo::getAtivo)
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Pedido com o id %d não encontrado".formatted(resumoPedido.getPedidoBoloId())));

            Bolo bolo = boloRepository.findById(pedido.getBoloId())
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Bolo com id %d não encontrado".formatted(pedido.getBoloId())));

            String massaNome = massaRepository.findById(bolo.getMassa())
                    .map(Massa::getSabor)
                    .orElse("Não especificada");

            String recheioNome = "Não especificado";
            try {
                recheioNome = recheioPedidoRepository.findById(bolo.getRecheioPedido())
                        .map(recheioPedido -> {
                            StringBuilder nomes = new StringBuilder();

                            if (recheioPedido.getRecheioExclusivo() != null) {
                                try {
                                    return recheioExclusivoRepository.buscarRecheioExclusivoPorId(recheioPedido.getRecheioExclusivo())
                                            .getNome() + " (" +
                                            recheioExclusivoRepository.buscarRecheioExclusivoPorId(recheioPedido.getRecheioExclusivo()).getSabor1() +
                                            " + " +
                                            recheioExclusivoRepository.buscarRecheioExclusivoPorId(recheioPedido.getRecheioExclusivo()).getSabor2() +
                                            ")";
                                } catch (Exception e) {
                                    return "Recheio exclusivo não encontrado";
                                }
                            } else {
                                if (recheioPedido.getRecheioUnitarioId1() != null) {
                                    recheioUnitarioRepository.findById(recheioPedido.getRecheioUnitarioId1())
                                            .ifPresent(recheio -> nomes.append(recheio.getSabor()));
                                }

                                if (recheioPedido.getRecheioUnitarioId2() != null) {
                                    if (!nomes.isEmpty()) {
                                        nomes.append(" + ");
                                    }
                                    recheioUnitarioRepository.findById(recheioPedido.getRecheioUnitarioId2())
                                            .ifPresent(recheio -> nomes.append(recheio.getSabor()));
                                }
                                return nomes.toString();
                            }
                        })
                        .orElse("Não especificado");
            } catch (Exception e) {
                System.err.println("Erro ao buscar recheio: " + e.getMessage());
                recheioNome = "Erro ao carregar recheio";
            }

            String coberturaNome = coberturaRepository.findById(bolo.getCobertura())
                    .map(Cobertura::getDescricao)
                    .orElse("Não especificada");

            String imagemUrl = "";
            String[] imagensDecoracao = new String[]{};
            try {
                List<ImagemDecoracao> imagens = boloRepository.findAllImagensByDecoracao(bolo.getDecoracao());
                if (imagens != null && !imagens.isEmpty()) {
                    // Usar a primeira imagem como imagem principal
                    imagemUrl = imagens.get(0).getUrl();
                    // Converter todas as imagens para array de strings
                    imagensDecoracao = imagens.stream()
                            .map(ImagemDecoracao::getUrl)
                            .filter(url -> url != null && !url.trim().isEmpty())
                            .toArray(String[]::new);
                }
            } catch (Exception e) {
                System.err.println("Erro ao buscar imagens da decoração: " + e.getMessage());
                imagemUrl = "";
                imagensDecoracao = new String[]{};
            }

            EnderecoResponseDTO enderecoDTO = null;
            try {
                if (pedido.getTipoEntrega() == TipoEntregaEnum.ENTREGA && pedido.getEnderecoId() != null) {
                    enderecoDTO = enderecoRepository.findById(pedido.getEnderecoId())
                            .filter(EnderecoEntity::isAtivo)
                            .map(e -> EnderecoMapper.toResponseDTO(enderecoMapper.toDomain(e)))
                            .orElse(null);
                }
            } catch (Exception e) {
                System.err.println("Erro ao buscar endereço: " + e.getMessage());
                enderecoDTO = null;
            }

            // Adicionais: extrair da observação
            String adicionais = null;
            try {
                String obs = pedido.getObservacao();
                if (obs != null && obs.contains("Adicionais:")) {
                    String adicionaisTexto = obs.substring(obs.indexOf("Adicionais:") + 11).trim();
                    // Se há quebra de linha, pegar apenas a primeira linha dos adicionais
                    if (adicionaisTexto.contains("\n")) {
                        adicionaisTexto = adicionaisTexto.substring(0, adicionaisTexto.indexOf("\n")).trim();
                    }
                    adicionais = adicionaisTexto;
                }
            } catch (Exception e) {
                System.err.println("Erro ao extrair adicionais da observação: " + e.getMessage());
            }

            return DetalhePedidoBoloDTO.toDetalhePedidoResponse(
                    pedido.getId(),
                    bolo.getTamanho(),
                    bolo.getFormato(),
                    massaNome,
                    recheioNome,
                    coberturaNome,
                    imagemUrl,
                    imagensDecoracao,
                    adicionais,
                    pedido.getObservacao(),
                    pedido.getTipoEntrega(),
                    resumoPedido.getDataPedido(),
                    pedido.getNomeCliente(),
                    pedido.getTelefoneCliente(),
                    pedido.getHorarioRetirada(),
                    enderecoDTO
            );
        } catch (Exception e) {
            System.err.println("Erro geral em obterDetalhePedidoBolo: " + e.getMessage());
            e.printStackTrace();
            throw new EntidadeImprocessavelException("Erro ao buscar detalhes do pedido de bolo: " + e.getMessage());
        }
    }

    public DetalhePedidoFornadaDTO obterDetalhePedidoFornada(Integer pedidoResumoId) {
        try {
            ResumoPedido resumoPedido = resumoPedidoRepository
                    .findTop1ByPedidoFornadaIdAndIsAtivoTrueOrderByDataPedidoDesc(pedidoResumoId)
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Resumo de pedido (fornada) não encontrado"));

            if (resumoPedido.getPedidoFornadaId() == null) {
                throw new EntidadeImprocessavelException("O resumo de pedido #" + pedidoResumoId + " não está vinculado a um pedido de fornada");
            }

            PedidoFornada pedidoFornada = pedidoFornadaRepository.findById(resumoPedido.getPedidoFornadaId())
                    .filter(PedidoFornada::isAtivo)
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Pedido de fornada com ID " + resumoPedido.getPedidoFornadaId() + " não encontrado."));

            FornadaDaVez fornadaDaVez = fornadaDaVezRepository.findById(pedidoFornada.getFornadaDaVez())
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("FornadaDaVez com ID " + pedidoFornada.getFornadaDaVez() + " não encontrada."));

            String produtoFornada = "Produto não especificado";
            try {
                if (fornadaDaVez.getProdutoFornada() != null) {
                    produtoFornada = produtoFornadaRepository.findById(fornadaDaVez.getProdutoFornada())
                            .map(ProdutoFornada::getProduto)
                            .orElse("Produto não especificado");
                }
            } catch (Exception e) {
                System.err.println("Erro ao buscar produto da fornada: " + e.getMessage());
                produtoFornada = "Erro ao carregar produto";
            }

            EnderecoResponseDTO enderecoDTO = null;
            try {
                if (pedidoFornada.getTipoEntrega() == TipoEntregaEnum.ENTREGA && pedidoFornada.getEndereco() != null) {
                    enderecoDTO = enderecoRepository.findById(pedidoFornada.getEndereco())
                            .filter(EnderecoEntity::isAtivo)
                            .map(e -> EnderecoMapper.toResponseDTO(enderecoMapper.toDomain(e)))
                            .orElse(null);
                }
            } catch (Exception e) {
                System.err.println("Erro ao buscar endereço da fornada: " + e.getMessage());
                enderecoDTO = null;
            }

            return DetalhePedidoFornadaDTO.toDetalhePedidoResponse(
                    pedidoFornada.getQuantidade(),
                    produtoFornada,
                    pedidoFornada.getTipoEntrega(),
                    pedidoFornada.getNomeCliente(),
                    pedidoFornada.getTelefoneCliente(),
                    pedidoFornada.getObservacoes(),
                    resumoPedido.getDataPedido(),
                    enderecoDTO
            );
        } catch (Exception e) {
            System.err.println("Erro geral em obterDetalhePedidoFornada: " + e.getMessage());
            e.printStackTrace();
            throw new EntidadeImprocessavelException("Erro ao buscar detalhes do pedido de fornada: " + e.getMessage());
        }
    }

    public String gerarMensagensConsolidadas(List<Integer> idsResumo) {
        if (idsResumo == null || idsResumo.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Integer id : idsResumo) {
            var opt = resumoPedidoRepository.findByIdAndIsAtivoTrue(id);
            if (opt.isEmpty()) continue;
            var rp = opt.get();
            String msg = ResumoPedidoMensagemResponseDTO
                    .toResumoPedidoMensagemResponse(rp).mensagem();
            if (msg != null && !msg.isBlank()) {
                if (!sb.isEmpty()) sb.append("\n\n");
                sb.append(msg);
            }
        }
        return sb.toString();
    }

    private void validarReferencias(ResumoPedido resumoPedido) {
        if (resumoPedido.getPedidoBoloId() == null && resumoPedido.getPedidoFornadaId() == null) {
            throw new IllegalArgumentException("O resumo de pedido deve estar vinculado a um pedido de bolo ou pedido de fornada");
        }

        if (resumoPedido.getPedidoBoloId() != null) {
            validarPedidoBolo(resumoPedido.getPedidoBoloId());
        }

        if(resumoPedido.getPedidoFornadaId() != null) {
            validarPedidoFornada(resumoPedido.getPedidoFornadaId());
        }
    }

    private void validarPedidoBolo(Integer pedidoBoloId) {
        if (!pedidoBoloRepository.existsByIdAndIsAtivoTrue(pedidoBoloId)) {
            throw new EntidadeNaoEncontradaException("Pedido de bolo com ID %d não encontrado".formatted(pedidoBoloId));
        }
    }

    private void validarPedidoFornada(Integer pedidoFornadaId) {
        if (!pedidoFornadaRepository.existsByIdAndIsAtivoTrue(pedidoFornadaId)) {
            throw new EntidadeNaoEncontradaException("Pedido de fornada com ID %d não encontrado".formatted(pedidoFornadaId));
        }
    }

    private boolean isTransicaoStatusValida(StatusEnum statusAtual, StatusEnum novoStatus) {
        if (statusAtual == novoStatus) {
            return false;
        }

        return true;
    }

    private Double calcularValorPedidoFornada(Integer pedidoFornadaId) {
        var pedidoFornada = pedidoFornadaRepository.findById(pedidoFornadaId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Pedido fornada não encontrado"));

        var fornadaDaVez = fornadaDaVezRepository.findById(pedidoFornada.getFornadaDaVez())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Fornada da vez não encontrada"));

        var produtoFornada = produtoFornadaRepository.findById(fornadaDaVez.getProdutoFornada())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Produto da fornada não encontrado"));

        Double valorProduto = produtoFornada.getValor();
        return valorProduto * pedidoFornada.getQuantidade();
    }

    private Double calcularValorPedidoBolo(Integer pedidoBoloId) {
        var pedidoBolo = pedidoBoloRepository.findById(pedidoBoloId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Pedido bolo não encontrado"));

        var bolo = boloRepository.findById(pedidoBolo.getBoloId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Bolo não encontrado"));

        Double valorTamanho = 0.0;
        if (bolo.getTamanho() != null) {
            switch (bolo.getTamanho()) {
                case TAMANHO_5 -> valorTamanho = 50.0;
                case TAMANHO_7 -> valorTamanho = 100.0;
                case TAMANHO_12 -> valorTamanho = 150.0;
                case TAMANHO_15 -> valorTamanho = 200.0;
                case TAMANHO_17 -> valorTamanho = 250.0;
            }
        }

        Double valorRecheio = 0.0;
        if (bolo.getRecheioPedido() != null) {
            var recheioPedido = recheioPedidoRepository.findById(bolo.getRecheioPedido())
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Recheio do bolo não encontrado"));

            if (recheioPedido.getRecheioExclusivo() != null) {
                var recheioExclusivo = recheioExclusivoRepository.findById(recheioPedido.getRecheioExclusivo())
                        .orElseThrow(() -> new EntidadeNaoEncontradaException("Recheio exclusivo não encontrado"));

                if (recheioExclusivo.getRecheioUnitarioId1() != null) {
                    var recheioUnitario1 = recheioUnitarioRepository.findById(recheioExclusivo.getRecheioUnitarioId1())
                            .orElse(null);
                    if (recheioUnitario1 != null && recheioUnitario1.getValor() != null) {
                        valorRecheio += recheioUnitario1.getValor();
                    }
                }

                if (recheioExclusivo.getRecheioUnitarioId2() != null) {
                    var recheioUnitario2 = recheioUnitarioRepository.findById(recheioExclusivo.getRecheioUnitarioId2())
                            .orElse(null);
                    if (recheioUnitario2 != null && recheioUnitario2.getValor() != null) {
                        valorRecheio += recheioUnitario2.getValor();
                    }
                }
            } else {
                if (recheioPedido.getRecheioUnitarioId1() != null) {
                    var recheioUnitario1 = recheioUnitarioRepository.findById(recheioPedido.getRecheioUnitarioId1())
                            .orElse(null);
                    if (recheioUnitario1 != null && recheioUnitario1.getValor() != null) {
                        valorRecheio += recheioUnitario1.getValor();
                    }
                }

                if (recheioPedido.getRecheioUnitarioId2() != null) {
                    var recheioUnitario2 = recheioUnitarioRepository.findById(recheioPedido.getRecheioUnitarioId2())
                            .orElse(null);
                    if (recheioUnitario2 != null && recheioUnitario2.getValor() != null) {
                        valorRecheio += recheioUnitario2.getValor();
                    }
                }
            }
        }

        Double valorMassa = 0.0;
        if (bolo.getMassa() != null) {
            var massa = massaRepository.findById(bolo.getMassa())
                    .orElse(null);
            if (massa != null && massa.getValor() != null) {
                valorMassa = massa.getValor();
            }
        }

        Double valorTotal = valorTamanho + valorRecheio + valorMassa;
        return valorTotal;
    }
}
