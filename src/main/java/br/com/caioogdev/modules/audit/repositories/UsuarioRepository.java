package br.com.caioogdev.modules.audit.repositories;

import br.com.caioogdev.modules.audit.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmailAndOrganizacaoId(String email, Long organizacaoId);

    List<Usuario> findAllByOrganizacaoId(Long organizacaoId);

    @Query("SELECT DISTINCT u FROM Usuario u JOIN FETCH u.roles WHERE u.id = :id")
    Optional<Usuario> findByIdComRolesEPermissions(@Param("id") Long id);
}
