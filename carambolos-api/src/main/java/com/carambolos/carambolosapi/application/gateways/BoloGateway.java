package com.carambolos.carambolosapi.application.gateways;

import com.carambolos.carambolosapi.domain.entity.Bolo;
import com.carambolos.carambolosapi.domain.entity.ImagemDecoracao;
import com.carambolos.carambolosapi.domain.projection.DetalheBoloProjection;

import java.util.List;

public interface BoloGateway {
    List<Bolo> findByCategoriaIn(List<String> categorias);
    List<Bolo> findAll();
    List<DetalheBoloProjection> listarDetalheBolo();
    Bolo findById(Integer id);
    Boolean existsByIdAndIsAtivoTrue(Integer id);
    Bolo save(Bolo bolo);
    Boolean existsByIdAndIdNotAndIsAtivoTrue(Integer id, Integer excludeId);
    Boolean existsById(Integer id);
    void atualizarStatusBolo(Integer status, Integer id);
    List<ImagemDecoracao> findAllImagensByDecoracao(Integer boloId);
}
