package com.carambolos.carambolosapi.infrastructure.web.response;

import com.carambolos.carambolosapi.domain.enums.StatusEnum;
import com.carambolos.carambolosapi.domain.entity.ResumoPedido;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.*;
import com.carambolos.carambolosapi.domain.entity.ProdutoFornada;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.PedidoFornada;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.*;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.FornadasMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public record ResumoPedidoMensagemResponseDTO (
        Integer id,
        StatusEnum status,
        Double valor,
        LocalDateTime dataPedido,
        LocalDateTime dataEntrega,
        Integer pedidoFornadaId,
        Integer pedidoBoloId,
        String mensagem
)
{
    private static MensagemHelper mensagemHelper;

    public static void setMensagemHelper(MensagemHelper helper) {
        mensagemHelper = helper;
    }




    public static ResumoPedidoMensagemResponseDTO toResumoPedidoMensagemResponse(ResumoPedido pedido) {
        if (mensagemHelper == null) {
            throw new IllegalStateException("MensagemHelper não foi inicializado");
        }

        String mensagem = mensagemHelper.gerarMensagemWhatsapp(pedido);

        return new ResumoPedidoMensagemResponseDTO(
                pedido.getId(),
                pedido.getStatus(),
                pedido.getValor(),
                pedido.getDataPedido(),
                pedido.getDataEntrega(),
                pedido.getPedidoFornadaId(),
                pedido.getPedidoBoloId(),
                mensagem
        );
    }

    @Component
    public static class MensagemHelper {
        private final PedidoBoloRepository pedidoBoloRepository;
        private final BoloRepository boloRepository;
        private final MassaRepository massaRepository;
        private final CoberturaRepository coberturaRepository;
        private final RecheioPedidoRepository recheioPedidoRepository;
        private final RecheioExclusivoRepository recheioExclusivoRepository;
        private final RecheioUnitarioRepository recheioUnitarioRepository;
        private final PedidoFornadaRepository pedidoFornadaRepository;
        private final FornadaDaVezRepository fornadaDaVezRepository;
        private final ProdutoFornadaRepository produtoFornadaRepository;
        private final EnderecoRepository enderecoRepository;

        @Autowired
        public MensagemHelper(
                PedidoBoloRepository pedidoBoloRepository,
                BoloRepository boloRepository,
                MassaRepository massaRepository, CoberturaRepository coberturaRepository, RecheioPedidoRepository recheioPedidoRepository, RecheioExclusivoRepository recheioExclusivoRepository, RecheioUnitarioRepository recheioUnitarioRepository, PedidoFornadaRepository pedidoFornadaRepository, FornadaDaVezRepository fornadaDaVezRepository, ProdutoFornadaRepository produtoFornadaRepository, EnderecoRepository enderecoRepository) {
            this.pedidoBoloRepository = pedidoBoloRepository;
            this.boloRepository = boloRepository;
            ResumoPedidoMensagemResponseDTO.setMensagemHelper(this);
            this.massaRepository = massaRepository;
            this.coberturaRepository = coberturaRepository;
            this.recheioPedidoRepository = recheioPedidoRepository;
            this.recheioExclusivoRepository = recheioExclusivoRepository;
            this.recheioUnitarioRepository = recheioUnitarioRepository;
            this.pedidoFornadaRepository = pedidoFornadaRepository;
            this.fornadaDaVezRepository = fornadaDaVezRepository;
            this.produtoFornadaRepository = produtoFornadaRepository;
            this.enderecoRepository = enderecoRepository;
        }

        public String gerarMensagemWhatsapp(ResumoPedido pedido) {
            String linkSite = "https://carambolos.com.br";
            String numeroPedido = "#" + pedido.getId();
            String valorFormatado = String.format("R$ %.2f", pedido.getValor()).replace(".", ",");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            StringBuilder mensagem = new StringBuilder();
            mensagem.append("⭐ GOSTOU DE PEDIR NO NOSSO APP? ⭐\n");
            mensagem.append("Não precisa baixar nada, adicione o nosso restaurante na tela inicial do seu celular e peça com mais agilidade na próxima vez através do link abaixo:\n");
            mensagem.append(linkSite + "\n");
            mensagem.append("Carambolo Doces - Cardápio e Delivery em São Paulo\n");
            mensagem.append("Faça seu pedido online no Carambolo Doces, Sobremesas em São Paulo. Acesse e peça agora pelo nosso website!\n");
            mensagem.append(linkSite + "\n");
            mensagem.append("---------------------------------------\n");
            mensagem.append("Confira o pedido abaixo:\n");
            mensagem.append("Pedido Carambolos Delivery - Carambolo Doces\n");
            mensagem.append("---------------------------------------\n");


            if (pedido.getPedidoBoloId() != null) {
                adicionarDetalhesPedidoBolo(mensagem, pedido.getPedidoBoloId(), valorFormatado);
            } else if (pedido.getPedidoFornadaId() != null) {
                adicionarDetalhesPedidoFornada(mensagem, pedido.getPedidoFornadaId(), valorFormatado);
            }

            mensagem.append("---------------------------------------\n");
            mensagem.append("Link de entrega: ").append(linkSite).append("/entrega\n");
            mensagem.append("Número do pedido: ").append(numeroPedido).append("\n");
            mensagem.append("Pagamento: A decidir");

            return mensagem.toString();
        }

        private void adicionarDetalhesPedidoBolo(StringBuilder mensagem, Integer pedidoBoloId, String valorTotal) {
            PedidoBoloEntity pedidoBoloEntity = pedidoBoloRepository.findById(pedidoBoloId)
                    .orElseThrow(() -> new RuntimeException("Pedido de bolo não encontrado"));

            BoloEntity boloEntity = boloRepository.findById(pedidoBoloEntity.getBoloId())
                    .orElseThrow(() -> new RuntimeException("Bolo não encontrado"));

            String descricaoBolo = gerarDescricaoBolo(boloEntity);
            mensagem.append("1x ").append(descricaoBolo).append(" ").append(valorTotal).append("\n");
            mensagem.append("Total: ").append(valorTotal).append("\n");

            mensagem.append("Cliente: ").append(pedidoBoloEntity.getNomeCliente()).append("\n")
                    .append("Telefone: ").append(pedidoBoloEntity.getTelefoneCliente()).append("\n");

            mensagem.append("Tipo: ")
                    .append(pedidoBoloEntity.getTipoEntrega() != null ? pedidoBoloEntity.getTipoEntrega().name() : "ENTREGA")
                    .append("\n");

            if (pedidoBoloEntity.getDataPrevisaoEntrega() != null) {
                mensagem.append("Previsão de entrega: ")
                        .append(pedidoBoloEntity.getDataPrevisaoEntrega().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                        .append("\n");
            }

            if (pedidoBoloEntity.getObservacao() != null && !pedidoBoloEntity.getObservacao().isEmpty()) {
                mensagem.append("Observações: ").append(pedidoBoloEntity.getObservacao()).append("\n");
            }

            // Adicionar informações do endereço se for entrega
            if (pedidoBoloEntity.getTipoEntrega() != null &&
                    pedidoBoloEntity.getTipoEntrega().name().equals("ENTREGA") &&
                    pedidoBoloEntity.getEnderecoId() != null) {

                EnderecoEntity endereco = enderecoRepository.findByIdAndIsAtivoTrue(pedidoBoloEntity.getEnderecoId());
                if (endereco != null) {
                    mensagem.append("Endereço de entrega:\n");
                    mensagem.append(endereco.getLogradouro());
                    if (endereco.getNumero() != null && !endereco.getNumero().isEmpty()) {
                        mensagem.append(", ").append(endereco.getNumero());
                    }
                    if (endereco.getComplemento() != null && !endereco.getComplemento().isEmpty()) {
                        mensagem.append(", ").append(endereco.getComplemento());
                    }
                    mensagem.append("\n");
                    mensagem.append(endereco.getBairro()).append(" - ").append(endereco.getCidade());
                    mensagem.append(" - ").append(endereco.getEstado());
                    mensagem.append(" - CEP: ").append(endereco.getCep()).append("\n");
                    if (endereco.getReferencia() != null && !endereco.getReferencia().isEmpty()) {
                        mensagem.append("Referência: ").append(endereco.getReferencia()).append("\n");
                    }
                }
            }
        }

        private String gerarDescricaoBolo(BoloEntity boloEntity) {
            StringBuilder descricao = new StringBuilder("Bolo");

            if (boloEntity.getTamanho() != null) {
                descricao.append("\nTamanho: ").append(boloEntity.getTamanho());
            }

            if (boloEntity.getFormato() != null) {
                descricao.append("\nFormato: ").append(boloEntity.getFormato());
            }

            if (boloEntity.getCategoria() != null && !boloEntity.getCategoria().isEmpty()) {
                descricao.append("\nCategoria: ").append(boloEntity.getCategoria());
            }

            if (boloEntity.getCobertura() != null) {
                CoberturaEntity coberturaEntity = coberturaRepository.findById(boloEntity.getCobertura())
                        .orElse(null);
                if (coberturaEntity != null) {
                    descricao.append("\nCobertura: ").append(coberturaEntity.getDescricao());
                }
            }

            if (boloEntity.getMassa() != null) {
                com.carambolos.carambolosapi.infrastructure.persistence.entity.MassaEntity massa = massaRepository.findById(boloEntity.getMassa())
                        .orElse(null);
                if (massa != null) {
                    descricao.append("\nMassa: ").append(massa.getSabor());
                }
            }

            if (boloEntity.getRecheioPedido() != null) {
                RecheioPedidoEntity recheioPedidoEntity = recheioPedidoRepository.findById(boloEntity.getRecheioPedido())
                        .orElse(null);
                if (recheioPedidoEntity != null) {
                    descricao.append("\nRecheio: ");

                    if (recheioPedidoEntity.getRecheioExclusivo() != null) {
                        Optional<RecheioExclusivoEntity> recheioExclusivo = recheioExclusivoRepository.findById(recheioPedidoEntity.getRecheioExclusivo());
                        if (recheioExclusivo.isPresent()) {
                            descricao.append(recheioExclusivo.get().getNome());
                        }
                    } else {
                        if (recheioPedidoEntity.getRecheioUnitarioId1() != null) {
                            RecheioUnitarioEntity recheio1 = recheioUnitarioRepository.findById(recheioPedidoEntity.getRecheioUnitarioId1())
                                    .orElse(null);
                            if (recheio1 != null) {
                                descricao.append(recheio1.getSabor());
                            }
                        }

                        if (recheioPedidoEntity.getRecheioUnitarioId2() != null) {
                            RecheioUnitarioEntity recheio2 = recheioUnitarioRepository.findById(recheioPedidoEntity.getRecheioUnitarioId2())
                                    .orElse(null);
                            if (recheio2 != null) {
                                descricao.append(" e ").append(recheio2.getSabor());
                            }
                        }
                    }
                }
            }
            return descricao.toString();
        }

        private void adicionarDetalhesPedidoFornada(StringBuilder mensagem, Integer pedidoFornadaId, String valorTotal) {
            PedidoFornada pedidoFornada = pedidoFornadaRepository.findById(pedidoFornadaId)
                    .orElseThrow(() -> new RuntimeException("Pedido de fornada não encontrado"));

            Integer fornadaDaVezId = pedidoFornada.getFornadaDaVez();
            FornadaDaVez fornadaDaVez = fornadaDaVezRepository.findById(fornadaDaVezId)
                    .orElse(null);

            String descricaoFornada = gerarDescricaoFornada(fornadaDaVez);

            mensagem.append(pedidoFornada.getQuantidade())
                    .append("x ")
                    .append(descricaoFornada)
                    .append(" ")
                    .append(valorTotal)
                    .append("\n")
                    .append("Total: ")
                    .append(valorTotal)
                    .append("\n");

            mensagem.append("Cliente: ")
                    .append(pedidoFornada.getNomeCliente())
                    .append("\n")
                    .append("Telefone: ")
                    .append(pedidoFornada.getTelefoneCliente())
                    .append("\n");

            mensagem.append("Tipo: ")
                    .append(pedidoFornada.getTipoEntrega() != null ? pedidoFornada.getTipoEntrega().name() : "ENTREGA")
                    .append("\n");

            if (pedidoFornada.getDataPrevisaoEntrega() != null) {
                mensagem.append("Previsão de entrega: ")
                        .append(pedidoFornada.getDataPrevisaoEntrega().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                        .append("\n");
            }

            if (pedidoFornada.getHorario() != null && !pedidoFornada.getHorario().isEmpty()) {
                mensagem.append("Horário: ")
                        .append(pedidoFornada.getHorario())
                        .append("\n");
            }

            if (pedidoFornada.getObservacoes() != null && !pedidoFornada.getObservacoes().isEmpty()) {
                mensagem.append("Observações: ")
                        .append(pedidoFornada.getObservacoes())
                        .append("\n");
            }

            // Adicionar informações do endereço se for entrega
            if (pedidoFornada.getTipoEntrega() != null &&
                    pedidoFornada.getTipoEntrega().name().equals("ENTREGA") &&
                    pedidoFornada.getEndereco() != null) {

                EnderecoEntity endereco = enderecoRepository.findByIdAndIsAtivoTrue(pedidoFornada.getEndereco());
                if (endereco != null) {
                    mensagem.append("Endereço de entrega:\n");
                    mensagem.append(endereco.getLogradouro());
                    if (endereco.getNumero() != null && !endereco.getNumero().isEmpty()) {
                        mensagem.append(", ").append(endereco.getNumero());
                    }
                    if (endereco.getComplemento() != null && !endereco.getComplemento().isEmpty()) {
                        mensagem.append(", ").append(endereco.getComplemento());
                    }
                    mensagem.append("\n");
                    mensagem.append(endereco.getBairro()).append(" - ").append(endereco.getCidade());
                    mensagem.append(" - ").append(endereco.getEstado());
                    mensagem.append(" - CEP: ").append(endereco.getCep()).append("\n");
                    if (endereco.getReferencia() != null && !endereco.getReferencia().isEmpty()) {
                        mensagem.append("Referência: ").append(endereco.getReferencia()).append("\n");
                    }
                }
            }
        }

        private String gerarDescricaoFornada(FornadaDaVez fornadaDaVez) {
            if (fornadaDaVez == null || fornadaDaVez.getFornada() == null) return "Fornada Especial";

            Integer produtoFornadaId = fornadaDaVez.getProdutoFornada();
            var produtoEntity = produtoFornadaRepository.findById(produtoFornadaId).orElse(null);
            ProdutoFornada produto = produtoEntity != null ? FornadasMapper.toDomain(produtoEntity) : null;

            if (produto != null && produto.getProduto() != null) {
                return "Produto da Fornada: " + produto.getProduto() + " - " + produto.getDescricao();
            } else {
                return "Fornada Especial";
            }
        }
    }
}