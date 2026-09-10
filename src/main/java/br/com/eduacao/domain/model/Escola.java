package br.com.eduacao.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "escolas")
@Getter
@Setter
@NoArgsConstructor
public class Escola extends EntidadeAuditavel {

    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String id;

    @NotBlank(message = "O nome da escola é obrigatório")
    @Size(max = 120, message = "O nome da escola deve ter no máximo 120 caracteres")
    @Indexed(unique = true)
    private String nome;

}
