package com.carambolos.carambolosapi.infrastructure.persistence.jpa;

import com.carambolos.carambolosapi.domain.projection.RecheioExclusivoProjection;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.RecheioExclusivoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecheioExclusivoRepository extends JpaRepository<RecheioExclusivoEntity, Integer> {
    @Query(value = """
            select count(1) from recheio_exclusivo re
            where re.recheio_unitario_id1 = ?1 and re.recheio_unitario_id2 = ?2
            """, nativeQuery = true)
    Integer countByRecheioUnitarioIds(Integer id1, Integer id2);

    @Query(value = """
        select re.id, re.nome, ru1.sabor, ru2.sabor, CAST(re.is_ativo AS SIGNED) as is_ativo
        from recheio_exclusivo re
        join recheio_unitario ru1 on re.recheio_unitario_id1 = ru1.id
        join recheio_unitario ru2 on re.recheio_unitario_id2 = ru2.id
        where re.id = ?1
        """, nativeQuery = true)
    RecheioExclusivoProjection buscarRecheioExclusivoPorId(Integer id);

    @Query(value = """
            select re.id, re.nome, ru1.sabor, ru2.sabor, CAST(re.is_ativo AS SIGNED) as is_ativo
            from recheio_exclusivo re\s
            join recheio_unitario ru1 on re.recheio_unitario_id1 = ru1.id
            join recheio_unitario ru2 on re.recheio_unitario_id2 = ru2.id;
            """, nativeQuery = true)
    List<RecheioExclusivoProjection> listarRecheiosExclusivos();
}
