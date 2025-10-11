package com.carambolos.carambolosapi.infrastructure.gateways.impl;

import com.carambolos.carambolosapi.domain.entity.PedidoFornada;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.PedidoFornadaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoFornadaGatewayImplTest {

    @Mock
    private PedidoFornadaRepository repository;

    @InjectMocks
    private PedidoFornadaGatewayImpl gateway;

    @Test
    void save_shouldMapAndReturnDomain() {
        PedidoFornada domain = new PedidoFornada();
        domain.setId(null);
        domain.setFornadaDaVez(10);
        domain.setEndereco(20);
        domain.setUsuario(30);
        domain.setQuantidade(2);
        domain.setDataPrevisaoEntrega(LocalDate.now());
        domain.setNomeCliente("Cliente");
        domain.setTelefoneCliente("11999999999");
        domain.setHorarioRetirada("10:00");
        domain.setObservacoes("Obs");
        domain.setisAtivo(true);

        com.carambolos.carambolosapi.infrastructure.persistence.entity.PedidoFornada persisted = new com.carambolos.carambolosapi.infrastructure.persistence.entity.PedidoFornada();
        persisted.setId(1);
        persisted.setFornadaDaVez(domain.getFornadaDaVez());
        persisted.setEndereco(domain.getEndereco());
        persisted.setUsuario(domain.getUsuario());
        persisted.setQuantidade(domain.getQuantidade());
        persisted.setDataPrevisaoEntrega(domain.getDataPrevisaoEntrega());
        persisted.setNomeCliente(domain.getNomeCliente());
        persisted.setTelefoneCliente(domain.getTelefoneCliente());
        persisted.setHorarioRetirada(domain.getHorarioRetirada());
        persisted.setObservacoes(domain.getObservacoes());
        persisted.setAtivo(true);

        when(repository.save(any(com.carambolos.carambolosapi.infrastructure.persistence.entity.PedidoFornada.class)))
                .thenReturn(persisted);

        var result = gateway.save(domain);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(domain.getFornadaDaVez(), result.getFornadaDaVez());
        assertEquals(domain.getEndereco(), result.getEndereco());
        assertEquals(domain.getUsuario(), result.getUsuario());
        assertEquals(domain.getQuantidade(), result.getQuantidade());
        assertEquals(domain.getDataPrevisaoEntrega(), result.getDataPrevisaoEntrega());
        assertEquals(domain.getNomeCliente(), result.getNomeCliente());
        assertEquals(domain.getTelefoneCliente(), result.getTelefoneCliente());
        assertEquals(domain.getHorarioRetirada(), result.getHorarioRetirada());
        assertEquals(domain.getObservacoes(), result.getObservacoes());
        assertTrue(Boolean.TRUE.equals(result.getisAtivo()));

        ArgumentCaptor<com.carambolos.carambolosapi.infrastructure.persistence.entity.PedidoFornada> captor = ArgumentCaptor.forClass(com.carambolos.carambolosapi.infrastructure.persistence.entity.PedidoFornada.class);
        verify(repository, times(1)).save(captor.capture());
        var entitySent = captor.getValue();
        assertNull(entitySent.getId());
        assertEquals(domain.getFornadaDaVez(), entitySent.getFornadaDaVez());
        assertEquals(domain.getEndereco(), entitySent.getEndereco());
        assertEquals(domain.getUsuario(), entitySent.getUsuario());
        assertEquals(domain.getQuantidade(), entitySent.getQuantidade());
        assertEquals(domain.getDataPrevisaoEntrega(), entitySent.getDataPrevisaoEntrega());
        assertEquals(domain.getNomeCliente(), entitySent.getNomeCliente());
        assertEquals(domain.getTelefoneCliente(), entitySent.getTelefoneCliente());
        assertEquals(domain.getHorarioRetirada(), entitySent.getHorarioRetirada());
        assertEquals(domain.getObservacoes(), entitySent.getObservacoes());
        assertTrue(Boolean.TRUE.equals(entitySent.getAtivo()));
    }

    @Test
    void findById_shouldReturnMappedDomain() {
        com.carambolos.carambolosapi.infrastructure.persistence.entity.PedidoFornada entity = new com.carambolos.carambolosapi.infrastructure.persistence.entity.PedidoFornada();
        entity.setId(5);
        entity.setFornadaDaVez(10);
        entity.setEndereco(20);
        entity.setUsuario(30);
        entity.setQuantidade(3);
        entity.setAtivo(true);
        when(repository.findById(5)).thenReturn(Optional.of(entity));

        var opt = gateway.findById(5);
        assertTrue(opt.isPresent());
        var d = opt.get();
        assertEquals(5, d.getId());
        assertEquals(10, d.getFornadaDaVez());
        assertEquals(20, d.getEndereco());
        assertEquals(30, d.getUsuario());
        assertEquals(3, d.getQuantidade());
        assertTrue(Boolean.TRUE.equals(d.getisAtivo()));
    }

    @Test
    void findAll_shouldMapListToDomain() {
        com.carambolos.carambolosapi.infrastructure.persistence.entity.PedidoFornada e1 = new com.carambolos.carambolosapi.infrastructure.persistence.entity.PedidoFornada();
        e1.setId(1);
        com.carambolos.carambolosapi.infrastructure.persistence.entity.PedidoFornada e2 = new com.carambolos.carambolosapi.infrastructure.persistence.entity.PedidoFornada();
        e2.setId(2);
        when(repository.findAll()).thenReturn(List.of(e1, e2));

        var list = gateway.findAll();
        assertNotNull(list);
        assertEquals(2, list.size());
        assertEquals(1, list.get(0).getId());
        assertEquals(2, list.get(1).getId());
    }
}

