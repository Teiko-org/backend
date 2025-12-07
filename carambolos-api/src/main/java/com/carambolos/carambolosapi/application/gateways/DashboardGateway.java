package com.carambolos.carambolosapi.application.gateways;

import com.carambolos.carambolosapi.domain.entity.ResumoPedido;

import java.util.List;
import java.util.Map;

public interface DashboardGateway {
    long qtdClientesUnicos();
    Map<String, Long> countPedidos();
    Map<String, Long> countPedidosBolos();
    Map<String, Long> countPedidosFornada();
    List<Map<String, Object>> getBolosMaisPedidos();
    List<Map<String, Object>> getFornadasMaisPedidas();
    List<Map<String, Object>> getProdutosMaisPedidos();
    List<Map<String, Object>> getProdutosCadastrados();
    List<Map<String, Object>> getUltimosPedidos();
    Map<String, Map<String, Long>> countPedidosBolosPorPeriodo(String periodo);
    Map<String, Map<String, Long>> countPedidosFornadaPorPeriodo(String periodo);
    Map<String, Object> getKPIFornada(Integer fornadaId);
    Map<String, Object> getKPIFornadaMaisRecente();
    Map<String, Object> getKPIFornadasPorMesAno(int ano, int mes);
    List<Map<String, Object>> getMassasPendentes();
    List<Map<String, Object>> getRecheiosPendentes();
    List<Map<String, Object>> getPedidosProximosDaEntrega(int diasProximos);
    List<Map<String, Object>> getItensMaisPedidosPorPeriodo(String tipoItem, String periodo, Integer ano, Integer mes);
}
