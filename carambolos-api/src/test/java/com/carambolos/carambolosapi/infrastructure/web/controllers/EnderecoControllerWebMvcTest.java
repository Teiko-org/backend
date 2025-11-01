package com.carambolos.carambolosapi.infrastructure.web.controllers;

import com.carambolos.carambolosapi.application.usecases.EnderecoUseCase;
import com.carambolos.carambolosapi.domain.entity.Endereco;
import com.carambolos.carambolosapi.infrastructure.web.request.EnderecoRequestDTO;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.EnderecoMapper;
import com.carambolos.carambolosapi.system.security.JwtBlacklistFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EnderecoController.class)
@AutoConfigureMockMvc(addFilters = false)
class EnderecoControllerWebMvcTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private EnderecoUseCase enderecoUseCase;
    @MockBean private EnderecoMapper enderecoMapper;
    @MockBean private JwtBlacklistFilter jwtBlacklistFilter;

    @Test
    void listar_quandoVazio_204() throws Exception {
        when(enderecoUseCase.listar(any(Pageable.class))).thenReturn(Page.empty());
        mockMvc.perform(get("/enderecos"))
                .andExpect(status().isNoContent());
    }

    @Test
    void listar_quandoTemDados_200() throws Exception {
        Page<Endereco> page = new PageImpl<>(List.of(new Endereco()), PageRequest.of(0,10), 1);
        when(enderecoUseCase.listar(any(Pageable.class))).thenReturn(page);
        mockMvc.perform(get("/enderecos"))
                .andExpect(status().isOk());
    }

    @Test
    void buscarPorId_ok() throws Exception {
        when(enderecoUseCase.buscarPorId(1)).thenReturn(new Endereco());
        mockMvc.perform(get("/enderecos/1"))
                .andExpect(status().isOk());
    }

    @Test
    void cadastrar_201() throws Exception {
        EnderecoRequestDTO req = new EnderecoRequestDTO();
        req.setCep("12345678");
        req.setEstado("SP");
        req.setCidade("Sao Paulo");
        req.setBairro("Centro");
        req.setLogradouro("Rua A");
        req.setNumero("123");
        req.setComplemento("Compl");
        req.setReferencia("Ref");
        when(enderecoUseCase.cadastrar(any(Endereco.class))).thenReturn(new Endereco());
        mockMvc.perform(post("/enderecos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());
    }

    @Test
    void atualizar_200() throws Exception {
        EnderecoRequestDTO req = new EnderecoRequestDTO();
        req.setCep("12345678");
        req.setEstado("SP");
        req.setCidade("Sao Paulo");
        req.setBairro("Centro");
        req.setLogradouro("Rua B");
        req.setNumero("456");
        req.setComplemento("Compl");
        req.setReferencia("Ref");
        when(enderecoUseCase.atualizar(eq(1), any(Endereco.class))).thenReturn(new Endereco());
        mockMvc.perform(put("/enderecos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @Test
    void listarPorUsuario_quandoVazio_204() throws Exception {
        when(enderecoUseCase.listarPorUsuario(7)).thenReturn(List.of());
        mockMvc.perform(get("/enderecos/usuario/7"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deletar_204() throws Exception {
        mockMvc.perform(delete("/enderecos/1"))
                .andExpect(status().isNoContent());
        verify(enderecoUseCase, times(1)).deletar(1);
    }
}


