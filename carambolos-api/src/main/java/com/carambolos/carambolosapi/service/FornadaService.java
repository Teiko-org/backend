package com.carambolos.carambolosapi.service;

import com.carambolos.carambolosapi.controller.request.FornadaRequestDTO;
import com.carambolos.carambolosapi.exception.EntidadeJaExisteException;
import com.carambolos.carambolosapi.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.model.Fornada;
import com.carambolos.carambolosapi.repository.FornadaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FornadaService {

    private FornadaRepository fornadaRepository;

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

    public Fornada buscarFornada(Integer id) {
        return fornadaRepository.findById(id)
                .filter(Fornada::isAtivo)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Fornada com cadastro " + id + " não encontrada."));
    }

    public void excluirFornada(Integer id) {
        Fornada fornada = buscarFornada(id);
        fornada.setAtivo(false);
        fornadaRepository.save(fornada);
    }

    public Fornada atualizarFornada(Integer id, FornadaRequestDTO request) {
        Fornada fornada = buscarFornada(id);

        fornada.setId(id);
        fornada.setDataInicio(request.dataInicio());
        fornada.setDataFim(request.dataFim());

        return fornadaRepository.save(fornada);
    }
}

