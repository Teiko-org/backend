package com.carambolos.carambolosapi.application.usecases;

import com.carambolos.carambolosapi.application.gateways.DashboardGateway;

import java.util.List;
import java.util.Map;

public class DashboardUseCase {
    private final DashboardGateway dashboardGateway;

    public DashboardUseCase(DashboardGateway dashboardGateway) {
        this.dashboardGateway = dashboardGateway;
    }

    public long qtdClientesUnicos() {
        return dashboardGateway.qtdClientesUnicos();
    }

    public Map<String, Long> countPedidos() {
        return dashboardGateway.countPedidos();
    }

    public Map<String, Long> countPedidosBolos() {
        return dashboardGateway.countPedidosBolos();
    }

    public Map<String, Long> countPedidosFornada() {
        return dashboardGateway.countPedidosFornada();
    }

    public List<Map<String, Object>> getBolosMaisPedidos() {
        return dashboardGateway.getBolosMaisPedidos();
    }

    public List<Map<String, Object>> getFornadasMaisPedidas() {
        return dashboardGateway.getFornadasMaisPedidas();
    }

    @Deprecated
    public List<Map<String, Object>> getProdutosMaisPedidos() {
        return dashboardGateway.getProdutosMaisPedidos();
    }

    public List<Map<String, Object>> getProdutosCadastrados() {
        return dashboardGateway.getProdutosCadastrados();
    }

    public List<Map<String, Object>> getUltimosPedidos() {
        return dashboardGateway.getUltimosPedidos();
    }

    public Map<String, Map<String, Long>> countPedidosBolosPorPeriodo(String periodo) {
        return dashboardGateway.countPedidosBolosPorPeriodo(periodo);
    }

    public Map<String, Map<String, Long>> countPedidosFornadaPorPeriodo(String periodo) {
        return dashboardGateway.countPedidosFornadaPorPeriodo(periodo);
    }

    public Map<String, Object> getKPIFornada(Integer fornadaId) {
        return dashboardGateway.getKPIFornada(fornadaId);
    }

    public Map<String, Object> getKPIFornadaMaisRecente() {
        return dashboardGateway.getKPIFornadaMaisRecente();
    }

    public Map<String, Object> getKPIFornadasPorMesAno(int ano, int mes) {
        return dashboardGateway.getKPIFornadasPorMesAno(ano, mes);
    }

}
