package br.com.eduacao.domain.service;

import br.com.eduacao.domain.model.Escola;
import br.com.eduacao.domain.repository.EscolaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EscolaService {

    private final EscolaRepository escolaRepository;

    public Escola cadastrar(Escola escola) {
        return escolaRepository.save(escola);
    }

    public List<Escola> listarTodas() {
        return escolaRepository.findAll();
    }

    public Escola buscarPorId(String id) {
        return escolaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Escola não encontrada com o ID: " + id));
    }
}
