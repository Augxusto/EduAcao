package br.com.eduacao.domain.controllers;

import br.com.eduacao.domain.enums.StatusProblema;
import br.com.eduacao.domain.model.Problema;
import br.com.eduacao.domain.service.ProblemaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/problemas")
@Tag(
        name = "Problemas",
        description = "Cadastro e acompanhamento de problemas educacionais"
)
public class ProblemaController {

    private final ProblemaService problemaService;

    public ProblemaController(ProblemaService problemaService) {
        this.problemaService = problemaService;
    }

    @PostMapping
    @Operation(summary = "Cadastrar um problema educacional")
    public ResponseEntity<Problema> cadastrar(@Valid @RequestBody Problema problema) {
        Problema salvo = problemaService.cadastrar(problema);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @GetMapping
    @Operation(summary = "Listar todos os problemas")
    public ResponseEntity<List<Problema>> listar() {
        return ResponseEntity.ok(problemaService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar um problema pelo ID")
    public ResponseEntity<Problema> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(problemaService.buscarPorId(id));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar o status de um problema")
    public ResponseEntity<Problema> atualizarStatus(
            @PathVariable String id,
            @RequestParam StatusProblema novoStatus
    ) {
        return ResponseEntity.ok(problemaService.atualizarStatus(id, novoStatus));
    }
}
