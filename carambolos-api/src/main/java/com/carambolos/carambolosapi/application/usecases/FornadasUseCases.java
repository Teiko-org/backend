package com.carambolos.carambolosapi.application.usecases;

import com.carambolos.carambolosapi.application.gateways.FornadaGateway;
import com.carambolos.carambolosapi.domain.entity.Fornada;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class FornadasUseCases {
    private final FornadaGateway gateway;

    public FornadasUseCases(FornadaGateway gateway) {
        this.gateway = gateway;
    }

    @Transactional
    public Fornada criar(Integer id, LocalDate inicio, LocalDate fim) {
        if (id != null && gateway.existsAtivaById(id)) {
            throw new IllegalArgumentException("Fornada com cadastro " + id + " já existe.");
        }
        var f = new Fornada(inicio, fim, true);
        if (id != null) f.setId(id);
        return gateway.save(f);
    }

    @Transactional
    public Fornada atualizar(Integer id, LocalDate inicio, LocalDate fim) {
        var f = buscarPorId(id);
        f.setDataInicio(inicio);
        f.setDataFim(fim);
        return gateway.save(f);
    }

    @Transactional
    public void encerrar(Integer id) {
        var f = gateway.findById(id).orElseThrow(() -> new RuntimeException(
            "Fornada com id " + id + " não encontrada."
        ));
        f.desativar();
        gateway.save(f);
    }

    public List<Fornada> listarAtivas() {
        return gateway.findAllAtivas();
    }

    public List<Fornada> listarTodas() {
        return gateway.findAll();
    }

    public List<Fornada> listarPorMesAno(int ano, int mes) {
        var inicio = LocalDate.of(ano, mes, 1);
        var fim = inicio.withDayOfMonth(inicio.lengthOfMonth());
        return gateway.findByDataInicioBetweenOrderByDataInicioAsc(inicio, fim);
    }

    public List<Fornada> buscarMaisRecente() {
        return gateway.findTop1ByAtivaTrueOrderByDataInicioDesc().stream().toList();
    }

    public Optional<Fornada> buscarProxima() {
        var hoje = java.time.LocalDate.now();
        return gateway.findAllByAtivaTrueOrderByDataInicioAsc()
                .stream()
                .filter(f -> f.getDataInicio()!=null && f.getDataInicio().isAfter(hoje))
                .filter(f -> f.getDataFim()==null || !f.getDataFim().isBefore(hoje))
                .findFirst();
    }

    public Fornada buscarPorId(Integer id) {
        return gateway.findById(id)
                .filter(f -> Boolean.TRUE.equals(f.getAtivo())).orElseThrow(() -> new RuntimeException("Fornada com cadastro "+id+" não encontrada."));
    }
}
