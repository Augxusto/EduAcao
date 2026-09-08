package br.com.eduacao.domain.service;

import br.com.eduacao.domain.enums.StatusProblema;
import br.com.eduacao.domain.model.Problema;
import br.com.eduacao.domain.repository.ProblemaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProblemaService {

    private final ProblemaRepository problemaRepository;

    public ProblemaService(ProblemaRepository problemaRepository) {
        this.problemaRepository = problemaRepository;
    }

    public Problema cadastrar(Problema problema) {
        if (problema.getStatus() == null) {
            problema.setStatus(StatusProblema.ABERTO);
        }
        return problemaRepository.save(problema);
    }

    public List<Problema> listarTodos() {
        return problemaRepository.findAll();
    }

    public Problema buscarPorId(String id) {
        return problemaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Problema não encontrado com o ID: " + id));
    }

    public Problema atualizarStatus(String id, StatusProblema novoStatus) {
        Problema problema = buscarPorId(id);
        problema.setStatus(novoStatus);
        return problemaRepository.save(problema);
    }
}