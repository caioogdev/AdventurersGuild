package br.com.caioogdev.modules.aventureiro.repositories;

import br.com.caioogdev.modules.aventureiro.models.Aventureiro;
import br.com.caioogdev.shared.enums.Classe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AventureiroRepository extends JpaRepository<Aventureiro, Long> {

    List<Aventureiro> findAllByOrganizacaoId(Long organizacaoId);

    @Query("""
        SELECT a FROM Aventureiro a
        WHERE (:classe IS NULL OR a.classe = :classe)
        AND (:ativo IS NULL OR a.ativo = :ativo)
        AND (:nivelMinimo IS NULL OR a.nivel >= :nivelMinimo)
    """)
    Page<Aventureiro> listarComFiltros(
            @Param("classe") Classe classe,
            @Param("ativo") Boolean ativo,
            @Param("nivelMinimo") Integer nivelMinimo,
            Pageable pageable
    );

    @Query("""
        SELECT a FROM Aventureiro a
        WHERE LOWER(a.nome) LIKE LOWER(CONCAT('%', :nome, '%'))
    """)
    Page<Aventureiro> buscarPorNome(@Param("nome") String nome, Pageable pageable);

    @Query("""
        SELECT a FROM Aventureiro a
        LEFT JOIN FETCH a.companheiro
        WHERE a.id = :id
    """)
    Optional<Aventureiro> buscarPerfilCompleto(@Param("id") Long id);
}
