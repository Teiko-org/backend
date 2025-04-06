package com.carambolos.carambolosapi.service;

import com.carambolos.carambolosapi.entities.RecheioExclusivoProjection;
import com.carambolos.carambolosapi.exception.EntidadeJaExisteException;
import com.carambolos.carambolosapi.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.model.RecheioExclusivo;
import com.carambolos.carambolosapi.model.RecheioUnitario;
import com.carambolos.carambolosapi.repository.RecheioExclusivoRepository;
import com.carambolos.carambolosapi.repository.RecheioUnitarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoloService {
    @Autowired
    private RecheioUnitarioRepository recheioUnitarioRepository;

    @Autowired
    private RecheioExclusivoRepository recheioExclusivoRepository;

    public RecheioUnitario cadastrarRecheioUnitario(RecheioUnitario recheioUnitario) {
        if(recheioUnitarioRepository.countBySaborIgnoreCase(recheioUnitario.getSabor()) > 0) {
            throw new EntidadeJaExisteException("Recheio com o sabor %s já existe".formatted(recheioUnitario.getSabor()));
        }
        return recheioUnitarioRepository.save(recheioUnitario);
    }

    public List<RecheioUnitario> listarRecheiosUnitarios() {
        return recheioUnitarioRepository.findAll();
    }

    public RecheioUnitario buscarPorId(Integer id) {
        return recheioUnitarioRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Recheio com o id %d não encontrado".formatted(id)));
    }

    public RecheioUnitario atualizarRecheioUnitario(RecheioUnitario recheioUnitario, Integer id) {
        RecheioUnitario recheioExistente = recheioUnitarioRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Recheio com o id %d não encontrado".formatted(id)));

        if(recheioUnitarioRepository.countBySaborIgnoreCase(recheioUnitario.getSabor()) > 0) {
            throw new EntidadeJaExisteException("Recheio com o sabor %s já existe".formatted(recheioUnitario.getSabor()));
        }

        if (recheioUnitario.getSabor() != null) {
            recheioExistente.setSabor(recheioUnitario.getSabor());
        }
        if (recheioUnitario.getDescricao() != null) {
            recheioExistente.setDescricao(recheioUnitario.getDescricao());
        }
        if (recheioUnitario.getValor() != null) {
            recheioExistente.setValor(recheioUnitario.getValor());
        }

        recheioExistente.setId(id);

        return recheioUnitarioRepository.save(recheioExistente);
    }

    public void deletarRecheioUnitario(Integer id) {
        if (!recheioUnitarioRepository.existsById(id)) {
            throw new EntidadeNaoEncontradaException("Recheio com id %d não existe".formatted(id));
        }
        recheioUnitarioRepository.deleteById(id);
    }

    public RecheioExclusivo cadastrarRecheioExclusivo(RecheioExclusivo recheioExclusivo) {
        Integer recheiosExistentes = recheioExclusivoRepository.countByRecheioUnitarioIds(
                recheioExclusivo.getRecheioUnitarioId1(),
                recheioExclusivo.getRecheioUnitarioId2()
        );

        if(recheiosExistentes > 0) {
            throw new EntidadeJaExisteException("Recheio exclusivo já existente");
        }

        return recheioExclusivoRepository.save(recheioExclusivo);
    }

    public RecheioExclusivoProjection buscarRecheioExclusivoPorId(Integer id) {
        return recheioExclusivoRepository.buscarRecheioExclusivoPorId(id);
    }
}
