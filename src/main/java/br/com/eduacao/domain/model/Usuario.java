package br.com.eduacao.domain.model;

import br.com.eduacao.domain.enums.PerfilUsuario;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "usuarios")
@Getter
@Setter
@NoArgsConstructor
public class Usuario extends EntidadeAuditavel {

    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String id;

    @NotBlank(message = "O nome do usuário é obrigatório")
    @Size(max = 120, message = "O nome do usuário deve ter no máximo 120 caracteres")
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "O e-mail deve ser válido")
    @Indexed(unique = true)
    private String email;

    @NotNull(message = "O perfil é obrigatório")
    private PerfilUsuario perfil;

    @NotBlank(message = "A escola é obrigatória")
    private String escolaId;
    private boolean ativo = true;

}
