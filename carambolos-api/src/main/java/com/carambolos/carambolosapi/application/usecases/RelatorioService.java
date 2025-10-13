package com.carambolos.carambolosapi.application.usecases;

import com.carambolos.carambolosapi.domain.entity.*;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.*;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.PedidoFornada;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.*;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RelatorioService {

    private final PedidoBoloRepository pedidoBoloRepository;
    private final UsuarioRepository usuarioRepository;
    private final BoloRepository boloRepository;
    private final PedidoFornadaRepository pedidoFornadaRepository;
    private final MassaRepository massaRepository;
    private final RecheioPedidoRepository recheioPedidoRepository;
    private final RecheioUnitarioRepository recheioUnitarioRepository;
    private final ProdutoFornadaRepository produtoFornadaRepository;
    private final FornadaDaVezRepository fornadaDaVezRepository;

    public RelatorioService(PedidoBoloRepository pedidoBoloRepository,
                            UsuarioRepository usuarioRepository,
                            BoloRepository boloRepository,
                            PedidoFornadaRepository pedidoFornadaRepository,
                            MassaRepository massaRepository,
                            RecheioPedidoRepository recheioPedidoRepository,
                            RecheioUnitarioRepository recheioUnitarioRepository,
                            ProdutoFornadaRepository produtoFornadaRepository,
                            FornadaDaVezRepository fornadaDaVezRepository) {
        this.pedidoBoloRepository = pedidoBoloRepository;
        this.usuarioRepository = usuarioRepository;
        this.boloRepository = boloRepository;
        this.pedidoFornadaRepository = pedidoFornadaRepository;
        this.massaRepository = massaRepository;
        this.recheioPedidoRepository = recheioPedidoRepository;
        this.recheioUnitarioRepository = recheioUnitarioRepository;
        this.produtoFornadaRepository = produtoFornadaRepository;
        this.fornadaDaVezRepository = fornadaDaVezRepository;
    }

    public byte[] gerarRelatorioInsights() {
        List<PedidoBolo> pedidosBolo = pedidoBoloRepository.findAll();
        List<PedidoFornada> pedidosFornada = pedidoFornadaRepository.findAll();
        List<UsuarioEntity> usuarioEntities = usuarioRepository.findAll();
        List<Bolo> bolos = boloRepository.findAll();
        List<MassaEntity> massas = massaRepository.findAll();
        List<RecheioUnitarioEntity> recheios = recheioUnitarioRepository.findAll();

        Map<Integer, Long> contagemBolos = pedidosBolo.stream()
                .collect(Collectors.groupingBy(PedidoBolo::getBoloId, Collectors.counting()));

        List<Map.Entry<Integer, Long>> top3Bolos = contagemBolos.entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                .limit(3)
                .toList();

        Map<Integer, Long> contagemFornadas = pedidosFornada.stream()
                .map(p -> fornadaDaVezRepository.findById(p.getFornadaDaVez())
                        .map(FornadaDaVez::getProdutoFornada)
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(id -> id, Collectors.counting()));

        List<Map.Entry<Integer, Long>> top3Fornadas = contagemFornadas.entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                .limit(3)
                .toList();

        Map<String, Long> combinacoesBolo = pedidosBolo.stream()
                .map(p -> {
                    Optional<Bolo> boloOpt = bolos.stream().filter(b -> b.getId().equals(p.getBoloId())).findFirst();
                    if (boloOpt.isEmpty()) return "Desconhecido";
                    Bolo bolo = boloOpt.get();

                    String saborMassa = massas.stream()
                            .filter(m -> m.getId().equals(bolo.getMassa()))
                            .map(MassaEntity::getSabor)
                            .findFirst()
                            .orElse("Massa desconhecida");

                    String saborRecheio = "";
                    if (bolo.getRecheioPedido() != null) {
                        Optional<RecheioPedido> recheioPedidoOpt = recheioPedidoRepository.findById(bolo.getRecheioPedido());
                        if (recheioPedidoOpt.isPresent()) {
                            RecheioPedido rp = recheioPedidoOpt.get();
                            List<String> sabores = new ArrayList<>();
                            if (rp.getRecheioUnitarioId1() != null) {
                                recheios.stream()
                                        .filter(r -> r.getId().equals(rp.getRecheioUnitarioId1()))
                                        .map(RecheioUnitarioEntity::getSabor)
                                        .findFirst()
                                        .ifPresent(sabores::add);
                            }
                            if (rp.getRecheioUnitarioId2() != null) {
                                recheios.stream()
                                        .filter(r -> r.getId().equals(rp.getRecheioUnitarioId2()))
                                        .map(RecheioUnitarioEntity::getSabor)
                                        .findFirst()
                                        .ifPresent(sabores::add);
                            }
                            saborRecheio = String.join(" + ", sabores);
                        } else {
                            saborRecheio = "Recheio desconhecido";
                        }
                    } else {
                        saborRecheio = "Recheio desconhecido";
                    }

                    return bolo.getFormato() + " - " + bolo.getTamanho() + " - " + saborMassa + " - " + saborRecheio;
                })
                .collect(Collectors.groupingBy(c -> c, Collectors.counting()));

        Optional<Map.Entry<String, Long>> combinacaoMaisPedida = combinacoesBolo.entrySet().stream()
                .max(Map.Entry.comparingByValue());

        Map<Integer, Long> contagemMassas = pedidosBolo.stream()
                .map(p -> bolos.stream()
                        .filter(bolo -> bolo.getId().equals(p.getBoloId()))
                        .findFirst()
                        .map(Bolo::getMassa)
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(id -> id, Collectors.counting()));

        List<Map.Entry<Integer, Long>> top3Massas = contagemMassas.entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                .limit(3)
                .toList();

        Map<Integer, Long> contagemRecheios = pedidosBolo.stream()
                .map(p -> bolos.stream()
                        .filter(bolo -> bolo.getId().equals(p.getBoloId()))
                        .findFirst()
                        .map(Bolo::getRecheioPedido)
                        .orElse(null))
                .filter(Objects::nonNull)
                .map(recheioPedidoRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .flatMap(r -> Stream.of(r.getRecheioUnitarioId1(), r.getRecheioUnitarioId2()))
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(id -> id, Collectors.counting()));

        List<Map.Entry<Integer, Long>> top3Recheios = contagemRecheios.entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                .limit(3)
                .toList();

        String recheioMaisPedido = top3Recheios.isEmpty()
                ? "Desconhecido"
                : recheios.stream()
                .filter(r -> r.getId().equals(top3Recheios.getFirst().getKey()))
                .map(RecheioUnitarioEntity::getSabor)
                .findFirst()
                .orElse("Recheio ID " + top3Recheios.getFirst().getKey());

        List<Map.Entry<Integer, Long>> top3UsuariosBolo = pedidosBolo.stream()
                .collect(Collectors.groupingBy(PedidoBolo::getUsuarioId, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                .limit(3)
                .toList();

        List<Map.Entry<Integer, Long>> top3UsuariosFornada = pedidosFornada.stream()
                .filter(p -> p.getUsuario() != null)
                .collect(Collectors.groupingBy(PedidoFornada::getUsuario, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                .limit(3)
                .toList();

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document doc = new Document();
            PdfWriter.getInstance(doc, out);
            doc.open();

            doc.addTitle("Relatório de Insights");
            doc.add(new Paragraph("📋 Relatório de Insights de Pedidos"));
            doc.add(new Paragraph(" "));

            doc.add(new Paragraph("🥧 Top 3 Bolos mais pedidos:"));
            if (!top3Bolos.isEmpty()) {
                int i = 1;
                for (var entry : top3Bolos) {
                    Integer boloId = entry.getKey();
                    Long count = entry.getValue();
                    Optional<Bolo> bolo = bolos.stream().filter(b -> b.getId().equals(boloId)).findFirst();
                    String boloNome = bolo.map(b -> b.getFormato() + " - " + b.getTamanho())
                            .orElse("Bolo ID " + boloId);
                    doc.add(new Paragraph(i + ". " + boloNome + " (" + count + " pedidos)"));
                    i++;
                }
            } else {
                doc.add(new Paragraph("- Nenhum pedido de bolo encontrado."));
            }
            doc.add(new Paragraph(" "));

            doc.add(new Paragraph("🍞 Top 3 Produtos de Fornada mais pedidos:"));
            if (!top3Fornadas.isEmpty()) {
                int i = 1;
                for (var entry : top3Fornadas) {
                    Integer produtoId = entry.getKey();
                    Long count = entry.getValue();

                    String produtoNome = produtoFornadaRepository.findById(produtoId)
                            .map(ProdutoFornada::getProduto)
                            .orElse("Produto ID " + produtoId);

                    doc.add(new Paragraph(i + ". " + produtoNome + " (" + count + " pedidos)"));
                    i++;
                }
            } else {
                doc.add(new Paragraph("- Nenhum pedido de fornada encontrado."));
            }
            doc.add(new Paragraph(" "));

            doc.add(new Paragraph("🔗 Combinação de bolo mais pedida (Formato + Tamanho + Massa + Recheio):"));
            if (combinacaoMaisPedida.isPresent()) {
                var entry = combinacaoMaisPedida.get();
                doc.add(new Paragraph("- " + entry.getKey() + " (" + entry.getValue() + " pedidos)"));
            } else {
                doc.add(new Paragraph("- Nenhuma combinação encontrada."));
            }
            doc.add(new Paragraph(" "));

            doc.add(new Paragraph("🍰 Top 3 Massas mais pedidas:"));
            if (!top3Massas.isEmpty()) {
                int i = 1;
                for (var entry : top3Massas) {
                    Integer massaId = entry.getKey();
                    Long count = entry.getValue();
                    String nome = massas.stream()
                            .filter(m -> m.getId().equals(massaId))
                            .map(MassaEntity::getSabor)
                            .findFirst()
                            .orElse("Massa ID " + massaId);
                    doc.add(new Paragraph(i + ". " + nome + " (" + count + " pedidos)"));
                    i++;
                }
            } else {
                doc.add(new Paragraph("- Nenhuma massa encontrada."));
            }
            doc.add(new Paragraph(" "));

            doc.add(new Paragraph("🍫 Top 3 Recheios mais pedidos:"));
            if (!top3Recheios.isEmpty()) {
                int i = 1;
                for (var entry : top3Recheios) {
                    Integer recheioId = entry.getKey();
                    Long count = entry.getValue();
                    String nome = recheios.stream()
                            .filter(r -> r.getId().equals(recheioId))
                            .map(RecheioUnitarioEntity::getSabor)
                            .findFirst()
                            .orElse("Recheio ID " + recheioId);
                    doc.add(new Paragraph(i + ". " + nome + " (" + count + " vezes)"));
                    i++;
                }
            } else {
                doc.add(new Paragraph("- Nenhum recheio encontrado."));
            }
            doc.add(new Paragraph(" "));

            doc.add(new Paragraph("👑 Top 3 usuários em pedidos de bolo:"));
            if (!top3UsuariosBolo.isEmpty()) {
                int i = 1;
                for (var entry : top3UsuariosBolo) {
                    Integer usuarioId = entry.getKey();
                    Long count = entry.getValue();
                    String nome = usuarioEntities.stream()
                            .filter(u -> u.getId().equals(usuarioId))
                            .map(UsuarioEntity::getNome)
                            .findFirst()
                            .orElse("Usuário ID " + usuarioId);
                    doc.add(new Paragraph(i + ". " + nome + " - " + count + " pedidos"));
                    i++;
                }
            } else {
                doc.add(new Paragraph("- Nenhum pedido de bolo encontrado."));
            }
            doc.add(new Paragraph(" "));

            doc.add(new Paragraph("👑 Top 3 usuários em pedidos de fornada:"));
            if (!top3UsuariosFornada.isEmpty()) {
                int i = 1;
                for (var entry : top3UsuariosFornada) {
                    Integer usuarioId = entry.getKey();
                    Long count = entry.getValue();
                    String nome = usuarioEntities.stream()
                            .filter(u -> u.getId().equals(usuarioId))
                            .map(UsuarioEntity::getNome)
                            .findFirst()
                            .orElse("Usuário ID " + usuarioId);
                    doc.add(new Paragraph(i + ". " + nome + " - " + count + " pedidos"));
                    i++;
                }
            } else {
                doc.add(new Paragraph("- Nenhum pedido de fornada encontrado."));
            }

            doc.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar relatório", e);
        }
    }
}
