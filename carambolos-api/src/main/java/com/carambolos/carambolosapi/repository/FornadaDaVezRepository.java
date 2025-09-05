package com.carambolos.carambolosapi.repository;

import com.carambolos.carambolosapi.model.FornadaDaVez;
import com.carambolos.carambolosapi.model.projection.ProdutoFornadaDaVezProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface FornadaDaVezRepository extends JpaRepository<FornadaDaVez, Integer> {
    List<FornadaDaVez> findByFornada(Integer id);
    
    // Buscar produto específico em uma fornada específica
    FornadaDaVez findByFornadaAndProdutoFornadaAndIsAtivoTrue(Integer fornadaId, Integer produtoFornadaId);

    @Query(value = """
            select
                fdv.id fornada_da_vez_id,
                pf.id produto_fornada_id,
                pf.produto,
                pf.descricao,
                pf.valor,
                pf.categoria,
                fdv.quantidade,
                CAST(pf.is_ativo AS SIGNED) is_ativo_pf,
                CAST(fdv.is_ativo AS SIGNED) is_ativo_fdv,
                f.data_inicio,
                f.data_fim
            from fornada_da_vez fdv
            join produto_fornada pf on fdv.produto_fornada_id = pf.id
            join fornada f on f.data_inicio = ?1 AND f.data_fim = ?2
""", nativeQuery = true)
    List<ProdutoFornadaDaVezProjection> findProductsByFornada(LocalDate dataInicio, LocalDate dataFim);

    @Query(value = """
            select
                fdv.id fornada_da_vez_id,
                pf.id produto_fornada_id,
                pf.produto,
                pf.descricao,
                pf.valor,
                pf.categoria,
                fdv.quantidade,
                COALESCE(SUM(pf_pedidos.quantidade), 0) as quantidade_vendida,
                CAST(pf.is_ativo AS SIGNED) is_ativo_pf,
                CAST(fdv.is_ativo AS SIGNED) is_ativo_fdv,
                f.data_inicio,
                f.data_fim
            from fornada_da_vez fdv
            join produto_fornada pf on fdv.produto_fornada_id = pf.id
            join fornada f on fdv.fornada_id = f.id
            left join pedido_fornada pf_pedidos on pf_pedidos.fornada_da_vez_id = fdv.id and pf_pedidos.is_ativo = 1
            where f.id = ?1 and f.is_ativo = 1 and fdv.is_ativo = 1 and pf.is_ativo = 1
            group by fdv.id, pf.id, pf.produto, pf.descricao, pf.valor, pf.categoria, fdv.quantidade, pf.is_ativo, fdv.is_ativo, f.data_inicio, f.data_fim
            """, nativeQuery = true)
    List<ProdutoFornadaDaVezProjection> findProductsByFornadaId(Integer fornadaId);

    @Query(value = """
            select
                fdv.id fornada_da_vez_id,
                pf.id produto_fornada_id,
                pf.produto,
                pf.descricao,
                pf.valor,
                pf.categoria,
                fdv.quantidade,
                CAST(pf.is_ativo AS SIGNED) is_ativo_pf,
                CAST(fdv.is_ativo AS SIGNED) is_ativo_fdv,
                f.data_inicio,
                f.data_fim
            from fornada_da_vez fdv
            join produto_fornada pf on fdv.produto_fornada_id = pf.id
            join fornada f on fdv.fornada_id = f.id
            where f.id = :fornadaId 
            and f.is_ativo = 1 and fdv.is_ativo = 1 and pf.is_ativo = 1
            """, nativeQuery = true)
    List<ProdutoFornadaDaVezProjection> findByFornadaId(@Param("fornadaId") Integer fornadaId);
    
    @Query(value = """
            select
                fdv.id fornada_da_vez_id,
                pf.id produto_fornada_id,
                pf.produto,
                pf.descricao,
                pf.valor,
                pf.categoria,
                fdv.quantidade,
                CAST(pf.is_ativo AS SIGNED) is_ativo_pf,
                CAST(fdv.is_ativo AS SIGNED) is_ativo_fdv,
                f.data_inicio,
                f.data_fim
            from fornada_da_vez fdv
            join produto_fornada pf on fdv.produto_fornada_id = pf.id
            join fornada f on fdv.fornada_id = f.id
            where f.id = :fornadaId 
            and f.data_inicio between :dataInicio1 and :dataFim1
            and f.data_fim between :dataInicio2 and :dataFim2
            and f.is_ativo = 1 and fdv.is_ativo = 1 and pf.is_ativo = 1
            """, nativeQuery = true)
    List<ProdutoFornadaDaVezProjection> findByFornadaIdAndDataInicioBetweenAndDataFimBetween(
        @Param("fornadaId") Integer fornadaId, 
        @Param("dataInicio1") LocalDate dataInicio1, 
        @Param("dataFim1") LocalDate dataFim1, 
        @Param("dataInicio2") LocalDate dataInicio2, 
        @Param("dataFim2") LocalDate dataFim2);
}
