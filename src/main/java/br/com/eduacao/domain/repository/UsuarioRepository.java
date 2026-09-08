package br.com.eduacao.domain.repository;

import br.com.eduacao.domain.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {

    Optional<Usuario> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);
    List<Usuario> findByEscolaIdAndAtivoTrue(String escolaId);
}
