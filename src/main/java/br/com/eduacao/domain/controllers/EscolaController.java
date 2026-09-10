package br.com.eduacao.domain.controllers;

import br.com.eduacao.domain.model.Escola;
import br.com.eduacao.domain.service.EscolaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/escolas")
@Tag(name = "Escolas", description = "Cadastro e consulta de escolas")
@RequiredArgsConstructor
public class EscolaController {

    private final EscolaService escolaService;

    @PostMapping
    @Operation(summary = "Cadastrar uma escola")
    public ResponseEntity<Escola> cadastrar(@Valid @RequestBody Escola escola) {
        return ResponseEntity.status(HttpStatus.CREATED).body(escolaService.cadastrar(escola));
    }

    @GetMapping
    @Operation(summary = "Listar todas as escolas")
    public List<Escola> listar() {
        return escolaService.listarTodas();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar uma escola pelo ID")
    public Escola buscarPorId(@PathVariable String id) {
        return escolaService.buscarPorId(id);
    }
}
