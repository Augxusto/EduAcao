package br.com.eduacao.domain.controllers;

import br.com.eduacao.domain.enums.CategoriaProblema;
import br.com.eduacao.domain.enums.PerfilUsuario;
import br.com.eduacao.domain.enums.PrioridadeProblema;
import br.com.eduacao.domain.enums.StatusProblema;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/opcoes")
@Tag(
        name = "Opções",
        description = "Valores aceitos nos campos de cadastro da API"
)
public class OpcaoController {

    @GetMapping("/categorias")
    @Operation(summary = "Listar as categorias de problema disponíveis")
    public List<CategoriaProblema> listarCategorias() {
        return List.of(CategoriaProblema.values());
    }

    @GetMapping("/prioridades")
    @Operation(summary = "Listar as prioridades disponíveis")
    public List<PrioridadeProblema> listarPrioridades() {
        return List.of(PrioridadeProblema.values());
    }

    @GetMapping("/status")
    @Operation(summary = "Listar os status disponíveis")
    public List<StatusProblema> listarStatus() {
        return List.of(StatusProblema.values());
    }

    @GetMapping("/perfis")
    @Operation(summary = "Listar os perfis de usuário disponíveis")
    public List<PerfilUsuario> listarPerfis() {
        return List.of(PerfilUsuario.values());
    }
}
