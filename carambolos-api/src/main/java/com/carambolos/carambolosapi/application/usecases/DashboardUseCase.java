package com.carambolos.carambolosapi.application.usecases;

import com.carambolos.carambolosapi.application.gateways.DashboardGateway;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Map;

public class DashboardUseCase {
    private final DashboardGateway dashboardGateway;

    public DashboardUseCase(DashboardGateway dashboardGateway) {
        this.dashboardGateway = dashboardGateway;
    }

    @Cacheable(cacheNames = "dashboard:qtdClientesUnicos")
    public long qtdClientesUnicos() {
        return dashboardGateway.qtdClientesUnicos();
    }

    @Cacheable(cacheNames = "dashboard:qtdPedidos")
    public Map<String, Long> countPedidos() {
        return dashboardGateway.countPedidos();
    }

    @Cacheable(cacheNames = "dashboard:qtdPedidosBolos")
    public Map<String, Long> countPedidosBolos() {
        return dashboardGateway.countPedidosBolos();
    }

    @Cacheable(cacheNames = "dashboard:qtdPedidosFornada")
    public Map<String, Long> countPedidosFornada() {
        return dashboardGateway.countPedidosFornada();
    }

    @Cacheable(cacheNames = "dashboard:bolosMaisPedidos")
    public List<Map<String, Object>> getBolosMaisPedidos() {
        return dashboardGateway.getBolosMaisPedidos();
    }

    @Cacheable(cacheNames = "dashboard:fornadasMaisPedidas")
    public List<Map<String, Object>> getFornadasMaisPedidas() {
        return dashboardGateway.getFornadasMaisPedidas();
    }

    @Cacheable(cacheNames = "dashboard:produtosMaisPedidos")
    public List<Map<String, Object>> getProdutosMaisPedidos() {
        return dashboardGateway.getProdutosMaisPedidos();
    }

    @Cacheable(cacheNames = "dashboard:produtosCadastrados")
    public List<Map<String, Object>> getProdutosCadastrados() {
        return dashboardGateway.getProdutosCadastrados();
    }

    @Cacheable(cacheNames = "dashboard:ultimosPedidos")
    public List<Map<String, Object>> getUltimosPedidos() {
        return dashboardGateway.getUltimosPedidos();
    }

    @Cacheable(cacheNames = "dashboard:qtdPedidosBolosPorPeriodo")
    public Map<String, Map<String, Long>> countPedidosBolosPorPeriodo(String periodo) {
        return dashboardGateway.countPedidosBolosPorPeriodo(periodo);
    }

    @Cacheable(cacheNames = "dashboard:qtdPedidosFornadaPorPeriodo")
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
