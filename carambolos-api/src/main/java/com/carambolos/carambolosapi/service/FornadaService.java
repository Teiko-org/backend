package com.carambolos.carambolosapi.service;

import com.carambolos.carambolosapi.controller.dto.FornadaRequestDTO;
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

        if (fornada.getId() != null && fornadaRepository.existsById(fornada.getId())) {
            throw new EntidadeJaExisteException("Fornada com cadastro " + fornada.getId() + " já existe.");
        }

        return fornadaRepository.save(fornada);
    }

    public List<Fornada> listarFornada() {
        return fornadaRepository.findAll();
    }

    public Fornada buscarFornada(Integer id) {
        return fornadaRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Fornada com cadastro " + id + " não encontrada."));
    }

    public void excluirFornada(Integer id) {
        if (!fornadaRepository.existsById(id)) {
            throw new EntidadeNaoEncontradaException("Não é possível excluir. Fornada com cadastro " + id + " não existe.");
        }
        fornadaRepository.deleteById(id);
    }

    public Fornada atualizarFornada(Integer id, FornadaRequestDTO request) {
        Fornada fornada = fornadaRepository.findById(id).orElseThrow(()
                -> new EntidadeNaoEncontradaException("Fornada com cadastro " + id + " não encontrada para atualização."));

        fornada.setDataInicio(request.dataInicio());
        fornada.setDataFim(request.dataFim());

        return fornadaRepository.save(fornada);
    }
}

