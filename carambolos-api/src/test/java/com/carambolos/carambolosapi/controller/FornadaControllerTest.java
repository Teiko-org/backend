package com.carambolos.carambolosapi.controller;

import com.carambolos.carambolosapi.controller.request.FornadaRequestDTO;
import com.carambolos.carambolosapi.model.Fornada;
import com.carambolos.carambolosapi.repository.FornadaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FornadaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FornadaRepository fornadaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        fornadaRepository.deleteAll();
    }

    @Test
    void deveCriarFornadaComSucesso() throws Exception {
        FornadaRequestDTO dto = new FornadaRequestDTO(null, LocalDate.now(), LocalDate.now().plusDays(3));

        mockMvc.perform(post("/fornadas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.dataInicio").value(dto.dataInicio().toString()))
                .andExpect(jsonPath("$.dataFim").value(dto.dataFim().toString()));
    }

    @Test
    void deveListarFornadas() throws Exception {
        Fornada fornada = new Fornada();
        fornada.setDataInicio(LocalDate.now());
        fornada.setDataFim(LocalDate.now().plusDays(2));
        fornada.setAtivo(true);
        fornadaRepository.save(fornada);

        mockMvc.perform(get("/fornadas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(fornada.getId()));
    }

    @Test
    void deveBuscarFornadaPorId() throws Exception {
        Fornada fornada = new Fornada();
        fornada.setDataInicio(LocalDate.now());
        fornada.setDataFim(LocalDate.now().plusDays(1));
        fornada.setAtivo(true);
        fornada = fornadaRepository.save(fornada);

        mockMvc.perform(get("/fornadas/{id}", fornada.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(fornada.getId()));
    }

    @Test
    void deveAtualizarFornada() throws Exception {
        Fornada fornada = new Fornada();
        fornada.setDataInicio(LocalDate.now());
        fornada.setDataFim(LocalDate.now().plusDays(1));
        fornada.setAtivo(true);
        fornada = fornadaRepository.save(fornada);

        FornadaRequestDTO dtoAtualizado = new FornadaRequestDTO(null, LocalDate.now().minusDays(1), LocalDate.now());

        mockMvc.perform(put("/fornadas/{id}", fornada.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dataInicio").value(dtoAtualizado.dataInicio().toString()));
    }

    @Test
    void deveExcluirFornada() throws Exception {
        Fornada fornada = new Fornada();
        fornada.setDataInicio(LocalDate.now());
        fornada.setDataFim(LocalDate.now().plusDays(1));
        fornada.setAtivo(true);
        fornada = fornadaRepository.save(fornada);

        mockMvc.perform(delete("/fornadas/{id}", fornada.getId()))
                .andExpect(status().isNoContent());

        Fornada desativada = fornadaRepository.findById(fornada.getId()).get();
        assertThat(desativada.isAtivo()).isFalse();
    }
}
