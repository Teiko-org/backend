package com.carambolos.carambolosapi.repository;

import com.carambolos.carambolosapi.model.FornadaDaVez;
import com.carambolos.carambolosapi.model.projection.ProdutoFornadaDaVezProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface FornadaDaVezRepository extends JpaRepository<FornadaDaVez, Integer> {
    List<FornadaDaVez> findByFornada(Integer id);

    @Query(value = """
            select\s
            	fdv.id fornada_da_vez_id,
            	pf.id produto_fornada_id,
            	pf.produto,
            	pf.descricao,
            	pf.valor,
            	pf.categoria,
                fdv.quantidade,
            	pf.is_ativo is_ativo_pf,
            	fdv.is_ativo is_ativo_fdv,
                f.data_inicio,
                f.data_fim
            from fornada_da_vez fdv
            join produto_fornada pf on fdv.produto_fornada_id = pf.id
            join fornada f on f.data_inicio = ?1 AND f.data_fim = ?2
            """, nativeQuery = true)
    List<ProdutoFornadaDaVezProjection> findProductsByFornada(LocalDate dataInicio, LocalDate dataFim);

    @Query(value = """
            select\s
            	fdv.id fornada_da_vez_id,
            	pf.id produto_fornada_id,
            	pf.produto,
            	pf.descricao,
            	pf.valor,
            	pf.categoria,
                fdv.quantidade,
            	pf.is_ativo is_ativo_pf,
            	fdv.is_ativo is_ativo_fdv,
                f.data_inicio,
                f.data_fim
            from fornada_da_vez fdv
            join produto_fornada pf on fdv.produto_fornada_id = pf.id
            join fornada f on fdv.fornada_id = f.id
            where f.id = ?1 and f.is_ativo = 1 and fdv.is_ativo = 1 and pf.is_ativo = 1
            """, nativeQuery = true)
    List<ProdutoFornadaDaVezProjection> findProductsByFornadaId(Integer fornadaId);
}
