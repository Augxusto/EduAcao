package br.com.eduacao.domain.service;

import br.com.eduacao.domain.enums.CategoriaProblema;
import br.com.eduacao.domain.enums.PrioridadeProblema;
import br.com.eduacao.domain.enums.StatusProblema;
import br.com.eduacao.domain.model.AlteracaoStatus;
import br.com.eduacao.domain.model.Problema;
import br.com.eduacao.domain.repository.EscolaRepository;
import br.com.eduacao.domain.repository.ProblemaRepository;
import br.com.eduacao.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProblemaService {

    private final ProblemaRepository problemaRepository;
    private final EscolaRepository escolaRepository;
    private final UsuarioRepository usuarioRepository;

    public Problema cadastrar(Problema problema) {
        validarReferencias(problema);
        problema.setStatus(StatusProblema.ABERTO);
        problema.setHistoricoStatus(new ArrayList<>());
        problema.setPrioridade(calcularPrioridade(problema));
        return problemaRepository.save(problema);
    }

    private PrioridadeProblema calcularPrioridade(Problema problema) {
        int quantidade = problema.getQuantidadePessoasAfetadas();
        PrioridadeProblema prioridade;

        if (quantidade >= 100) {
            prioridade = PrioridadeProblema.CRITICA;
        } else if (quantidade >= 30) {
            prioridade = PrioridadeProblema.ALTA;
        } else if (quantidade >= 10) {
            prioridade = PrioridadeProblema.MEDIA;
        } else {
            prioridade = PrioridadeProblema.BAIXA;
        }

        if (problema.getCategoria() == CategoriaProblema.ACESSIBILIDADE
                || problema.getCategoria() == CategoriaProblema.INFRAESTRUTURA) {
            prioridade = aumentarPrioridade(prioridade);
        }

        return prioridade;
    }

    private PrioridadeProblema aumentarPrioridade(PrioridadeProblema prioridade) {
        return switch (prioridade) {
            case BAIXA -> PrioridadeProblema.MEDIA;
            case MEDIA -> PrioridadeProblema.ALTA;
            case ALTA, CRITICA -> PrioridadeProblema.CRITICA;
        };
    }

    private void validarReferencias(Problema problema) {
        if (!escolaRepository.existsById(problema.getEscolaId())) {
            throw new IllegalArgumentException("Escola não encontrada com o ID: " + problema.getEscolaId());
        }

        if (!usuarioRepository.existsById(problema.getUsuarioId())) {
            throw new IllegalArgumentException("Usuário não encontrado com o ID: " + problema.getUsuarioId());
        }
    }

    public List<Problema> listarTodos() {
        return problemaRepository.findAll();
    }

    public Problema buscarPorId(String id) {
        return problemaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Problema não encontrado com o ID: " + id));
    }

    public Problema atualizarStatus(String id, StatusProblema novoStatus) {
        if (novoStatus == null) {
            throw new IllegalArgumentException("O novo status é obrigatório");
        }
        Problema problema = buscarPorId(id);
        if (problema.getStatus() == novoStatus) {
            return problema;
        }
        if (problema.getHistoricoStatus() == null) {
            problema.setHistoricoStatus(new ArrayList<>());
        }
        problema.getHistoricoStatus().add(new AlteracaoStatus(
                problema.getStatus(), novoStatus, Instant.now()));
        problema.setStatus(novoStatus);
        return problemaRepository.save(problema);
    }
}
