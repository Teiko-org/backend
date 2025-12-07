package com.carambolos.carambolosapi.application.usecases;

import com.carambolos.carambolosapi.infrastructure.persistence.jpa.ProducaoRepository;
import com.carambolos.carambolosapi.infrastructure.web.response.MassasMaisPedidasPorMesResponse;
import com.carambolos.carambolosapi.infrastructure.web.response.PedidosPendentesPorMassaResponse;
import com.carambolos.carambolosapi.infrastructure.web.response.PedidosPendentesPorRecheioResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProducaoService {

    private final ProducaoRepository producaoRepository;

    public ProducaoService(ProducaoRepository producaoRepository) {
        this.producaoRepository = producaoRepository;
    }

    @Cacheable(cacheNames = "producao:pedidosPendentesPorMassa")
    public List<PedidosPendentesPorMassaResponse> getPedidosPendentesPorMassa() {
        List<Map<String, Object>> results = producaoRepository.findPedidosPendentesPorMassa();
        return results.stream()
                .map(PedidosPendentesPorMassaResponse::fromMap)
                .toList();
    }

    @Cacheable(cacheNames = "producao:pedidosPendentesPorRecheio")
    public List<PedidosPendentesPorRecheioResponse> getPedidosPendentesPorRecheio() {
        List<Map<String, Object>> results = producaoRepository.findPedidosPendentesPorRecheio();
        return results.stream()
                .map(PedidosPendentesPorRecheioResponse::fromMap)
                .toList();
    }

    @Cacheable(cacheNames = "producao:massasMaisPedidasPorMes", key = "#ano")
    public MassasMaisPedidasPorMesResponse getMassasMaisPedidasPorMes(int ano) {
        List<Map<String, Object>> results = producaoRepository.findMassasMaisPedidasPorMes(ano);
        
        String[] meses = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", 
                          "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};
        
        // Agrupar por massa
        Map<String, Map<Integer, Integer>> dadosPorMassa = new HashMap<>();
        for (Map<String, Object> row : results) {
            String massa = (String) row.get("massa");
            Integer mes = ((Number) row.get("mes")).intValue();
            Integer quantidade = ((Number) row.get("quantidade")).intValue();
            
            dadosPorMassa.computeIfAbsent(massa, k -> new HashMap<>()).put(mes, quantidade);
        }
        
        // Encontrar a massa mais pedida
        String massaMaisPedida = dadosPorMassa.entrySet().stream()
                .max(Comparator.comparingInt(e -> e.getValue().values().stream()
                        .mapToInt(Integer::intValue).sum()))
                .map(Map.Entry::getKey)
                .orElse("Cacau Expresso");
        
        // Criar série de dados para a massa mais pedida
        List<Integer> serie = new ArrayList<>();
        for (int i = 1; i <= 11; i++) {
            Integer quantidade = dadosPorMassa.getOrDefault(massaMaisPedida, new HashMap<>()).get(i);
            serie.add(quantidade != null ? quantidade : 0);
        }
        
        return new MassasMaisPedidasPorMesResponse(
            Arrays.asList(Arrays.copyOf(meses, 11)),
            List.of(new MassasMaisPedidasPorMesResponse.SerieData(massaMaisPedida, serie)),
            massaMaisPedida
        );
    }
}
