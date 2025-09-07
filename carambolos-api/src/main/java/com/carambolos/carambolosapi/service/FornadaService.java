package com.carambolos.carambolosapi.service;

import com.carambolos.carambolosapi.controller.request.FornadaRequestDTO;
import com.carambolos.carambolosapi.exception.EntidadeJaExisteException;
import com.carambolos.carambolosapi.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.model.Fornada;
import com.carambolos.carambolosapi.repository.FornadaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Service
public class FornadaService {

    private final FornadaRepository fornadaRepository;

    public FornadaService(FornadaRepository fornadaRepository) {
        this.fornadaRepository = fornadaRepository;
    }

    public Fornada criarFornada(FornadaRequestDTO request) {
        Fornada fornada = request.toEntity();

        if (fornada.getId() != null && fornadaRepository.existsByIdAndIsAtivoTrue(fornada.getId())) {
            throw new EntidadeJaExisteException("Fornada com cadastro " + fornada.getId() + " já existe.");
        }

        return fornadaRepository.save(fornada);
    }

    public List<Fornada> listarFornada() {
        return fornadaRepository.findAll().stream().filter(Fornada::isAtivo).toList();
    }

    public List<Fornada> listarTodasFornadas() {
        return fornadaRepository.findAll();
    }

    public List<Fornada> listarFornadasPorMesAno(int ano, int mes) {
        YearMonth ym = YearMonth.of(ano, mes);
        LocalDate inicio = ym.atDay(1);
        LocalDate fim = ym.atEndOfMonth();
        // incluir ativas e encerradas para histórico completo
        return fornadaRepository.findByDataInicioBetweenOrderByDataInicioAsc(inicio, fim);
    }

    public Optional<Fornada> buscarFornadaMaisRecente() {
        return fornadaRepository.findTop1ByIsAtivoTrueOrderByDataInicioDesc();
    }

    public Optional<Fornada> buscarProximaFornada() {
        LocalDate hoje = LocalDate.now();
        // Buscar todas ativas e filtrar manualmente para evitar incluir fornadas "encerradas" (dataFim < hoje)
        return fornadaRepository.findAllByIsAtivoTrueOrderByDataInicioAsc()
                .stream()
                .filter(f -> f.getDataInicio() != null && f.getDataInicio().isAfter(hoje))
                .filter(f -> f.getDataFim() == null || !f.getDataFim().isBefore(hoje))
                .findFirst();
    }

    public Fornada buscarFornada(Integer id) {
        return fornadaRepository.findById(id)
                .filter(Fornada::isAtivo)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Fornada com cadastro " + id + " não encontrada."));
    }

    public void excluirFornada(Integer id) {
        Fornada fornada = fornadaRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Fornada com cadastro " + id + " não encontrada."));
        System.out.println("[ENCERRAR] encerrando fornada id=" + id + " ini=" + fornada.getDataInicio() + " fimAtual=" + fornada.getDataFim());
        // Encerrar: marcar inativa e preservar o intervalo planejado
        fornada.setAtivo(false);
        fornadaRepository.save(fornada);
        System.out.println("[ENCERRAR] fornada encerrada (ativo=false) id=" + id + " novoFim=" + fornada.getDataFim());
    }

    public Fornada atualizarFornada(Integer id, FornadaRequestDTO request) {
        Fornada fornada = buscarFornada(id);

        fornada.setId(id);
        fornada.setDataInicio(request.dataInicio());
        fornada.setDataFim(request.dataFim());

        return fornadaRepository.save(fornada);
    }
}

