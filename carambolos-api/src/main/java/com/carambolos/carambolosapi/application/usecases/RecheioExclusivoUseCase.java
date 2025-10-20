package com.carambolos.carambolosapi.application.usecases;

import com.carambolos.carambolosapi.application.exception.EntidadeJaExisteException;
import com.carambolos.carambolosapi.application.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.application.gateways.RecheioExclusivoGateway;
import com.carambolos.carambolosapi.application.gateways.RecheioUnitarioGateway;
import com.carambolos.carambolosapi.domain.entity.RecheioExclusivo;
import com.carambolos.carambolosapi.domain.projection.RecheioExclusivoProjection;

import java.util.List;

public class RecheioExclusivoUseCase {
    private final RecheioExclusivoGateway recheioExclusivoGateway;
    private final RecheioUnitarioGateway recheioUnitarioGateway;

    public RecheioExclusivoUseCase(RecheioExclusivoGateway recheioExclusivoGateway, RecheioUnitarioGateway recheioUnitarioGateway) {
        this.recheioExclusivoGateway = recheioExclusivoGateway;
        this.recheioUnitarioGateway = recheioUnitarioGateway;
    }

    public RecheioExclusivoProjection cadastrarRecheioExclusivo(RecheioExclusivo recheioExclusivo) {
        int id1 = recheioExclusivo.getRecheioUnitarioId1();
        int id2 = recheioExclusivo.getRecheioUnitarioId2();

        boolean recheiosExistem = recheioUnitarioGateway.existsByIdAndIsAtivoTrue(id1) && recheioUnitarioGateway.existsByIdAndIsAtivoTrue(id2);
        if (!recheiosExistem) {
            throw new EntidadeNaoEncontradaException("Um ou mais recheios cadastrados não existem");
        }

        Integer recheiosExistentes1 = recheioExclusivoGateway.countByRecheioUnitarioIds(id1, id2);
        Integer recheiosExistentes2 = recheioExclusivoGateway.countByRecheioUnitarioIds(id2, id1);

        if (recheiosExistentes1 > 0 || recheiosExistentes2 > 0) {
            throw new EntidadeJaExisteException("Recheio exclusivo já existente");
        }

        RecheioExclusivo recheioSalvo = recheioExclusivoGateway.save(recheioExclusivo);
        return recheioExclusivoGateway.buscarRecheioExclusivoPorId(recheioSalvo.getId());
    }

    public RecheioExclusivoProjection buscarRecheioExclusivoPorId(Integer id) {
        return recheioExclusivoGateway.buscarRecheioExclusivoPorId(id);
    }

    public List<RecheioExclusivoProjection> listarRecheiosExclusivos() {
        return recheioExclusivoGateway.listarRecheiosExclusivos();
    }

    public RecheioExclusivoProjection editarRecheioExclusivo(RecheioExclusivo recheioExclusivo, Integer id) {
        RecheioExclusivo recheioExistente = recheioExclusivoGateway.findById(id);

        Integer id1 = recheioExclusivo.getRecheioUnitarioId1();
        Integer id2 = recheioExclusivo.getRecheioUnitarioId2();
        Integer recheiosExistentes1 = recheioExclusivoGateway.countByRecheioUnitarioIds(id1, id2);
        Integer recheiosExistentes2 = recheioExclusivoGateway.countByRecheioUnitarioIds(id2, id1);

        if (recheiosExistentes1 > 0 || recheiosExistentes2 > 0) {
            throw new EntidadeJaExisteException("Recheio exclusivo já existente");
        }

        recheioExistente = verificarCampos(recheioExclusivo, recheioExistente);
        recheioExistente.setId(id);
        recheioExclusivoGateway.save(recheioExistente);

        return buscarRecheioExclusivoPorId(id);
    }

    public void excluirRecheioExclusivo(Integer id) {
        RecheioExclusivo recheio = recheioExclusivoGateway.findById(id);
        recheio.setAtivo(false);
        recheioExclusivoGateway.save(recheio);
    }
    private RecheioExclusivo verificarCampos(RecheioExclusivo recheioExclusivo, RecheioExclusivo recheioExistente) {
        if (recheioExclusivo.getNome() != null) {
            recheioExistente.setNome(recheioExclusivo.getNome());
        }
        if (recheioExclusivo.getRecheioUnitarioId1() != null) {
            recheioExistente.setRecheioUnitarioId1(recheioExclusivo.getRecheioUnitarioId1());
        }
        if (recheioExclusivo.getRecheioUnitarioId2() != null) {
            recheioExistente.setRecheioUnitarioId2(recheioExclusivo.getRecheioUnitarioId2());
        }
        return recheioExistente;
    }
}
