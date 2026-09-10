package br.com.eduacao.domain.model;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Getter
public abstract class EntidadeAuditavel {

    @CreatedDate
    private Instant criadoEm;

    @LastModifiedDate
    private Instant atualizadoEm;

}
