package br.com.eduacao.domain.service;

import br.com.eduacao.domain.model.Escola;
import br.com.eduacao.domain.repository.EscolaRepository;
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
class EscolaServiceTest {

    @Mock
    private EscolaRepository escolaRepository;

    @InjectMocks
    private EscolaService escolaService;

    private Escola escola;

    @BeforeEach
    void setUp() {
        escola = new Escola();
        escola.setNome("Gastão Vidigal");
    }

    @Test
    void deveCadastrarEscolaComSucesso() {

        when(escolaRepository.save(any(Escola.class)))
                .thenReturn(escola);

        Escola resultado = escolaService.cadastrar(escola);

        assertNotNull(resultado);
        assertEquals("Gastão Vidigal", resultado.getNome());

        verify(escolaRepository, times(1))
                .save(escola);
    }

    @Test
    void deveListarTodasAsEscolas() {

        when(escolaRepository.findAll())
                .thenReturn(List.of(escola));

        List<Escola> resultado = escolaService.listarTodas();

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());

        verify(escolaRepository, times(1))
                .findAll();
    }

    @Test
    void deveBuscarEscolaPorId() {

        when(escolaRepository.findById("123"))
                .thenReturn(Optional.of(escola));

        Escola resultado = escolaService.buscarPorId("123");

        assertNotNull(resultado);
        assertEquals(
                "Gastão Vidigal",
                resultado.getNome()
        );

        verify(escolaRepository, times(1))
                .findById("123");
    }

    @Test
    void deveLancarExcecaoQuandoEscolaNaoEncontrada() {

        when(escolaRepository.findById("999"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> escolaService.buscarPorId("999")
        );

        assertEquals(
                "Escola não encontrada com o ID: 999",
                exception.getMessage()
        );

        verify(escolaRepository, times(1))
                .findById("999");
    }
}