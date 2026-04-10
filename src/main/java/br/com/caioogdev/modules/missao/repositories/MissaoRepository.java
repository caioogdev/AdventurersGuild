package br.com.caioogdev.modules.missao.repositories;

import br.com.caioogdev.modules.missao.models.Missao;
import br.com.caioogdev.shared.enums.NivelPerigo;
import br.com.caioogdev.shared.enums.StatusMissao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;

@Repository
public interface MissaoRepository extends JpaRepository<Missao, Long> {

    @Query("""
        SELECT m FROM Missao m
        WHERE (:status IS NULL OR m.status = :status)
        AND (:nivelPerigo IS NULL OR m.nivelPerigo = :nivelPerigo)
        AND (cast(:dataInicio as java.time.OffsetDateTime) IS NULL OR m.createdAt >= :dataInicio)
        AND (cast(:dataFim as java.time.OffsetDateTime) IS NULL OR m.createdAt <= :dataFim)
    """)
    Page<Missao> listarComFiltros(
            @Param("status") StatusMissao status,
            @Param("nivelPerigo") NivelPerigo nivelPerigo,
            @Param("dataInicio") OffsetDateTime dataInicio,
            @Param("dataFim") OffsetDateTime dataFim,
            Pageable pageable
    );

    @Query("""
        SELECT m FROM Missao m
        LEFT JOIN FETCH m.participacoes p
        LEFT JOIN FETCH p.aventureiro
        WHERE m.id = :id
    """)
    Optional<Missao> buscarComParticipantes(@Param("id") Long id);
}
