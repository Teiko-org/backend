package com.carambolos.carambolosapi.service;

import com.carambolos.carambolosapi.exception.EntidadeJaExisteException;
import com.carambolos.carambolosapi.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.model.RecheioUnitario;
import com.carambolos.carambolosapi.repository.RecheioUnitarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoloService {
    @Autowired
    private RecheioUnitarioRepository recheioUnitarioRepository;

    public RecheioUnitario cadastrarRecheioUnitario(RecheioUnitario recheioUnitario) {
        return recheioUnitarioRepository.save(recheioUnitario);
    }

    public List<RecheioUnitario> listarRecheiosUnitarios() {
        return recheioUnitarioRepository.findAll();
    }

    public RecheioUnitario atualizarRecheioUnitario(RecheioUnitario recheioUnitario, Integer id) {
        RecheioUnitario recheioExistente = recheioUnitarioRepository.findById(recheioUnitario.getId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Recheio com o id %d não encontrado".formatted(id)));
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

    public Void deletarRecheioUnitario(Integer id) {
        if (recheioUnitarioRepository.existsById(id)) {
            recheioUnitarioRepository.deleteById(id);
        }
        throw new EntidadeNaoEncontradaException("Recheio com id %d não existe".formatted(id));
    }
}
