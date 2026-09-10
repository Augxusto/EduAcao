package br.com.eduacao.domain.model;

import br.com.eduacao.domain.enums.CategoriaProblema;
import br.com.eduacao.domain.enums.PrioridadeProblema;
import br.com.eduacao.domain.enums.StatusProblema;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "problemas")
@CompoundIndex(
        name = "idx_problema_filtros",
        def = "{'escolaId': 1, 'status': 1, 'categoria': 1, 'prioridade': 1}"
)
@Getter
@Setter
@NoArgsConstructor
public class Problema extends EntidadeAuditavel {

    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String id;

    @NotBlank(message = "O título é obrigatório")
    @Size(max = 120, message = "O título deve ter no máximo 120 caracteres")
    private String titulo;

    @NotBlank(message = "A descrição é obrigatória")
    @Size(max = 2000, message = "A descrição deve ter no máximo 2000 caracteres")
    private String descricao;

    @NotNull(message = "A categoria é obrigatória")
    private CategoriaProblema categoria;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            description = "Calculada automaticamente conforme a categoria e a quantidade de pessoas afetadas"
    )
    private PrioridadeProblema prioridade;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, defaultValue = "ABERTO")
    private StatusProblema status = StatusProblema.ABERTO;

    @NotBlank(message = "O local é obrigatório")
    @Size(max = 120, message = "O local deve ter no máximo 120 caracteres")
    private String local;

    @Size(max = 120, message = "A descrição da turma deve ter no máximo 120 caracteres")
    private String turma;

    @NotBlank(message = "O usuário é obrigatório")
    private String usuarioId;

    @NotBlank(message = "A escola é obrigatória")
    private String escolaId;

    @NotNull(message = "A quantidade de pessoas afetadas é obrigatória")
    @Min(value = 1, message = "A quantidade de pessoas afetadas deve ser no mínimo 1")
    private Integer quantidadePessoasAfetadas;

}
