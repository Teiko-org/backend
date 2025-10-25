package com.carambolos.carambolosapi.infrastructure.web.controllers;

import com.carambolos.carambolosapi.application.usecases.ResumoPedidoUseCase;
import com.carambolos.carambolosapi.domain.entity.ResumoPedido;
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
    private ResumoPedidoUseCase resumoPedidouseCase;

    @Test
    void listarResumosPedidos_quandoVazio_retorna204() throws Exception {
        when(resumoPedidouseCase.listarResumosPedidos()).thenReturn(List.of());
        mockMvc.perform(get("/resumo-pedido"))
                .andExpect(status().isNoContent());
    }

    @Test
    void listarResumosPedidos_quandoExiste_retorna200() throws Exception {
        when(resumoPedidouseCase.listarResumosPedidos()).thenReturn(List.of(new ResumoPedido()));
        mockMvc.perform(get("/resumo-pedido"))
                .andExpect(status().isOk());
    }

    @Test
    void buscarResumosPorStatus_quandoVazio_retorna204() throws Exception {
        when(resumoPedidouseCase.buscarResumosPedidosPorStatus(StatusEnum.PENDENTE)).thenReturn(List.of());
        mockMvc.perform(get("/resumo-pedido/status/PENDENTE"))
                .andExpect(status().isNoContent());
    }

    @Test
    void buscarPorDataPedido_quandoVazio_retorna204() throws Exception {
        when(resumoPedidouseCase.buscarResumosPedidosPorDataPedido(LocalDate.of(2025,1,1))).thenReturn(List.of());
        mockMvc.perform(get("/resumo-pedido/data-pedido/2025-01-01"))
                .andExpect(status().isNoContent());
    }

    @Test
    void marcarPedidoComoCancelado_ok() throws Exception {
        ResumoPedido rp = new ResumoPedido();
        rp.setStatus(StatusEnum.CANCELADO);
        when(resumoPedidouseCase.alterarStatus(1, StatusEnum.CANCELADO)).thenReturn(rp);
        mockMvc.perform(patch("/resumo-pedido/1/cancelado").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}


