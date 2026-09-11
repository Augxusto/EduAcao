package br.com.eduacao.domain.controllers;

import br.com.eduacao.domain.model.Escola;
import br.com.eduacao.domain.service.EscolaService;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class EscolaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EscolaService escolaService;

    @InjectMocks
    private EscolaController escolaController;

    private ObjectMapper objectMapper;

    private Escola escola;

    @BeforeEach
    void setUp() {

        objectMapper = new ObjectMapper();

        mockMvc = MockMvcBuilders
                .standaloneSetup(escolaController)
                .build();

        escola = new Escola();

        escola.setNome("Gastão Vidigal");
    }

    @Test
    void deveCadastrarEscolaComSucesso() throws Exception {

        when(escolaService.cadastrar(any(Escola.class)))
                .thenReturn(escola);

        mockMvc.perform(
                        post("/api/escolas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(escola))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome")
                        .value("Gastão Vidigal"));

        verify(escolaService).cadastrar(any(Escola.class));
    }

    @Test
    void deveListarTodasAsEscolas() throws Exception {

        when(escolaService.listarTodas())
                .thenReturn(List.of(escola));

        mockMvc.perform(
                        get("/api/escolas")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(escolaService).listarTodas();
    }

    @Test
    void deveBuscarEscolaPorId() throws Exception {

        when(escolaService.buscarPorId("123"))
                .thenReturn(escola);

        mockMvc.perform(
                        get("/api/escolas/123")
                )
                .andExpect(status().isOk());

        verify(escolaService).buscarPorId("123");
    }
}