package br.com.eduacao.domain.controllers;

import br.com.eduacao.domain.enums.CategoriaProblema;
import br.com.eduacao.domain.enums.PerfilUsuario;
import br.com.eduacao.domain.enums.StatusProblema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OpcaoControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        OpcaoController opcaoController = new OpcaoController();

        mockMvc = MockMvcBuilders
                .standaloneSetup(opcaoController)
                .build();
    }

    @Test
    void deveListarCategorias() throws Exception {
        mockMvc.perform(get("/api/opcoes/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(
                        CategoriaProblema.values().length
                )));
    }

    @Test
    void deveListarStatus() throws Exception {
        mockMvc.perform(get("/api/opcoes/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(
                        StatusProblema.values().length
                )));
    }

    @Test
    void deveListarPerfis() throws Exception {
        mockMvc.perform(get("/api/opcoes/perfis"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(
                        PerfilUsuario.values().length
                )));
    }
}