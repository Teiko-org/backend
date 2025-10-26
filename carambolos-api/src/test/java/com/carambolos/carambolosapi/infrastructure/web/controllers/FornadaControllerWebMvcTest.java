package com.carambolos.carambolosapi.infrastructure.web.controllers;

import com.carambolos.carambolosapi.application.usecases.FornadaDaVezUseCases;
import com.carambolos.carambolosapi.application.usecases.FornadasUseCases;
import com.carambolos.carambolosapi.application.usecases.PedidoFornadaUseCases;
import com.carambolos.carambolosapi.application.usecases.ProdutoFornadaUseCases;
import com.carambolos.carambolosapi.domain.entity.Fornada;
import com.carambolos.carambolosapi.domain.entity.ProdutoFornada;
import com.carambolos.carambolosapi.system.security.JwtBlacklistFilter;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.FornadaDaVez;
import com.carambolos.carambolosapi.infrastructure.web.request.FornadaRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FornadaController.class)
@AutoConfigureMockMvc(addFilters = false)
class FornadaControllerWebMvcTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private FornadasUseCases fornadasUseCases;
    @MockBean private FornadaDaVezUseCases fornadaDaVezService;
    @MockBean private ProdutoFornadaUseCases produtoFornadaService;
    @MockBean private PedidoFornadaUseCases pedidoFornadaUseCases;
    @MockBean private JwtBlacklistFilter jwtBlacklistFilter;

    @Test
    void listarFornadas_ok() throws Exception {
        Fornada d = new Fornada(LocalDate.of(2025,1,1), LocalDate.of(2025,1,2), true);
        d.setId(1);
        when(fornadasUseCases.listarAtivas()).thenReturn(List.of(d));
        mockMvc.perform(get("/fornadas"))
                .andExpect(status().isOk());
    }

    @Test
    void buscarProximaFornada_semResultados_204() throws Exception {
        when(fornadasUseCases.buscarProxima()).thenReturn(Optional.empty());
        mockMvc.perform(get("/fornadas/proxima"))
                .andExpect(status().isNoContent());
    }

    @Test
    void criarFornada_201() throws Exception {
        Fornada d = new Fornada(LocalDate.of(2025,1,1), LocalDate.of(2025,1,2), true);
        d.setId(10);
        FornadaRequestDTO body = new FornadaRequestDTO(null, LocalDate.of(2025,1,1), LocalDate.of(2025,1,2));
        when(fornadasUseCases.criar(null, body.dataInicio(), body.dataFim())).thenReturn(d);

        mockMvc.perform(post("/fornadas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated());
    }

    @Test
    void listarProdutosDaFornadaMaisRecente_semFornada_retornarOkVazio() throws Exception {
        when(fornadasUseCases.buscarMaisRecente()).thenReturn(List.of());
        mockMvc.perform(get("/fornadas/mais-recente/produtos"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void listarFornadasDaVez_ok() throws Exception {
        when(fornadaDaVezService.listarFornadasDaVez()).thenReturn(List.of(new FornadaDaVez()));
        mockMvc.perform(get("/fornadas/da-vez"))
                .andExpect(status().isOk());
    }

    @Test
    void listarProdutoFornada_ok() throws Exception {
        ProdutoFornada pf = new ProdutoFornada();
        pf.setId(1);
        pf.setProduto("Cookie");
        pf.setValor(5.0);
        when(produtoFornadaService.listarProdutosFornada(List.of())).thenReturn(List.of(pf));
        mockMvc.perform(get("/fornadas/produto-fornada"))
                .andExpect(status().isOk());
    }
}


