package com.carambolos.carambolosapi.infrastructure.web.controllers;

import com.carambolos.carambolosapi.application.usecases.ResumoPedidoService;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.ResumoPedidoEntity;
import com.carambolos.carambolosapi.domain.enums.StatusEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ResumoPedidoController.class)
class ResumoPedidoEntityControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ResumoPedidoService resumoPedidoService;

    @Test
    void listarResumosPedidos_quandoVazio_retorna204() throws Exception {
        when(resumoPedidoService.listarResumosPedidos()).thenReturn(List.of());
        mockMvc.perform(get("/resumo-pedido"))
                .andExpect(status().isNoContent());
    }

    @Test
    void listarResumosPedidos_quandoExiste_retorna200() throws Exception {
        when(resumoPedidoService.listarResumosPedidos()).thenReturn(List.of(new ResumoPedidoEntity()));
        mockMvc.perform(get("/resumo-pedido"))
                .andExpect(status().isOk());
    }

    @Test
    void buscarResumosPorStatus_quandoVazio_retorna204() throws Exception {
        when(resumoPedidoService.buscarResumosPedidosPorStatus(StatusEnum.PENDENTE)).thenReturn(List.of());
        mockMvc.perform(get("/resumo-pedido/status/PENDENTE"))
                .andExpect(status().isNoContent());
    }

    @Test
    void buscarPorDataPedido_quandoVazio_retorna204() throws Exception {
        when(resumoPedidoService.buscarResumosPedidosPorDataPedido(LocalDate.of(2025,1,1))).thenReturn(List.of());
        mockMvc.perform(get("/resumo-pedido/data-pedido/2025-01-01"))
                .andExpect(status().isNoContent());
    }

    @Test
    void marcarPedidoComoCancelado_ok() throws Exception {
        ResumoPedidoEntity rp = new ResumoPedidoEntity();
        rp.setStatus(StatusEnum.CANCELADO);
        when(resumoPedidoService.alterarStatus(1, StatusEnum.CANCELADO)).thenReturn(rp);
        mockMvc.perform(patch("/resumo-pedido/1/cancelado").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}


