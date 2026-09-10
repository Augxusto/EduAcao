package br.com.eduacao.domain.controllers;

import br.com.eduacao.domain.model.Usuario;
import br.com.eduacao.domain.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuários", description = "Cadastro e consulta de usuários")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    @Operation(summary = "Cadastrar um usuário")
    public ResponseEntity<Usuario> cadastrar(@Valid @RequestBody Usuario usuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.cadastrar(usuario));
    }

    @GetMapping
    @Operation(summary = "Listar todos os usuários")
    public List<Usuario> listar() {
        return usuarioService.listarTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar um usuário pelo ID")
    public Usuario buscarPorId(@PathVariable String id) {
        return usuarioService.buscarPorId(id);
    }
}
