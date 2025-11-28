package com.carambolos.carambolosapi.application.usecases;

import com.carambolos.carambolosapi.domain.entity.ResumoPedido;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.ProdutoFornada;
import com.carambolos.carambolosapi.domain.enums.StatusEnum;
import com.carambolos.carambolosapi.infrastructure.gateways.mapper.EnderecoMapper;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.FornadaDaVez;
import com.carambolos.carambolosapi.infrastructure.persistence.entity.PedidoFornada;
import com.carambolos.carambolosapi.infrastructure.persistence.jpa.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResumoPedidoServiceTest {

    @Mock private ResumoPedidoRepository resumoPedidoRepository;
    @Mock private PedidoBoloRepository pedidoBoloRepository;
    @Mock private PedidoFornadaRepository pedidoFornadaRepository;
    @Mock private FornadaDaVezRepository fornadaDaVezRepository;
    @Mock private ProdutoFornadaRepository produtoFornadaRepository;
    @Mock private BoloRepository boloRepository;
    @Mock private RecheioPedidoRepository recheioPedidoRepository;
    @Mock private RecheioExclusivoRepository recheioExclusivoRepository;
    @Mock private RecheioUnitarioRepository recheioUnitarioRepository;
    @Mock private MassaRepository massaRepository;
    @Mock private CoberturaRepository coberturaRepository;
    @Mock private EnderecoRepository enderecoRepository;
    @Mock private EnderecoMapper enderecoMapper;

    @InjectMocks
    private ResumoPedidoService service;

    @BeforeEach
    void setup() {
    }

    @Test
    void listarResumosPedidos_deveRetornarSomenteAtivos() {
        Pageable pageable = PageRequest.of(0, 10);

        List<ResumoPedido> listaPrimeiraPagina = List.of(new ResumoPedido());
        Page<ResumoPedido> pagina1 = new PageImpl<>(listaPrimeiraPagina, pageable, listaPrimeiraPagina.size());
        Page<ResumoPedido> paginaVazia = new PageImpl<>(List.of(), pageable, 0);

        when(resumoPedidoRepository.findAllByIsAtivoTrue(pageable))
                .thenReturn(pagina1)
                .thenReturn(paginaVazia);

        var page1 = service.listarResumosPedidos(pageable);
        assertEquals(1, page1.getContent().size());

        var page2 = service.listarResumosPedidos(pageable);
        assertEquals(0, page2.getContent().size());

        verify(resumoPedidoRepository, times(2)).findAllByIsAtivoTrue(pageable);
    }

    @Test
    void buscarResumoPedidoPorId_quandoNaoEncontrado_deveLancar() {
        when(resumoPedidoRepository.findByIdAndIsAtivoTrue(123)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.buscarResumoPedidoPorId(123));
    }

    @Test
    void buscarResumosPedidosPorDataPedido_intervaloCompleto() {
        LocalDate data = LocalDate.of(2025, 1, 1);
        when(resumoPedidoRepository.findByDataPedidoBetweenAndIsAtivoTrue(any(), any()))
                .thenReturn(List.of());
        var list = service.buscarResumosPedidosPorDataPedido(data);
        assertNotNull(list);
        verify(resumoPedidoRepository).findByDataPedidoBetweenAndIsAtivoTrue(
                LocalDateTime.of(2025,1,1,0,0,0), LocalDateTime.of(2025,1,1,23,59,59)
        );
    }

    @Test
    void buscarResumosPedidosPorStatus_ok() {
        when(resumoPedidoRepository.findByStatusAndIsAtivoTrue(StatusEnum.PENDENTE))
                .thenReturn(List.of(new ResumoPedido()));
        var list = service.buscarResumosPedidosPorStatus(StatusEnum.PENDENTE);
        assertEquals(1, list.size());
    }

    @Test
    void cadastrarResumoPedido_comPedidoFornada_calculaValorUsandoQuantidade() {
        ResumoPedido input = new ResumoPedido();
        input.setPedidoFornadaId(77);

        PedidoFornada pedidoFornada = new PedidoFornada();
        pedidoFornada.setId(77);
        pedidoFornada.setFornadaDaVez(10);
        pedidoFornada.setQuantidade(4);

        FornadaDaVez fdv = new FornadaDaVez();
        fdv.setId(10);
        fdv.setProdutoFornada(3);

        ProdutoFornada produto = new ProdutoFornada();
        produto.setId(3);
        produto.setProduto("Cookie");
        produto.setValor(5.0);

        when(pedidoFornadaRepository.existsByIdAndIsAtivoTrue(77)).thenReturn(true);
        when(pedidoFornadaRepository.findById(77)).thenReturn(Optional.of(pedidoFornada));
        when(fornadaDaVezRepository.findById(10)).thenReturn(Optional.of(fdv));
        when(produtoFornadaRepository.findById(3)).thenReturn(Optional.of(produto));
        when(resumoPedidoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var saved = service.cadastrarResumoPedido(input);
        assertNotNull(saved);
        assertEquals(20.0, saved.getValor());
        assertEquals(StatusEnum.PENDENTE, saved.getStatus());
        assertNotNull(saved.getDataPedido());
    }

    @Test
    void atualizarResumoPedido_quandoNaoExiste_deveLancar() {
        when(resumoPedidoRepository.existsByIdAndIsAtivoTrue(9)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> service.atualizarResumoPedido(9, new ResumoPedido()));
    }

    @Test
    void deletarResumoPedido_deveMarcarComoInativo() {
        ResumoPedido rp = new ResumoPedido();
        rp.setAtivo(true);
        when(resumoPedidoRepository.findByIdAndIsAtivoTrue(5)).thenReturn(Optional.of(rp));
        when(resumoPedidoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        service.deletarResumoPedido(5);
        assertFalse(rp.getAtivo());
    }

    @Test
    void alterarStatus_quandoCancela_deveReporQuantidadeNaFornadaDaVez() {
        ResumoPedido rp = new ResumoPedido();
        rp.setPedidoFornadaId(77);
        rp.setStatus(StatusEnum.PENDENTE);

        PedidoFornada pedido = new PedidoFornada();
        pedido.setFornadaDaVez(10);
        pedido.setQuantidade(3);

        FornadaDaVez fdv = new FornadaDaVez();
        fdv.setId(10);
        fdv.setQuantidade(7);

        when(resumoPedidoRepository.findByIdAndIsAtivoTrue(1)).thenReturn(Optional.of(rp));
        when(pedidoFornadaRepository.findById(77)).thenReturn(Optional.of(pedido));
        when(fornadaDaVezRepository.findById(10)).thenReturn(Optional.of(fdv));
        when(resumoPedidoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var updated = service.alterarStatus(1, StatusEnum.CANCELADO);
        assertEquals(StatusEnum.CANCELADO, updated.getStatus());
        verify(fornadaDaVezRepository, times(1)).save(argThat(f -> f.getQuantidade() == 10));
    }
}


