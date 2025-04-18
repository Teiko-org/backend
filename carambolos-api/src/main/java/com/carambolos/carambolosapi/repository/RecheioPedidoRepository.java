package com.carambolos.carambolosapi.repository;

import com.carambolos.carambolosapi.model.RecheioPedido;
import com.carambolos.carambolosapi.model.projection.RecheioPedidoProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RecheioPedidoRepository extends JpaRepository<RecheioPedido, Integer> {
    @Query(value = """
            select
            rp.id,
            ru1.sabor,
            ru2.sabor,
            sum(ru1.valor + ru2.valor) valor
            from recheio_pedido rp
            join recheio_unitario ru1 on rp.recheio_unitario_id1 = ru1.id
            join recheio_unitario ru2 on rp.recheio_unitario_id2 = ru2.id
            where rp.id = ?1
            """, nativeQuery = true)
    RecheioPedidoProjection buscarRecheioPedidoUnitariosPorId(Integer id);

    @Query(value = """
            select\s
            rp.id,
            ru1.sabor sabor1,
            ru2.sabor sabor2,
            sum(ru1.valor + ru2.valor) valor
            from recheio_pedido rp
            join recheio_exclusivo re on rp.recheio_exclusivo = re.id
            join recheio_unitario ru1 on re.recheio_unitario_id1 = ru1.id
            join recheio_unitario ru2 on re.recheio_unitario_id2 = ru2.id
            where rp.id = ?1
            group by ru1.sabor, ru2.sabor;
            """, nativeQuery = true)
    RecheioPedidoProjection buscarRecheioPedidoExclusivoPorId(Integer id);
}
