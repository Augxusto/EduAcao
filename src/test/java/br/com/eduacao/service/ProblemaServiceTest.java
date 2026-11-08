package br.com.eduacao.service;

import br.com.eduacao.domain.enums.CategoriaProblema;
import br.com.eduacao.domain.enums.PrioridadeProblema;
import br.com.eduacao.domain.enums.StatusProblema;
import br.com.eduacao.domain.model.Problema;
import br.com.eduacao.domain.repository.ProblemaRepository;
import br.com.eduacao.domain.repository.EscolaRepository;
import br.com.eduacao.domain.repository.UsuarioRepository;
import br.com.eduacao.domain.service.ProblemaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProblemaServiceTest {

    @Mock
    private ProblemaRepository problemaRepository;

    @Mock
    private EscolaRepository escolaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ProblemaService problemaService;

    private Problema problema;

    @BeforeEach
    void setUp() {
        problema = new Problema();
        problema.setTitulo("Sem conexão no laboratório");
        problema.setCategoria(CategoriaProblema.TECNOLOGIA);
        problema.setPrioridade(PrioridadeProblema.ALTA);
        problema.setStatus(StatusProblema.ABERTO);
        problema.setQuantidadePessoasAfetadas(30);
        problema.setEscolaId("escola-1");
        problema.setUsuarioId("usuario-1");
        problema.setTurma("6º ano B");
    }

    @Test
    void deveCadastrarProblemaComSucesso() {
        when(escolaRepository.existsById("escola-1")).thenReturn(true);
        when(usuarioRepository.existsById("usuario-1")).thenReturn(true);
        when(problemaRepository.save(any(Problema.class))).thenReturn(problema);

        Problema resultado = problemaService.cadastrar(problema);

        assertNotNull(resultado);
        assertEquals("Sem conexão no laboratório", resultado.getTitulo());
        assertEquals(PrioridadeProblema.ALTA, resultado.getPrioridade());
        assertEquals(List.of(), resultado.getHistoricoStatus());
        verify(problemaRepository, times(1)).save(problema);
    }

    @Test
    void deveAumentarPrioridadeParaCategoriaAgravante() {
        problema.setCategoria(CategoriaProblema.ACESSIBILIDADE);
        problema.setQuantidadePessoasAfetadas(5);
        when(escolaRepository.existsById("escola-1")).thenReturn(true);
        when(usuarioRepository.existsById("usuario-1")).thenReturn(true);
        when(problemaRepository.save(any(Problema.class))).thenReturn(problema);

        Problema resultado = problemaService.cadastrar(problema);

        assertEquals(PrioridadeProblema.MEDIA, resultado.getPrioridade());
    }

    @Test
    void naoDeveCadastrarProblemaComEscolaInexistente() {
        when(escolaRepository.existsById("escola-1")).thenReturn(false);

        IllegalArgumentException erro = assertThrows(
                IllegalArgumentException.class,
                () -> problemaService.cadastrar(problema)
        );

        assertEquals("Escola não encontrada com o ID: escola-1", erro.getMessage());
    }

    @Test
    void deveListarTodosOsProblemas() {
        when(problemaRepository.findAll()).thenReturn(List.of(problema));

        List<Problema> resultado = problemaService.listarTodos();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    @Test
    void deveBuscarProblemaPorId() {
        when(problemaRepository.findById("123")).thenReturn(Optional.of(problema));

        Problema resultado = problemaService.buscarPorId("123");

        assertNotNull(resultado);
        assertEquals(StatusProblema.ABERTO, resultado.getStatus());
    }

    @Test
    void deveLancarExcecaoQuandoProblemaNaoEncontrado() {
        when(problemaRepository.findById("999")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> problemaService.buscarPorId("999"));
    }

    @Test
    void deveAtualizarStatusDoProblema() {
        Instant inicio = Instant.now();
        when(problemaRepository.findById("123")).thenReturn(Optional.of(problema));
        when(problemaRepository.save(any(Problema.class))).thenReturn(problema);

        Problema resultado = problemaService.atualizarStatus("123", StatusProblema.EM_ANALISE);

        assertNotNull(resultado);
        assertEquals(StatusProblema.EM_ANALISE, resultado.getStatus());
        assertEquals(1, resultado.getHistoricoStatus().size());
        var alteracao = resultado.getHistoricoStatus().getFirst();
        assertEquals(StatusProblema.ABERTO, alteracao.getStatusAnterior());
        assertEquals(StatusProblema.EM_ANALISE, alteracao.getNovoStatus());
        assertFalse(alteracao.getAlteradoEm().isBefore(inicio));
        assertFalse(alteracao.getAlteradoEm().isAfter(Instant.now()));
        verify(problemaRepository).save(problema);
    }

    @Test
    void devePreservarOrdemDoHistorico() {
        when(problemaRepository.findById("123")).thenReturn(Optional.of(problema));
        when(problemaRepository.save(problema)).thenReturn(problema);

        problemaService.atualizarStatus("123", StatusProblema.EM_ANALISE);
        problemaService.atualizarStatus("123", StatusProblema.EM_RESOLUCAO);

        assertEquals(2, problema.getHistoricoStatus().size());
        assertEquals(StatusProblema.ABERTO, problema.getHistoricoStatus().get(0).getStatusAnterior());
        assertEquals(StatusProblema.EM_ANALISE, problema.getHistoricoStatus().get(1).getStatusAnterior());
        assertEquals(StatusProblema.EM_RESOLUCAO, problema.getHistoricoStatus().get(1).getNovoStatus());
    }

    @Test
    void naoDeveRegistrarStatusSemAlteracao() {
        when(problemaRepository.findById("123")).thenReturn(Optional.of(problema));

        problemaService.atualizarStatus("123", StatusProblema.ABERTO);

        assertEquals(0, problema.getHistoricoStatus().size());
        verify(problemaRepository, never()).save(any());
    }

    @Test
    void deveAtualizarProblemaAntigoSemHistorico() {
        problema.setHistoricoStatus(null);
        when(problemaRepository.findById("123")).thenReturn(Optional.of(problema));
        when(problemaRepository.save(problema)).thenReturn(problema);

        problemaService.atualizarStatus("123", StatusProblema.RESOLVIDO);

        assertEquals(1, problema.getHistoricoStatus().size());
        assertEquals(StatusProblema.ABERTO, problema.getHistoricoStatus().getFirst().getStatusAnterior());
    }

    @Test
    void naoDeveAceitarStatusNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> problemaService.atualizarStatus("123", null));
        verify(problemaRepository, never()).save(any());
    }
}
