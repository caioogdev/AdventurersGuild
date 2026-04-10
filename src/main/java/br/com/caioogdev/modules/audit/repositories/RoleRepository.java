package br.com.caioogdev.modules.audit.repositories;

import br.com.caioogdev.modules.audit.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    List<Role> findAllByOrganizacaoId(Long organizacaoId);

    Optional<Role> findByNomeAndOrganizacaoId(String nome, Long organizacaoId);

    @Query("SELECT r FROM Role r JOIN FETCH r.permissions WHERE r.id = :id")
    Optional<Role> findByIdComPermissions(@Param("id") Long id);
}
