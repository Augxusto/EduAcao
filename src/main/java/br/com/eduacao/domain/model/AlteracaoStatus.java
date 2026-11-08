package br.com.eduacao.domain.model;

import br.com.eduacao.domain.enums.StatusProblema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AlteracaoStatus {
    private StatusProblema statusAnterior;
    private StatusProblema novoStatus;
    private Instant alteradoEm;
}
