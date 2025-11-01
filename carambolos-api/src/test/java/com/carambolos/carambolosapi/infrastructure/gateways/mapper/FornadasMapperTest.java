package com.carambolos.carambolosapi.infrastructure.gateways.mapper;

import com.carambolos.carambolosapi.domain.entity.Fornada;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.PedidoFornada;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FornadasMapperTest {

    @Test
    void toDomain_fornada_shouldMapAllFields() {
        com.carambolos.carambolosapi.infrastructure.persistence.entity.Fornada e = new com.carambolos.carambolosapi.infrastructure.persistence.entity.Fornada();
        e.setId(7);
        e.setDataInicio(LocalDate.of(2025, 1, 1));
        e.setDataFim(LocalDate.of(2025, 1, 2));
        e.setAtivo(true);

        var d = FornadasMapper.toDomain(e);
        assertNotNull(d);
        assertEquals(7, d.getId());
        assertEquals(LocalDate.of(2025, 1, 1), d.getDataInicio());
        assertEquals(LocalDate.of(2025, 1, 2), d.getDataFim());
        assertTrue(Boolean.TRUE.equals(d.getAtivo()));
    }

    @Test
    void toEntity_fornada_shouldMapAllFields() {
        Fornada d = new Fornada(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 2), true);
        d.setId(9);

        var e = FornadasMapper.toEntity(d);
        assertNotNull(e);
        assertEquals(9, e.getId());
        assertEquals(LocalDate.of(2025, 1, 1), e.getDataInicio());
        assertEquals(LocalDate.of(2025, 1, 2), e.getDataFim());
        assertTrue(Boolean.TRUE.equals(e.isAtivo()));
    }

    @Test
    void toDomain_pedidoFornada_shouldMapAllFields() {
        var e = new com.carambolos.carambolosapi.infrastructure.persistence.entity.PedidoFornada();
        e.setId(1);
        e.setFornadaDaVez(10);
        e.setEndereco(20);
        e.setUsuario(30);
        e.setQuantidade(2);
        e.setNomeCliente("João");
        e.setTelefoneCliente("11999999999");
        e.setHorarioRetirada("10:00");
        e.setObservacoes("Sem açúcar");
        e.setAtivo(true);

        var d = FornadasMapper.toDomain(e);
        assertNotNull(d);
        assertEquals(1, d.getId());
        assertEquals(10, d.getFornadaDaVez());
        assertEquals(20, d.getEndereco());
        assertEquals(30, d.getUsuario());
        assertEquals(2, d.getQuantidade());
        assertEquals("João", d.getNomeCliente());
        assertEquals("11999999999", d.getTelefoneCliente());
        assertEquals("10:00", d.getHorarioRetirada());
        assertEquals("Sem açúcar", d.getObservacoes());
        assertTrue(Boolean.TRUE.equals(d.getisAtivo()));
    }

    @Test
    void toEntity_pedidoFornada_shouldMapAllFields() {
        var d = new com.carambolos.carambolosapi.domain.entity.PedidoFornada();
        d.setId(2);
        d.setFornadaDaVez(11);
        d.setEndereco(21);
        d.setUsuario(31);
        d.setQuantidade(3);
        d.setNomeCliente("Maria");
        d.setTelefoneCliente("11888888888");
        d.setHorarioRetirada("11:00");
        d.setObservacoes("Com cobertura");
        d.setisAtivo(true);

        var e = FornadasMapper.toEntity(d);
        assertNotNull(e);
        assertEquals(2, e.getId());
        assertEquals(11, e.getFornadaDaVez());
        assertEquals(21, e.getEndereco());
        assertEquals(31, e.getUsuario());
        assertEquals(3, e.getQuantidade());
        assertEquals("Maria", e.getNomeCliente());
        assertEquals("11888888888", e.getTelefoneCliente());
        assertEquals("11:00", e.getHorarioRetirada());
        assertEquals("Com cobertura", e.getObservacoes());
        assertTrue(Boolean.TRUE.equals(e.getAtivo()));
    }
}


