package br.com.eduacao.domain.service;

import br.com.eduacao.domain.enums.PerfilUsuario;
import br.com.eduacao.domain.model.Usuario;
import br.com.eduacao.domain.repository.EscolaRepository;
import br.com.eduacao.domain.repository.UsuarioRepository;
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
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private EscolaRepository escolaRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {

        usuario = new Usuario();

        usuario.setNome("Gustavo Cardoso");
        usuario.setEmail("gustavogamer@gmail.com");
        usuario.setPerfil(PerfilUsuario.PROFESSOR);
        usuario.setEscolaId("escola123");
    }

    @Test
    void deveCadastrarUsuarioComSucesso() {

        when(escolaRepository.existsById("escola123"))
                .thenReturn(true);

        when(usuarioRepository.save(any(Usuario.class)))
                .thenReturn(usuario);

        Usuario resultado = usuarioService.cadastrar(usuario);

        assertNotNull(resultado);
        assertEquals("Gustavo Cardoso", resultado.getNome());
        assertEquals("gustavogamer@gmail.com", resultado.getEmail());

        verify(escolaRepository, times(1))
                .existsById("escola123");

        verify(usuarioRepository, times(1))
                .save(usuario);
    }

    @Test
    void deveLancarExcecaoQuandoEscolaNaoEncontrada() {

        when(escolaRepository.existsById("escola123"))
                .thenReturn(false);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> usuarioService.cadastrar(usuario)
        );

        assertEquals(
                "Escola não encontrada com o ID: escola123",
                exception.getMessage()
        );

        verify(escolaRepository, times(1))
                .existsById("escola123");

        verify(usuarioRepository, times(0))
                .save(any(Usuario.class));
    }

    @Test
    void deveListarTodosOsUsuarios() {

        when(usuarioRepository.findAll())
                .thenReturn(List.of(usuario));

        List<Usuario> resultado = usuarioService.listarTodos();

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Gustavo Cardoso", resultado.get(0).getNome());

        verify(usuarioRepository, times(1))
                .findAll();
    }

    @Test
    void deveBuscarUsuarioPorId() {

        when(usuarioRepository.findById("123"))
                .thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.buscarPorId("123");

        assertNotNull(resultado);
        assertEquals("Gustavo Cardoso", resultado.getNome());
        assertEquals("gustavogamer@gmail.com", resultado.getEmail());

        verify(usuarioRepository, times(1))
                .findById("123");
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {

        when(usuarioRepository.findById("999"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> usuarioService.buscarPorId("999")
        );

        assertEquals(
                "Usuário não encontrado com o ID: 999",
                exception.getMessage()
        );

        verify(usuarioRepository, times(1))
                .findById("999");
    }
}