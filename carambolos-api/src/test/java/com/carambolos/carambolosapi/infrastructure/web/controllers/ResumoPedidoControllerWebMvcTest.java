package com.carambolos.carambolosapi.infrastructure.web.controllers;

import com.carambolos.carambolosapi.application.usecases.ResumoPedidoService;
import com.carambolos.carambolosapi.domain.entity.ResumoPedido;
import com.carambolos.carambolosapi.domain.enums.StatusEnum;
import com.carambolos.carambolosapi.system.security.JwtBlacklistFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ResumoPedidoController.class)
@AutoConfigureMockMvc(addFilters = false)
class ResumoPedidoControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ResumoPedidoService resumoPedidoService;

    @MockBean
    private JwtBlacklistFilter jwtBlacklistFilter;

    @Test
    void listarResumosPedidos_quandoVazio_retorna204() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ResumoPedido> paginaVazia = new PageImpl<>(List.of(), pageable, 0);

        when(resumoPedidoService.listarResumosPedidos(any(Pageable.class))).thenReturn(paginaVazia);
        mockMvc.perform(get("/resumo-pedido"))
                .andExpect(status().isNoContent());
    }

    @Test
    void listarResumosPedidos_quandoExiste_retorna200() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ResumoPedido> paginaComConteudo =
                new PageImpl<>(List.of(new ResumoPedido()), pageable, 1);

        when(resumoPedidoService.listarResumosPedidos(any(Pageable.class))).thenReturn(paginaComConteudo);
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
        ResumoPedido rp = new ResumoPedido();
        rp.setStatus(StatusEnum.CANCELADO);
        when(resumoPedidoService.alterarStatus(1, StatusEnum.CANCELADO)).thenReturn(rp);
        mockMvc.perform(patch("/resumo-pedido/1/cancelado").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}


