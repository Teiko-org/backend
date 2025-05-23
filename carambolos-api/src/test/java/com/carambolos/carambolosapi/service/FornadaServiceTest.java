package com.carambolos.carambolosapi.service;

import com.carambolos.carambolosapi.controller.request.FornadaRequestDTO;
import com.carambolos.carambolosapi.exception.EntidadeNaoEncontradaException;
import com.carambolos.carambolosapi.model.Fornada;
import com.carambolos.carambolosapi.repository.FornadaRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FornadaServiceTest {

    @Mock
    private FornadaRepository fornadaRepository;

    @InjectMocks
    private FornadaService fornadaService;

    private FornadaRequestDTO requestDTO;

    @BeforeEach
    void setup() {
        requestDTO = new FornadaRequestDTO(null, LocalDate.of(2025, 5, 1), LocalDate.of(2025, 5, 7));
    }

    @Test
    @DisplayName("criarFornada deve salvar quando não existir")
    void criarFornadaDeveSalvarQuandoNaoExistir() {
        Fornada savedFornada = new Fornada();
        savedFornada.setId(1);
        savedFornada.setDataInicio(requestDTO.dataInicio());
        savedFornada.setDataFim(requestDTO.dataFim());
        savedFornada.setAtivo(true);

        when(fornadaRepository.save(any(Fornada.class))).thenReturn(savedFornada);

        Fornada result = fornadaService.criarFornada(requestDTO);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(requestDTO.dataInicio(), result.getDataInicio());
        assertEquals(requestDTO.dataFim(), result.getDataFim());

        verify(fornadaRepository, times(1)).save(any(Fornada.class));
    }

    @Test
    @DisplayName("listarFornada deve retornar somente ativas")
    void listarFornadaDeveRetornarSomenteAtivas() {
        Fornada f1 = new Fornada();
        f1.setId(1);
        f1.setAtivo(true);
        Fornada f2 = new Fornada();
        f2.setId(2);
        f2.setAtivo(false);

        when(fornadaRepository.findAll()).thenReturn(List.of(f1, f2));

        List<Fornada> result = fornadaService.listarFornada();

        assertEquals(1, result.size());
        assertTrue(result.get(0).isAtivo());
    }

    @Test
    @DisplayName("buscarFornada deve retornar quando encontrada e ativa")
    void buscarFornadaDeveRetornarFornadaQuandoEncontradaEAtiva() {
        Fornada f = new Fornada();
        f.setId(1);
        f.setAtivo(true);

        when(fornadaRepository.findById(1)).thenReturn(Optional.of(f));

        Fornada result = fornadaService.buscarFornada(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    @Test
    @DisplayName("buscarFornada deve lançar exceção quando não encontrada")
    void buscarFornadaDeveLancarExceptionQuandoNaoEncontrada() {
        when(fornadaRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntidadeNaoEncontradaException.class, () -> fornadaService.buscarFornada(1));
    }

    @Test
    @DisplayName("excluirFornada deve setar ativo como false")
    void excluirFornadaDeveSetarAtivoFalse() {
        Fornada f = new Fornada();
        f.setId(1);
        f.setAtivo(true);

        when(fornadaRepository.findById(1)).thenReturn(Optional.of(f));
        when(fornadaRepository.save(any(Fornada.class))).thenReturn(f);

        fornadaService.excluirFornada(1);

        assertFalse(f.isAtivo());
        verify(fornadaRepository, times(1)).save(f);
    }

    @Test
    @DisplayName("atualizarFornada deve atualizar campos")
    void atualizarFornada_deveAtualizarCampos() {
        Fornada f = new Fornada();
        f.setId(1);
        f.setAtivo(true);
        f.setDataInicio(LocalDate.of(2025,1,1));
        f.setDataFim(LocalDate.of(2025,1,10));

        when(fornadaRepository.findById(1)).thenReturn(Optional.of(f));
        when(fornadaRepository.save(any(Fornada.class))).thenAnswer(i -> i.getArgument(0));

        FornadaRequestDTO updateRequest = new FornadaRequestDTO(null, LocalDate.of(2025,5,1), LocalDate.of(2025,5,7));

        Fornada updated = fornadaService.atualizarFornada(1, updateRequest);

        assertEquals(1, updated.getId());
        assertEquals(updateRequest.dataInicio(), updated.getDataInicio());
        assertEquals(updateRequest.dataFim(), updated.getDataFim());
    }
}
