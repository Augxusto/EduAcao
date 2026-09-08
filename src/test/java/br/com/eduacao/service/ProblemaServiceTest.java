package br.com.eduacao.service;

import br.com.eduacao.domain.enums.CategoriaProblema;
import br.com.eduacao.domain.enums.PrioridadeProblema;
import br.com.eduacao.domain.enums.StatusProblema;
import br.com.eduacao.domain.model.Problema;
import br.com.eduacao.domain.repository.ProblemaRepository;
import br.com.eduacao.domain.service.ProblemaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProblemaServiceTest {

    @Mock
    private ProblemaRepository problemaRepository;

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
    }

    @Test
    void deveCadastrarProblemaComSucesso() {
        when(problemaRepository.save(any(Problema.class))).thenReturn(problema);

        Problema resultado = problemaService.cadastrar(problema);

        assertNotNull(resultado);
        assertEquals("Sem conexão no laboratório", resultado.getTitulo());
        verify(problemaRepository, times(1)).save(problema);
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
        when(problemaRepository.findById("123")).thenReturn(Optional.of(problema));
        when(problemaRepository.save(any(Problema.class))).thenReturn(problema);

        Problema resultado = problemaService.atualizarStatus("123", StatusProblema.EM_ANALISE);

        assertNotNull(resultado);
        assertEquals(StatusProblema.EM_ANALISE, resultado.getStatus());
    }
}