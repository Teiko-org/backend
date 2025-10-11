package com.carambolos.carambolosapi.infrastructure.web.controllers;

import com.carambolos.carambolosapi.application.usecases.EnderecoUseCase;
import com.carambolos.carambolosapi.domain.entity.Endereco;
import com.carambolos.carambolosapi.infrastructure.web.request.EnderecoRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EnderecoController.class)
class EnderecoControllerWebMvcTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private EnderecoUseCase enderecoUseCase;

    @Test
    void listar_quandoVazio_204() throws Exception {
        when(enderecoUseCase.listar()).thenReturn(List.of());
        mockMvc.perform(get("/enderecos"))
                .andExpect(status().isNoContent());
    }

    @Test
    void listar_quandoTemDados_200() throws Exception {
        when(enderecoUseCase.listar()).thenReturn(List.of(new Endereco()));
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
        EnderecoRequestDTO req = new EnderecoRequestDTO("Rua A", 123, "Compl", "Bairro", "Cidade", "Estado", "00000-000");
        when(enderecoUseCase.cadastrar(any(Endereco.class))).thenReturn(new Endereco());
        mockMvc.perform(post("/enderecos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());
    }

    @Test
    void atualizar_200() throws Exception {
        EnderecoRequestDTO req = new EnderecoRequestDTO("Rua B", 456, "Compl", "Bairro", "Cidade", "Estado", "11111-111");
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


