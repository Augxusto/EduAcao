package br.com.eduacao.domain.model;

import br.com.eduacao.domain.enums.StatusProblema;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "historicos_problema")
@CompoundIndex(
        name = "idx_historico_problema_data",
        def = "{'problemaId': 1, 'data': -1}"
)
@Getter
@Setter
@NoArgsConstructor
public class HistoricoProblema {

    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String id;

    private String problemaId;
    private StatusProblema statusAnterior;
    private StatusProblema novoStatus;
    private String observacao;
    private String usuarioId;

    @CreatedDate
    private Instant data;

}
