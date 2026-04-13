package br.com.caioogdev.modules.audit.repositories;

import br.com.caioogdev.modules.audit.models.Organizacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganizacaoRepository extends JpaRepository<Organizacao, Long> {
    Optional<Organizacao> findByNome(String nome);
}
