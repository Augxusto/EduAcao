package br.com.eduacao.domain.service;

import br.com.eduacao.domain.model.Usuario;
import br.com.eduacao.domain.repository.EscolaRepository;
import br.com.eduacao.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final EscolaRepository escolaRepository;

    public Usuario cadastrar(Usuario usuario) {
        if (!escolaRepository.existsById(usuario.getEscolaId())) {
            throw new IllegalArgumentException("Escola não encontrada com o ID: " + usuario.getEscolaId());
        }
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(String id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com o ID: " + id));
    }
}
