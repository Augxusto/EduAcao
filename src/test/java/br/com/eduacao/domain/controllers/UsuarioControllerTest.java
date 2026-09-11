package br.com.eduacao.domain.controllers;

import br.com.eduacao.domain.enums.PerfilUsuario;
import br.com.eduacao.domain.model.Usuario;
import br.com.eduacao.domain.service.UsuarioService;
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
class UsuarioControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    private ObjectMapper objectMapper;

    private Usuario usuario;

    @BeforeEach
    void setUp() {

        objectMapper = new ObjectMapper();

        mockMvc = MockMvcBuilders
                .standaloneSetup(usuarioController)
                .build();

        usuario = new Usuario();

        usuario.setNome("Gustavo Cardoso");
        usuario.setEmail("gustavogamer@gmail.com");
        usuario.setPerfil(PerfilUsuario.PROFESSOR);
        usuario.setEscolaId("escola123");
    }

    @Test
    void deveCadastrarUsuarioComSucesso() throws Exception {

        when(usuarioService.cadastrar(any(Usuario.class)))
                .thenReturn(usuario);

        mockMvc.perform(
                        post("/api/usuarios")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(usuario))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome")
                        .value("Gustavo Cardoso"))
                .andExpect(jsonPath("$.email")
                        .value("gustavogamer@gmail.com"))
                .andExpect(jsonPath("$.perfil")
                        .value("PROFESSOR"))
                .andExpect(jsonPath("$.escolaId")
                        .value("escola123"))
                .andExpect(jsonPath("$.ativo")
                        .value(true));

        verify(usuarioService)
                .cadastrar(any(Usuario.class));
    }

    @Test
    void deveListarTodosOsUsuarios() throws Exception {

        when(usuarioService.listarTodos())
                .thenReturn(List.of(usuario));

        mockMvc.perform(
                        get("/api/usuarios")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nome")
                        .value("Gustavo Cardoso"))
                .andExpect(jsonPath("$[0].email")
                        .value("gustavogamer@gmail.com"));

        verify(usuarioService)
                .listarTodos();
    }

    @Test
    void deveBuscarUsuarioPorId() throws Exception {

        when(usuarioService.buscarPorId("123"))
                .thenReturn(usuario);

        mockMvc.perform(
                        get("/api/usuarios/123")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome")
                        .value("Gustavo Cardoso"))
                .andExpect(jsonPath("$.email")
                        .value("gustavogamer@gmail.com"));

        verify(usuarioService)
                .buscarPorId("123");
    }
}