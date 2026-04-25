package com.carambolos.carambolosapi.application.usecases;

import com.carambolos.carambolosapi.domain.entity.*;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.*;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.PedidoFornada;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.ProdutoFornada;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.*;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RelatorioService {

    private static final Color COLOR_PRIMARY = new Color(16, 52, 100);
    private static final Color COLOR_PRIMARY_DARK = new Color(48, 52, 79);
    private static final Color COLOR_GOLD = new Color(164, 112, 50);
    private static final Color COLOR_GOLD_LIGHT = new Color(212, 176, 118);
    private static final Color COLOR_CREAM = new Color(255, 238, 231);
    private static final Color COLOR_TEXT = new Color(40, 40, 50);
    private static final Color COLOR_MUTED = new Color(120, 120, 130);
    private static final Color COLOR_ZEBRA = new Color(250, 246, 240);
    private static final Color COLOR_BORDER = new Color(220, 210, 195);

    private static final Font FONT_TITLE = new Font(Font.HELVETICA, 24, Font.BOLD, Color.WHITE);
    private static final Font FONT_SUBTITLE = new Font(Font.HELVETICA, 11, Font.NORMAL, COLOR_CREAM);
    private static final Font FONT_SECTION = new Font(Font.HELVETICA, 14, Font.BOLD, COLOR_PRIMARY);
    private static final Font FONT_SECTION_NUM = new Font(Font.HELVETICA, 10, Font.BOLD, Color.WHITE);
    private static final Font FONT_KPI_VALUE = new Font(Font.HELVETICA, 22, Font.BOLD, COLOR_PRIMARY);
    private static final Font FONT_KPI_LABEL = new Font(Font.HELVETICA, 9, Font.NORMAL, COLOR_MUTED);
    private static final Font FONT_TABLE_HEADER = new Font(Font.HELVETICA, 10, Font.BOLD, Color.WHITE);
    private static final Font FONT_TABLE_CELL = new Font(Font.HELVETICA, 10, Font.NORMAL, COLOR_TEXT);
    private static final Font FONT_TABLE_RANK = new Font(Font.HELVETICA, 10, Font.BOLD, COLOR_GOLD);
    private static final Font FONT_HIGHLIGHT_TITLE = new Font(Font.HELVETICA, 11, Font.BOLD, COLOR_PRIMARY);
    private static final Font FONT_HIGHLIGHT_VALUE = new Font(Font.HELVETICA, 13, Font.BOLD, COLOR_GOLD);
    private static final Font FONT_EMPTY = new Font(Font.HELVETICA, 10, Font.ITALIC, COLOR_MUTED);

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
        ReportData data = collectData();

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document doc = new Document(PageSize.A4, 36, 36, 110, 60);
            PdfWriter writer = PdfWriter.getInstance(doc, out);
            writer.setPageEvent(new HeaderFooterEvent());

            doc.addTitle("Relatório de Insights Carambolos");
            doc.addAuthor("Carambolos");
            doc.addSubject("Análise de pedidos e produtos");

            doc.open();

            addExecutiveSummary(doc, data);
            addKpiCards(doc, data);
            addCombinacaoDestaque(doc, data);

            addSectionTitle(doc, "1", "Bolos mais pedidos");
            addRankingTable(doc, data.top3Bolos, data.boloEntities,
                    (id, ent) -> ent.stream().filter(b -> b.getId().equals(id)).findFirst()
                            .map(b -> humanize(String.valueOf(b.getFormato())) + " — " + humanize(String.valueOf(b.getTamanho())))
                            .orElse("Bolo #" + id),
                    "pedidos");

            addSectionTitle(doc, "2", "Produtos de fornada mais pedidos");
            addRankingTableSimple(doc, data.top3Fornadas,
                    id -> produtoFornadaRepository.findById(id)
                            .map(ProdutoFornada::getProduto)
                            .map(RelatorioService::humanize)
                            .orElse("Produto #" + id),
                    "pedidos");

            addSectionTitle(doc, "3", "Massas mais pedidas");
            addRankingTableSimple(doc, data.top3Massas,
                    id -> data.massas.stream()
                            .filter(m -> m.getId().equals(id))
                            .map(MassaEntity::getSabor)
                            .map(RelatorioService::humanize)
                            .findFirst()
                            .orElse("Massa #" + id),
                    "pedidos");

            addSectionTitle(doc, "4", "Recheios mais utilizados");
            addRankingTableSimple(doc, data.top3Recheios,
                    id -> data.recheios.stream()
                            .filter(r -> r.getId().equals(id))
                            .map(RecheioUnitarioEntity::getSabor)
                            .map(RelatorioService::humanize)
                            .findFirst()
                            .orElse("Recheio #" + id),
                    "ocorrências");

            addSectionTitle(doc, "5", "Top clientes — pedidos de bolo");
            addClientesTable(doc, data.top3UsuariosBolo, data.usuarioEntities);

            addSectionTitle(doc, "6", "Top clientes — pedidos de fornada");
            addClientesTable(doc, data.top3UsuariosFornada, data.usuarioEntities);

            doc.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar relatório", e);
        }
    }

    private ReportData collectData() {
        ReportData d = new ReportData();
        d.pedidosBolo = pedidoBoloRepository.findAll();
        d.pedidosFornada = pedidoFornadaRepository.findAll();
        d.usuarioEntities = usuarioRepository.findAll();
        d.boloEntities = boloRepository.findAll();
        d.massas = massaRepository.findAll();
        d.recheios = recheioUnitarioRepository.findAll();

        d.adminIds = d.usuarioEntities.stream()
                .filter(u -> Boolean.TRUE.equals(u.getSysAdmin()))
                .map(UsuarioEntity::getId)
                .collect(Collectors.toSet());

        d.top3Bolos = topN(d.pedidosBolo.stream()
                .filter(p -> p.getBoloId() != null)
                .collect(Collectors.groupingBy(PedidoBoloEntity::getBoloId, Collectors.counting())), 3);

        d.top3Fornadas = topN(d.pedidosFornada.stream()
                .map(p -> fornadaDaVezRepository.findById(p.getFornadaDaVez())
                        .map(FornadaDaVez::getProdutoFornada)
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(id -> id, Collectors.counting())), 3);

        d.top3Massas = topN(d.pedidosBolo.stream()
                .map(p -> d.boloEntities.stream()
                        .filter(b -> b.getId().equals(p.getBoloId()))
                        .findFirst()
                        .map(BoloEntity::getMassa)
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(id -> id, Collectors.counting())), 3);

        d.top3Recheios = topN(d.pedidosBolo.stream()
                .map(p -> d.boloEntities.stream()
                        .filter(b -> b.getId().equals(p.getBoloId()))
                        .findFirst()
                        .map(BoloEntity::getRecheioPedido)
                        .orElse(null))
                .filter(Objects::nonNull)
                .map(recheioPedidoRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .flatMap(r -> Stream.of(r.getRecheioUnitarioId1(), r.getRecheioUnitarioId2()))
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(id -> id, Collectors.counting())), 3);

        d.top3UsuariosBolo = topN(d.pedidosBolo.stream()
                .filter(p -> p.getUsuarioId() != null)
                .filter(p -> !d.adminIds.contains(p.getUsuarioId()))
                .collect(Collectors.groupingBy(PedidoBoloEntity::getUsuarioId, Collectors.counting())), 3);

        d.top3UsuariosFornada = topN(d.pedidosFornada.stream()
                .filter(p -> p.getUsuario() != null)
                .filter(p -> !d.adminIds.contains(p.getUsuario()))
                .collect(Collectors.groupingBy(PedidoFornada::getUsuario, Collectors.counting())), 3);

        d.combinacaoMaisPedida = d.pedidosBolo.stream()
                .map(p -> buildCombinacao(p, d))
                .collect(Collectors.groupingBy(c -> c, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue());

        return d;
    }

    private String buildCombinacao(PedidoBoloEntity p, ReportData d) {
        Optional<BoloEntity> boloOpt = d.boloEntities.stream()
                .filter(b -> b.getId().equals(p.getBoloId())).findFirst();
        if (boloOpt.isEmpty()) return "Desconhecido";
        BoloEntity bolo = boloOpt.get();

        String massa = d.massas.stream()
                .filter(m -> m.getId().equals(bolo.getMassa()))
                .map(MassaEntity::getSabor)
                .map(RelatorioService::humanize)
                .findFirst()
                .orElse("Massa desconhecida");

        String recheio = "Recheio desconhecido";
        if (bolo.getRecheioPedido() != null) {
            Optional<RecheioPedidoEntity> rpOpt = recheioPedidoRepository.findById(bolo.getRecheioPedido());
            if (rpOpt.isPresent()) {
                RecheioPedidoEntity rp = rpOpt.get();
                List<String> sabores = new ArrayList<>();
                if (rp.getRecheioUnitarioId1() != null) {
                    d.recheios.stream()
                            .filter(r -> r.getId().equals(rp.getRecheioUnitarioId1()))
                            .map(RecheioUnitarioEntity::getSabor)
                            .map(RelatorioService::humanize)
                            .findFirst()
                            .ifPresent(sabores::add);
                }
                if (rp.getRecheioUnitarioId2() != null) {
                    d.recheios.stream()
                            .filter(r -> r.getId().equals(rp.getRecheioUnitarioId2()))
                            .map(RecheioUnitarioEntity::getSabor)
                            .map(RelatorioService::humanize)
                            .findFirst()
                            .ifPresent(sabores::add);
                }
                if (!sabores.isEmpty()) recheio = String.join(" + ", sabores);
            }
        }

        return humanize(String.valueOf(bolo.getFormato()))
                + " · " + humanize(String.valueOf(bolo.getTamanho()))
                + " · " + massa
                + " · " + recheio;
    }

    private static <K> List<Map.Entry<K, Long>> topN(Map<K, Long> m, int n) {
        return m.entrySet().stream()
                .sorted(Map.Entry.<K, Long>comparingByValue().reversed())
                .limit(n)
                .toList();
    }

    /**
     * Limpa identificadores em snake_case/UPPER_CASE vindos do banco
     * (ex.: "TAMANHO_5" -> "Tamanho 5", "devil_s_cake_ganache" -> "Devil's Cake Ganache").
     */
    private static String humanize(String raw) {
        if (raw == null) return "";
        String s = raw.trim();
        if (s.isEmpty()) return "";

        s = s.toLowerCase(Locale.ROOT)
                .replace("_s_", "'s ")
                .replace("_", " ")
                .replaceAll("\\s+", " ")
                .trim();

        StringBuilder sb = new StringBuilder(s.length());
        boolean capitalizeNext = true;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isWhitespace(c)) {
                sb.append(c);
                capitalizeNext = true;
            } else if (capitalizeNext) {
                sb.append(Character.toUpperCase(c));
                capitalizeNext = false;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private void addExecutiveSummary(Document doc, ReportData d) throws DocumentException {
        Paragraph p = new Paragraph();
        p.setSpacingAfter(14f);
        Chunk intro = new Chunk(
                "Visão consolidada de pedidos, produtos, ingredientes e clientes da Carambolos. " +
                "Use estes indicadores para identificar oportunidades de produção e fidelização.",
                new Font(Font.HELVETICA, 10, Font.NORMAL, COLOR_MUTED)
        );
        p.add(intro);
        doc.add(p);
    }

    private void addKpiCards(Document doc, ReportData d) throws DocumentException {
        long totalBolos = d.pedidosBolo.size();
        long totalFornadas = d.pedidosFornada.size();
        long totalClientesBolo = d.pedidosBolo.stream()
                .map(PedidoBoloEntity::getUsuarioId)
                .filter(Objects::nonNull)
                .filter(uid -> !d.adminIds.contains(uid))
                .distinct().count();
        long totalProdutosBolo = d.boloEntities.size();

        PdfPTable t = new PdfPTable(4);
        t.setWidthPercentage(100f);
        try { t.setWidths(new float[]{1, 1, 1, 1}); } catch (DocumentException ignored) {}
        t.setSpacingAfter(18f);

        t.addCell(buildKpiCell(String.valueOf(totalBolos), "Pedidos de bolo"));
        t.addCell(buildKpiCell(String.valueOf(totalFornadas), "Pedidos de fornada"));
        t.addCell(buildKpiCell(String.valueOf(totalClientesBolo), "Clientes únicos (bolo)"));
        t.addCell(buildKpiCell(String.valueOf(totalProdutosBolo), "Bolos cadastrados"));

        doc.add(t);
    }

    private PdfPCell buildKpiCell(String value, String label) {
        PdfPCell cell = new PdfPCell();
        cell.setBorderColor(COLOR_BORDER);
        cell.setBorderWidth(1f);
        cell.setBackgroundColor(Color.WHITE);
        cell.setPadding(12f);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);

        Paragraph v = new Paragraph(value, FONT_KPI_VALUE);
        v.setAlignment(Element.ALIGN_CENTER);
        v.setSpacingAfter(2f);

        Paragraph l = new Paragraph(label.toUpperCase(Locale.ROOT), FONT_KPI_LABEL);
        l.setAlignment(Element.ALIGN_CENTER);

        cell.addElement(v);
        cell.addElement(l);
        return cell;
    }

    private void addCombinacaoDestaque(Document doc, ReportData d) throws DocumentException {
        if (d.combinacaoMaisPedida.isEmpty()) return;

        PdfPTable t = new PdfPTable(1);
        t.setWidthPercentage(100f);
        t.setSpacingAfter(20f);

        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(COLOR_CREAM);
        cell.setBorderColor(COLOR_GOLD_LIGHT);
        cell.setBorderWidth(1f);
        cell.setPadding(14f);

        Paragraph title = new Paragraph("Combinação mais pedida", FONT_HIGHLIGHT_TITLE);
        title.setSpacingAfter(4f);

        var entry = d.combinacaoMaisPedida.get();
        Paragraph value = new Paragraph(entry.getKey(), FONT_HIGHLIGHT_VALUE);
        value.setSpacingAfter(2f);

        Paragraph count = new Paragraph(
                entry.getValue() + " pedido" + (entry.getValue() == 1 ? "" : "s") + " desta combinação",
                new Font(Font.HELVETICA, 9, Font.NORMAL, COLOR_MUTED)
        );

        cell.addElement(title);
        cell.addElement(value);
        cell.addElement(count);
        t.addCell(cell);
        doc.add(t);
    }

    private void addSectionTitle(Document doc, String num, String title) throws DocumentException {
        PdfPTable wrap = new PdfPTable(2);
        wrap.setWidthPercentage(100f);
        try { wrap.setWidths(new float[]{0.06f, 0.94f}); } catch (DocumentException ignored) {}
        wrap.setSpacingBefore(6f);
        wrap.setSpacingAfter(8f);

        PdfPCell numCell = new PdfPCell(new Phrase(num, FONT_SECTION_NUM));
        numCell.setBackgroundColor(COLOR_PRIMARY);
        numCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        numCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        numCell.setPadding(7f);
        numCell.setBorder(Rectangle.NO_BORDER);
        wrap.addCell(numCell);

        PdfPCell titleCell = new PdfPCell(new Phrase(title, FONT_SECTION));
        titleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        titleCell.setPaddingLeft(10f);
        titleCell.setPaddingTop(6f);
        titleCell.setPaddingBottom(6f);
        titleCell.setBorder(Rectangle.NO_BORDER);
        titleCell.setBorderWidthBottom(2f);
        titleCell.setBorderColorBottom(COLOR_GOLD_LIGHT);
        wrap.addCell(titleCell);

        doc.add(wrap);
    }

    private interface NameResolver<T> {
        String resolve(Integer id, List<T> source);
    }

    private interface SimpleNameResolver {
        String resolve(Integer id);
    }

    private <T> void addRankingTable(Document doc, List<Map.Entry<Integer, Long>> top,
                                     List<T> source, NameResolver<T> resolver, String unit) throws DocumentException {
        if (top.isEmpty()) {
            addEmptyState(doc, "Nenhum registro encontrado.");
            return;
        }
        PdfPTable t = buildRankingTableHeader();
        int i = 1;
        for (var entry : top) {
            String name = resolver.resolve(entry.getKey(), source);
            addRankingRow(t, i, name, entry.getValue(), unit, i % 2 == 0);
            i++;
        }
        doc.add(t);
    }

    private void addRankingTableSimple(Document doc, List<Map.Entry<Integer, Long>> top,
                                       SimpleNameResolver resolver, String unit) throws DocumentException {
        if (top.isEmpty()) {
            addEmptyState(doc, "Nenhum registro encontrado.");
            return;
        }
        PdfPTable t = buildRankingTableHeader();
        int i = 1;
        for (var entry : top) {
            String name = resolver.resolve(entry.getKey());
            addRankingRow(t, i, name, entry.getValue(), unit, i % 2 == 0);
            i++;
        }
        doc.add(t);
    }

    private void addClientesTable(Document doc, List<Map.Entry<Integer, Long>> top,
                                  List<UsuarioEntity> usuarios) throws DocumentException {
        if (top.isEmpty()) {
            addEmptyState(doc, "Nenhum cliente encontrado.");
            return;
        }
        PdfPTable t = buildRankingTableHeader();
        int i = 1;
        for (var entry : top) {
            String nome = usuarios.stream()
                    .filter(u -> u.getId().equals(entry.getKey()))
                    .map(UsuarioEntity::getNome)
                    .findFirst()
                    .orElse("Cliente #" + entry.getKey());
            addRankingRow(t, i, nome, entry.getValue(), "pedidos", i % 2 == 0);
            i++;
        }
        doc.add(t);
    }

    private PdfPTable buildRankingTableHeader() {
        PdfPTable t = new PdfPTable(3);
        t.setWidthPercentage(100f);
        try { t.setWidths(new float[]{0.10f, 0.65f, 0.25f}); } catch (DocumentException ignored) {}
        t.setSpacingAfter(16f);
        t.setHeaderRows(1);

        t.addCell(buildHeaderCell("#"));
        t.addCell(buildHeaderCell("Item"));
        t.addCell(buildHeaderCell("Quantidade"));
        return t;
    }

    private PdfPCell buildHeaderCell(String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, FONT_TABLE_HEADER));
        cell.setBackgroundColor(COLOR_PRIMARY);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setPadding(8f);
        cell.setBorderWidth(0f);
        return cell;
    }

    private void addRankingRow(PdfPTable t, int rank, String name, long count, String unit, boolean zebra) {
        Color bg = zebra ? COLOR_ZEBRA : Color.WHITE;

        PdfPCell rankCell = new PdfPCell(new Phrase(String.valueOf(rank), FONT_TABLE_RANK));
        rankCell.setBackgroundColor(bg);
        rankCell.setPadding(8f);
        rankCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        rankCell.setBorderColor(COLOR_BORDER);
        rankCell.setBorderWidth(0.5f);

        PdfPCell nameCell = new PdfPCell(new Phrase(name, FONT_TABLE_CELL));
        nameCell.setBackgroundColor(bg);
        nameCell.setPadding(8f);
        nameCell.setBorderColor(COLOR_BORDER);
        nameCell.setBorderWidth(0.5f);

        PdfPCell countCell = new PdfPCell(new Phrase(count + " " + unit, FONT_TABLE_CELL));
        countCell.setBackgroundColor(bg);
        countCell.setPadding(8f);
        countCell.setBorderColor(COLOR_BORDER);
        countCell.setBorderWidth(0.5f);

        t.addCell(rankCell);
        t.addCell(nameCell);
        t.addCell(countCell);
    }

    private void addEmptyState(Document doc, String text) throws DocumentException {
        Paragraph p = new Paragraph(text, FONT_EMPTY);
        p.setSpacingAfter(16f);
        doc.add(p);
    }

    private static class HeaderFooterEvent extends PdfPageEventHelper {
        private static final DateTimeFormatter FMT =
                DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte cb = writer.getDirectContent();

            float headerHeight = 80f;
            float top = document.top() + 60f;

            cb.saveState();
            cb.setColorFill(COLOR_PRIMARY);
            cb.rectangle(0, top - headerHeight + 60f, document.getPageSize().getWidth(), headerHeight);
            cb.fill();
            cb.restoreState();

            cb.saveState();
            cb.setColorFill(COLOR_GOLD);
            cb.rectangle(0, top - headerHeight + 60f - 3f, document.getPageSize().getWidth(), 3f);
            cb.fill();
            cb.restoreState();

            float logoX = document.left();
            float logoCenterX = logoX + 14f;
            float logoCenterY = top - 14f;
            cb.saveState();
            cb.setColorFill(COLOR_GOLD);
            cb.circle(logoCenterX, logoCenterY, 14f);
            cb.fill();
            cb.restoreState();

            BaseFont bfBold;
            try {
                bfBold = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            } catch (Exception e) {
                return;
            }

            cb.saveState();
            cb.setColorFill(COLOR_PRIMARY_DARK);
            cb.beginText();
            cb.setFontAndSize(bfBold, 16f);
            cb.showTextAligned(Element.ALIGN_CENTER, "C", logoCenterX, logoCenterY - 5.5f, 0);
            cb.endText();
            cb.restoreState();

            cb.saveState();
            cb.setColorFill(Color.WHITE);
            cb.beginText();
            cb.setFontAndSize(bfBold, 24f);
            cb.showTextAligned(Element.ALIGN_LEFT, "Carambolos", logoX + 38f, top - 8f, 0);
            cb.endText();
            cb.restoreState();

            ColumnText subtitle = new ColumnText(cb);
            subtitle.setSimpleColumn(
                    logoX + 38f, top - 26f,
                    document.right(), top - 10f,
                    12f, Element.ALIGN_LEFT
            );
            subtitle.addText(new Phrase("Relatório de Insights de Pedidos", FONT_SUBTITLE));
            try { subtitle.go(); } catch (DocumentException ignored) {}

            ColumnText date = new ColumnText(cb);
            date.setSimpleColumn(
                    document.right() - 220f, top - 26f,
                    document.right(), top - 10f,
                    12f, Element.ALIGN_RIGHT
            );
            date.addText(new Phrase(
                    "Gerado em " + LocalDateTime.now().format(FMT),
                    new Font(Font.HELVETICA, 9, Font.NORMAL, COLOR_CREAM)
            ));
            try { date.go(); } catch (DocumentException ignored) {}

            float footerY = document.bottom() - 20f;

            cb.saveState();
            cb.setColorStroke(COLOR_BORDER);
            cb.setLineWidth(0.5f);
            cb.moveTo(document.left(), footerY + 14f);
            cb.lineTo(document.right(), footerY + 14f);
            cb.stroke();
            cb.restoreState();

            Font footerFont = new Font(Font.HELVETICA, 8, Font.NORMAL, COLOR_MUTED);

            ColumnText leftFooter = new ColumnText(cb);
            leftFooter.setSimpleColumn(
                    document.left(), footerY - 4f,
                    document.left() + 300f, footerY + 12f,
                    10f, Element.ALIGN_LEFT
            );
            leftFooter.addText(new Phrase("Carambolos · Confeitaria Artesanal", footerFont));
            try { leftFooter.go(); } catch (DocumentException ignored) {}

            ColumnText rightFooter = new ColumnText(cb);
            rightFooter.setSimpleColumn(
                    document.right() - 200f, footerY - 4f,
                    document.right(), footerY + 12f,
                    10f, Element.ALIGN_RIGHT
            );
            rightFooter.addText(new Phrase(
                    "Página " + writer.getPageNumber(),
                    new Font(Font.HELVETICA, 8, Font.BOLD, COLOR_PRIMARY)
            ));
            try { rightFooter.go(); } catch (DocumentException ignored) {}
        }
    }

    private static class ReportData {
        List<PedidoBoloEntity> pedidosBolo;
        List<PedidoFornada> pedidosFornada;
        List<UsuarioEntity> usuarioEntities;
        List<BoloEntity> boloEntities;
        List<MassaEntity> massas;
        List<RecheioUnitarioEntity> recheios;
        Set<Integer> adminIds = Collections.emptySet();
        List<Map.Entry<Integer, Long>> top3Bolos;
        List<Map.Entry<Integer, Long>> top3Fornadas;
        List<Map.Entry<Integer, Long>> top3Massas;
        List<Map.Entry<Integer, Long>> top3Recheios;
        List<Map.Entry<Integer, Long>> top3UsuariosBolo;
        List<Map.Entry<Integer, Long>> top3UsuariosFornada;
        Optional<Map.Entry<String, Long>> combinacaoMaisPedida = Optional.empty();
    }
}
