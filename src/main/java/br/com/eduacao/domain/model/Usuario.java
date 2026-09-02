package br.com.eduacao.domain.model;

import br.com.eduacao.domain.enums.PerfilUsuario;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "usuarios")
public class Usuario extends EntidadeAuditavel {

    @Id
    private String id;

    private String nome;

    @Indexed(unique = true)
    private String email;

    private String senhaHash;
    private PerfilUsuario perfil;
    private String escolaId;
    private boolean ativo = true;

    public Usuario() {
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String valor) {
        nome = valor;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String valor) {
        email = valor;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public void setSenhaHash(String valor) {
        senhaHash = valor;
    }

    public PerfilUsuario getPerfil() {
        return perfil;
    }

    public void setPerfil(PerfilUsuario valor) {
        perfil = valor;
    }

    public String getEscolaId() {
        return escolaId;
    }

    public void setEscolaId(String valor) {
        escolaId = valor;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean valor) {
        ativo = valor;
    }
}
