package br.com.eduacao.domain.controllers;

import br.com.eduacao.domain.enums.CategoriaProblema;
import br.com.eduacao.domain.enums.PrioridadeProblema;
import br.com.eduacao.domain.enums.StatusProblema;
import br.com.eduacao.domain.model.Problema;
import br.com.eduacao.domain.service.ProblemaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ProblemaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProblemaService problemaService;

    @InjectMocks
    private ProblemaController problemaController;

    private ObjectMapper objectMapper;

    private Problema problema;

    @BeforeEach
    void setUp() {

        objectMapper = new ObjectMapper();

        mockMvc = MockMvcBuilders
                .standaloneSetup(problemaController)
                .build();

        problema = new Problema();

        problema.setTitulo("Sem conexão no laboratório");
        problema.setDescricao("Os computadores do laboratório estão sem conexão com a internet.");
        problema.setCategoria(CategoriaProblema.TECNOLOGIA);
        problema.setPrioridade(PrioridadeProblema.ALTA);
        problema.setStatus(StatusProblema.ABERTO);
        problema.setQuantidadePessoasAfetadas(10);
        problema.setUsuarioId("usuario123");
        problema.setLocal("Laboratório de informática");
        problema.setEscolaId("escola123");
    }

    @Test
    void deveCadastrarProblemaComSucesso() throws Exception {
        when(problemaService.cadastrar(any(Problema.class)))
                .thenReturn(problema);

        mockMvc.perform(
                        post("/api/problemas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(problema))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo")
                        .value("Sem conexão no laboratório"))
                .andExpect(jsonPath("$.categoria")
                        .value("TECNOLOGIA"))
                .andExpect(jsonPath("$.prioridade")
                        .value("ALTA"))
                .andExpect(jsonPath("$.status")
                        .value("ABERTO"));

        verify(problemaService).cadastrar(any(Problema.class));
    }

    @Test
    void deveListarTodosOsProblemas() throws Exception {

        when(problemaService.listarTodos())
                .thenReturn(List.of(problema));

        mockMvc.perform(
                        get("/api/problemas")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].titulo")
                        .value("Sem conexão no laboratório"));

        verify(problemaService).listarTodos();
    }

    @Test
    void deveBuscarProblemaPorId() throws Exception {

        when(problemaService.buscarPorId("123"))
                .thenReturn(problema);

        mockMvc.perform(
                        get("/api/problemas/123")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo")
                        .value("Sem conexão no laboratório"))
                .andExpect(jsonPath("$.status")
                        .value("ABERTO"));

        verify(problemaService).buscarPorId("123");
    }

    @Test
    void deveAtualizarStatusDoProblema() throws Exception {

        problema.setStatus(StatusProblema.EM_ANALISE);

        when(problemaService.atualizarStatus(
                eq("123"),
                eq(StatusProblema.EM_ANALISE)
        )).thenReturn(problema);

        mockMvc.perform(
                        patch("/api/problemas/123/status")
                                .param("novoStatus", "EM_ANALISE")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status")
                        .value("EM_ANALISE"));

        verify(problemaService)
                .atualizarStatus("123", StatusProblema.EM_ANALISE);
    }
}
