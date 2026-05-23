package com.carambolos.carambolosapi.application.usecases;

import com.carambolos.carambolosapi.application.exception.EntidadeImprocessavelException;
import com.carambolos.carambolosapi.application.gateways.FornadaGateway;
import com.carambolos.carambolosapi.domain.entity.Fornada;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FornadasUseCasesTest {

    @Mock
    private FornadaGateway gateway;

    private FornadasUseCases useCases;

    @BeforeEach
    void setUp() {
        useCases = new FornadasUseCases(gateway);
    }

    @Test
    void criar_bloqueia_quando_ja_existe_fornada_ativa() {
        var ativa = new Fornada(LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 7), true);
        ativa.setId(11);
        when(gateway.findAllAtivas()).thenReturn(List.of(ativa));

        var ex = assertThrows(
            EntidadeImprocessavelException.class,
            () -> useCases.criar(null, LocalDate.of(2026, 6, 10), LocalDate.of(2026, 6, 16))
        );

        assertEquals(
            "Ja existe uma fornada ativa (#11). Encerre-a antes de criar outra.",
            ex.getMessage()
        );
        verify(gateway, never()).save(any());
    }

    @Test
    void criar_permite_quando_nao_ha_fornada_ativa() {
        when(gateway.findAllAtivas()).thenReturn(List.of());
        var salva = new Fornada(LocalDate.of(2026, 6, 10), LocalDate.of(2026, 6, 16), true);
        salva.setId(12);
        when(gateway.save(any(Fornada.class))).thenReturn(salva);

        var result = useCases.criar(null, LocalDate.of(2026, 6, 10), LocalDate.of(2026, 6, 16));

        assertEquals(12, result.getId());
        verify(gateway).save(any(Fornada.class));
    }
}
