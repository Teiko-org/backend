package com.carambolos.carambolosapi.controller.response;

import com.carambolos.carambolosapi.model.*;
import com.carambolos.carambolosapi.model.enums.StatusEnum;
import com.carambolos.carambolosapi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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

        @Autowired
        public MensagemHelper(
                PedidoBoloRepository pedidoBoloRepository,
                BoloRepository boloRepository,
                MassaRepository massaRepository, CoberturaRepository coberturaRepository, RecheioPedidoRepository recheioPedidoRepository, RecheioExclusivoRepository recheioExclusivoRepository, RecheioUnitarioRepository recheioUnitarioRepository) {
            this.pedidoBoloRepository = pedidoBoloRepository;
            this.boloRepository = boloRepository;
            ResumoPedidoMensagemResponseDTO.setMensagemHelper(this);
            this.massaRepository = massaRepository;
            this.coberturaRepository = coberturaRepository;
            this.recheioPedidoRepository = recheioPedidoRepository;
            this.recheioExclusivoRepository = recheioExclusivoRepository;
            this.recheioUnitarioRepository = recheioUnitarioRepository;
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

            // Adiciona detalhes do pedido com base no tipo (Bolo ou Fornada)
            if (pedido.getPedidoBoloId() != null) {
                adicionarDetalhesPedidoBolo(mensagem, pedido.getPedidoBoloId(), valorFormatado);
            } else if (pedido.getPedidoFornadaId() != null) {
//                adicionarDetalhesPedidoFornada(mensagem, pedido.getPedidoFornadaId(), valorFormatado);
            }

            mensagem.append("---------------------------------------\n");
            mensagem.append("Link de entrega: ").append(linkSite).append("/entrega\n");
            mensagem.append("Número do pedido: ").append(numeroPedido).append("\n");
            mensagem.append("Pagamento: A decidir");

            return mensagem.toString();
        }

        private void adicionarDetalhesPedidoBolo(StringBuilder mensagem, Integer pedidoBoloId, String valorTotal) {
            PedidoBolo pedidoBolo = pedidoBoloRepository.findById(pedidoBoloId)
                    .orElseThrow(() -> new RuntimeException("Pedido de bolo não encontrado"));

            Bolo bolo = boloRepository.findById(pedidoBolo.getBoloId())
                    .orElseThrow(() -> new RuntimeException("Bolo não encontrado"));

            String descricaoBolo = gerarDescricaoBolo(bolo);
            mensagem.append("1x ").append(descricaoBolo).append(" ").append(valorTotal).append("\n");
            mensagem.append("Total: ").append(valorTotal).append("\n");

            if (pedidoBolo.getDataPrevisaoEntrega() != null) {
                mensagem.append("Previsão de entrega: ")
                        .append(pedidoBolo.getDataPrevisaoEntrega().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                        .append("\n");
            }

            if (pedidoBolo.getObservacao() != null && !pedidoBolo.getObservacao().isEmpty()) {
                mensagem.append("Observações: ").append(pedidoBolo.getObservacao()).append("\n");
            }
        }

        private String gerarDescricaoBolo(Bolo bolo) {
            StringBuilder descricao = new StringBuilder("Bolo");

            if (bolo.getTamanho() != null) {
                descricao.append("\nTamanho: ").append(bolo.getTamanho());
            }

            if (bolo.getFormato() != null) {
                descricao.append("\nFormato: ").append(bolo.getFormato());
            }

            if (bolo.getCategoria() != null && !bolo.getCategoria().isEmpty()) {
                descricao.append("\nCategoria: ").append(bolo.getCategoria());
            }

            if (bolo.getCobertura() != null) {
                Cobertura cobertura = coberturaRepository.findById(bolo.getCobertura())
                                .orElse(null);
                if (cobertura != null) {
                    descricao.append("\nCobertura: ").append(cobertura.getDescricao());
                }
            }

            if (bolo.getMassa() != null) {
                Massa massa = massaRepository.findById(bolo.getMassa())
                                .orElse(null);
                if (massa != null) {
                    descricao.append("\nMassa: ").append(massa.getSabor());
                }
            }

            if (bolo.getRecheioPedido() != null) {
                RecheioPedido recheioPedido = recheioPedidoRepository.findById(bolo.getRecheioPedido())
                                .orElse(null);
                if (recheioPedido != null) {
                    descricao.append("\nRecheio: ");

                    if (recheioPedido.getRecheioExclusivo() != null) {
                        RecheioExclusivo recheioExclusivo = recheioExclusivoRepository.findById(recheioPedido.getRecheioExclusivo())
                                .orElse(null);
                        if (recheioExclusivo != null) {
                            descricao.append(recheioExclusivo.getNome());
                        }
                    } else {
                        if (recheioPedido.getRecheioUnitarioId1() != null) {
                            RecheioUnitario recheio1 = recheioUnitarioRepository.findById(recheioPedido.getRecheioUnitarioId1())
                                    .orElse(null);
                            if (recheio1 != null) {
                                descricao.append(recheio1.getSabor());
                            }
                        }

                        if (recheioPedido.getRecheioUnitarioId2() != null) {
                            RecheioUnitario recheio2 = recheioUnitarioRepository.findById(recheioPedido.getRecheioUnitarioId2())
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
    }
}