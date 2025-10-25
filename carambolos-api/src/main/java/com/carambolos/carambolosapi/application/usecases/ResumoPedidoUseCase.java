package com.carambolos.carambolosapi.application.usecases;

import com.carambolos.carambolosapi.application.exception.EntidadeImprocessavelException;
import com.carambolos.carambolosapi.application.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.application.gateways.*;
import com.carambolos.carambolosapi.domain.entity.*;
import com.carambolos.carambolosapi.domain.entity.PedidoFornada;
import com.carambolos.carambolosapi.domain.enums.StatusEnum;
import com.carambolos.carambolosapi.domain.enums.TipoEntregaEnum;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.EnderecoMapper;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.*;
import com.carambolos.carambolosapi.infrastructure.web.response.DetalhePedidoBoloDTO;
import com.carambolos.carambolosapi.infrastructure.web.response.DetalhePedidoFornadaDTO;
import com.carambolos.carambolosapi.infrastructure.web.response.EnderecoResponseDTO;
import com.carambolos.carambolosapi.infrastructure.web.response.ResumoPedidoMensagemResponseDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ResumoPedidoUseCase {
    private final ResumoPedidoGateway resumoPedidoGateway;
    private final PedidoBoloGateway pedidoBoloGateway;
    private final PedidoFornadaGateway pedidoFornadaGateway;
    private final FornadaDaVezGateway fornadaDaVezGateway;
    private final ProdutoFornadaGateway produtoFornadaGateway;
    private final BoloGateway boloGateway;
    private final RecheioPedidoGateway recheioPedidoGateway;
    private final RecheioExclusivoGateway recheioExclusivoGateway;
    private final RecheioUnitarioGateway recheioUnitarioGateway;
    private final MassaGateway massaGateway;
    private final CoberturaGateway coberturaGateway;
    private final EnderecoGateway enderecoGateway;
    private final EnderecoMapper enderecoMapper;

    public ResumoPedidoUseCase(ResumoPedidoGateway resumoPedidoGateway, PedidoBoloGateway pedidoBoloGateway, PedidoFornadaGateway pedidoFornadaGateway, FornadaDaVezGateway fornadaDaVezGateway, ProdutoFornadaGateway produtoFornadaGateway, BoloGateway boloGateway, RecheioPedidoGateway recheioPedidoGateway, RecheioExclusivoGateway recheioExclusivoGateway, RecheioUnitarioGateway recheioUnitarioGateway, MassaGateway massaGateway, CoberturaGateway coberturaGateway, EnderecoGateway enderecoGateway, EnderecoMapper enderecoMapper) {
        this.resumoPedidoGateway = resumoPedidoGateway;
        this.pedidoBoloGateway = pedidoBoloGateway;
        this.pedidoFornadaGateway = pedidoFornadaGateway;
        this.fornadaDaVezGateway = fornadaDaVezGateway;
        this.produtoFornadaGateway = produtoFornadaGateway;
        this.boloGateway = boloGateway;
        this.recheioPedidoGateway = recheioPedidoGateway;
        this.recheioExclusivoGateway = recheioExclusivoGateway;
        this.recheioUnitarioGateway = recheioUnitarioGateway;
        this.massaGateway = massaGateway;
        this.coberturaGateway = coberturaGateway;
        this.enderecoGateway = enderecoGateway;
        this.enderecoMapper = enderecoMapper;
    }

    public List<ResumoPedido> listarResumosPedidos() {
        return resumoPedidoGateway.findAllByIsAtivoTrue();
    }

    public ResumoPedido buscarResumoPedidoPorId(Integer id) {
        return resumoPedidoGateway.findByIdAndIsAtivoTrue(id);
    }

    public List<ResumoPedido> buscarResumosPedidosPorDataPedido(LocalDate dataPedido) {
        LocalDateTime comecoData = dataPedido.atStartOfDay();
        LocalDateTime fimData = dataPedido.atTime(23, 59, 59);
        return resumoPedidoGateway.findByDataPedidoBetweenAndIsAtivoTrue(comecoData, fimData);
    }

    public List<ResumoPedido> buscarResumosPedidosPorStatus(StatusEnum status) {
        return resumoPedidoGateway.findByStatusAndIsAtivoTrue(status);
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

        return resumoPedidoGateway.save(resumoPedido);
    }

    public ResumoPedido atualizarResumoPedido(Integer id, ResumoPedido resumoPedido) {
        if (!resumoPedidoGateway.existsByIdAndIsAtivoTrue(id)) {
            throw new EntidadeNaoEncontradaException("Resumo de pedido não encontrado");
        }
        validarReferencias(resumoPedido);
        resumoPedido.setId(id);
        return resumoPedidoGateway.save(resumoPedido);
    }

    public void deletarResumoPedido(Integer id) {
        ResumoPedido resumoPedido = resumoPedidoGateway.findByIdAndIsAtivoTrue(id);
        resumoPedido.setAtivo(false);
        resumoPedidoGateway.save(resumoPedido);
    }

    public List<ResumoPedido> listarResumosPedidosBolo() {
        return resumoPedidoGateway.findByPedidoBoloIdIsNotNullAndIsAtivoTrue();
    }

    public List<ResumoPedido> listarResumosPedidosFornada() {
        return resumoPedidoGateway.findByPedidoFornadaIdIsNotNullAndIsAtivoTrue();
    }

    public ResumoPedido alterarStatus(Integer id, StatusEnum novoStatus) {
        ResumoPedido resumoPedido = resumoPedidoGateway.findByIdAndIsAtivoTrue(id);
        StatusEnum statusAtual = resumoPedido.getStatus();
        if (!isTransicaoStatusValida(statusAtual, novoStatus)) {
            throw new EntidadeImprocessavelException("Não é possível alterar o status de %s para %s".formatted(statusAtual, novoStatus));
        }

        try {
            if (resumoPedido.getPedidoFornadaId() != null) {
                var pedidoFornadaOpt = pedidoFornadaGateway.findById(resumoPedido.getPedidoFornadaId());
                if (pedidoFornadaOpt.isPresent()) {
                    var pedidoFornada = pedidoFornadaOpt.get();
                    var fdvOpt = fornadaDaVezGateway.findById(pedidoFornada.getFornadaDaVez());
                    if (fdvOpt.isPresent()) {
                        var fdv = fdvOpt.get();
                        if (novoStatus == StatusEnum.CANCELADO && statusAtual != StatusEnum.CANCELADO) {
                            int novaQtd = (fdv.getQuantidade() != null ? fdv.getQuantidade() : 0) + pedidoFornada.getQuantidade();
                            fdv.setQuantidade(novaQtd);
                            fornadaDaVezGateway.save(fdv);
                        }
                        if (statusAtual == StatusEnum.CANCELADO && novoStatus != StatusEnum.CANCELADO) {
                            int atual = (fdv.getQuantidade() != null ? fdv.getQuantidade() : 0);
                            int novaQtd = Math.max(0, atual - pedidoFornada.getQuantidade());
                            fdv.setQuantidade(novaQtd);
                            fornadaDaVezGateway.save(fdv);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[ESTOQUE] Falha ao ajustar estoque ao alterar status do pedido fornada: " + e.getMessage());
        }

        resumoPedido.setStatus(novoStatus);
        return resumoPedidoGateway.save(resumoPedido);
    }

    public DetalhePedidoBoloDTO obterDetalhePedidoBolo(Integer pedidoResumoId) {
        try {
            ResumoPedido resumoPedido = resumoPedidoGateway
                    .findTop1ByPedidoBoloIdAndIsAtivoTrueOrderByDataPedidoDesc(pedidoResumoId);

            if (resumoPedido.getPedidoBoloId() == null) {
                throw new EntidadeImprocessavelException("O resumo de pedido #" + pedidoResumoId + " não está vinculado a um pedido de bolo");
            }

            PedidoBolo pedido = pedidoBoloGateway.findById(resumoPedido.getPedidoBoloId());

            Bolo bolo = boloGateway.findById(pedido.getBoloId());

            String massaNome = massaGateway.getMassaAtivaPorSabor(bolo.getMassa());

            String recheioNome = "Não especificado";
            try {
                recheioNome = recheioPedidoGateway.findEntityById(bolo.getRecheioPedido())
                        .map(recheioPedido -> {
                            StringBuilder nomes = new StringBuilder();

                            if (recheioPedido.getRecheioExclusivo() != null) {
                                try {
                                    return recheioExclusivoGateway.buscarRecheioExclusivoPorId(recheioPedido.getRecheioExclusivo())
                                            .getNome() + " (" +
                                            recheioExclusivoGateway.buscarRecheioExclusivoPorId(recheioPedido.getRecheioExclusivo()).getSabor1() +
                                            " + " +
                                            recheioExclusivoGateway.buscarRecheioExclusivoPorId(recheioPedido.getRecheioExclusivo()).getSabor2() +
                                            ")";
                                } catch (Exception e) {
                                    return "Recheio exclusivo não encontrado";
                                }
                            } else {
                                if (recheioPedido.getRecheioUnitarioId1() != null) {
                                    recheioUnitarioGateway.findEntityById(recheioPedido.getRecheioUnitarioId1())
                                            .ifPresent(recheio -> nomes.append(recheio.getSabor()));
                                }

                                if (recheioPedido.getRecheioUnitarioId2() != null) {
                                    if (!nomes.isEmpty()) {
                                        nomes.append(" + ");
                                    }
                                    recheioUnitarioGateway.findEntityById(recheioPedido.getRecheioUnitarioId2())
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

            String coberturaNome = coberturaGateway.findNomeById(bolo.getCobertura());

            String imagemUrl = "";
            String[] imagensDecoracao = new String[]{};
            try {
                List<ImagemDecoracao> imagens = boloGateway.findAllImagensByDecoracao(bolo.getDecoracao());
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
                    enderecoDTO = enderecoGateway.findEntityById(pedido.getEnderecoId())
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
            ResumoPedido resumoPedido = resumoPedidoGateway
                    .findTop1ByPedidoFornadaIdAndIsAtivoTrueOrderByDataPedidoDesc(pedidoResumoId);

            if (resumoPedido.getPedidoFornadaId() == null) {
                throw new EntidadeImprocessavelException("O resumo de pedido #" + pedidoResumoId + " não está vinculado a um pedido de fornada");
            }

            PedidoFornada pedidoFornada = pedidoFornadaGateway.findById(resumoPedido.getPedidoFornadaId())
                    .filter(PedidoFornada::getisAtivo)
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Pedido de fornada com ID " + resumoPedido.getPedidoFornadaId() + " não encontrado."));

            FornadaDaVez fornadaDaVez = fornadaDaVezGateway.findById(pedidoFornada.getFornadaDaVez())
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("FornadaDaVez com ID " + pedidoFornada.getFornadaDaVez() + " não encontrada."));

            String produtoFornada = "Produto não especificado";
            try {
                if (fornadaDaVez.getProdutoFornada() != null) {
                    produtoFornada = produtoFornadaGateway.findById(fornadaDaVez.getProdutoFornada())
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
                    enderecoDTO = EnderecoMapper.toResponseDTO(enderecoGateway.buscarPorId(pedidoFornada.getEndereco()));
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
            var rp = resumoPedidoGateway.findByIdAndIsAtivoTrue(id);
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
        if (!pedidoBoloGateway.existsByIdAndIsAtivoTrue(pedidoBoloId)) {
            throw new EntidadeNaoEncontradaException("Pedido de bolo com ID %d não encontrado".formatted(pedidoBoloId));
        }
    }

    private void validarPedidoFornada(Integer pedidoFornadaId) {
        if (!pedidoFornadaGateway.existsByIdAndIsAtivoTrue(pedidoFornadaId)) {
            throw new EntidadeNaoEncontradaException("Pedido de fornada com ID %d não encontrado".formatted(pedidoFornadaId));
        }
    }

    private Double calcularValorPedidoFornada(Integer pedidoFornadaId) {
        var pedidoFornada = pedidoFornadaGateway.findById(pedidoFornadaId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Pedido fornada não encontrado"));

        var fornadaDaVez = fornadaDaVezGateway.findById(pedidoFornada.getFornadaDaVez())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Fornada da vez não encontrada"));

        var produtoFornada = produtoFornadaGateway.findById(fornadaDaVez.getProdutoFornada())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Produto da fornada não encontrado"));

        Double valorProduto = produtoFornada.getValor();
        return valorProduto * pedidoFornada.getQuantidade();
    }

    private Double calcularValorPedidoBolo(Integer pedidoBoloId) {
        var pedidoBolo = pedidoBoloGateway.findById(pedidoBoloId);

        var bolo = boloGateway.findById(pedidoBolo.getBoloId());

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
            var recheioPedido = recheioPedidoGateway.findById(bolo.getRecheioPedido());

            if (recheioPedido.getRecheioExclusivo() != null) {
                var recheioExclusivo = recheioExclusivoGateway.findById(recheioPedido.getRecheioExclusivo());

                if (recheioExclusivo.getRecheioUnitarioId1() != null) {
                    var recheioUnitario1 = recheioUnitarioGateway.findById(recheioExclusivo.getRecheioUnitarioId1());

                    if (recheioUnitario1 != null && recheioUnitario1.getValor() != null) {
                        valorRecheio += recheioUnitario1.getValor();
                    }
                }

                if (recheioExclusivo.getRecheioUnitarioId2() != null) {
                    var recheioUnitario2 = recheioUnitarioGateway.findById(recheioExclusivo.getRecheioUnitarioId2());

                    if (recheioUnitario2 != null && recheioUnitario2.getValor() != null) {
                        valorRecheio += recheioUnitario2.getValor();
                    }
                }
            } else {
                if (recheioPedido.getRecheioUnitarioId1() != null) {
                    var recheioUnitario1 = recheioUnitarioGateway.findById(recheioPedido.getRecheioUnitarioId1());

                    if (recheioUnitario1 != null && recheioUnitario1.getValor() != null) {
                        valorRecheio += recheioUnitario1.getValor();
                    }
                }

                if (recheioPedido.getRecheioUnitarioId2() != null) {
                    var recheioUnitario2 = recheioUnitarioGateway.findById(recheioPedido.getRecheioUnitarioId2());

                    if (recheioUnitario2 != null && recheioUnitario2.getValor() != null) {
                        valorRecheio += recheioUnitario2.getValor();
                    }
                }
            }
        }

        Double valorMassa = 0.0;
        if (bolo.getMassa() != null) {
            var massa = massaGateway.findById(bolo.getMassa());

            if (massa != null && massa.getValor() != null) {
                valorMassa = massa.getValor();
            }
        }

        Double valorTotal = valorTamanho + valorRecheio + valorMassa;
        return valorTotal;
    }

    private boolean isTransicaoStatusValida(StatusEnum statusAtual, StatusEnum novoStatus) {
        if (statusAtual == novoStatus) {
            return false;
        }

        return true;
    }
}
