package br.com.caioogdev.modules.missao.repositories;

import br.com.caioogdev.modules.missao.models.ParticipacaoMissao;
import br.com.caioogdev.modules.missao.models.ParticipacaoMissaoId;
import br.com.caioogdev.shared.enums.StatusMissao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface ParticipacaoMissaoRepository extends JpaRepository<ParticipacaoMissao, ParticipacaoMissaoId> {

    @Query("""
        SELECT p.aventureiro.id,
               p.aventureiro.nome,
               COUNT(p) AS totalParticipacoes,
               COALESCE(SUM(p.recompensaOuro), 0) AS totalOuro,
               SUM(CASE WHEN p.mvp = true THEN 1 ELSE 0 END) AS totalMvp
        FROM ParticipacaoMissao p
        WHERE (cast(:dataInicio as java.time.OffsetDateTime) IS NULL OR p.dataRegistro >= :dataInicio)
        AND (cast(:dataFim as java.time.OffsetDateTime) IS NULL OR p.dataRegistro <= :dataFim)
        AND (:statusMissao IS NULL OR p.missao.status = :statusMissao)
        GROUP BY p.aventureiro.id, p.aventureiro.nome
        ORDER BY totalParticipacoes DESC
    """)
    List<Object[]> ranking(
            @Param("dataInicio") OffsetDateTime dataInicio,
            @Param("dataFim") OffsetDateTime dataFim,
            @Param("statusMissao") StatusMissao statusMissao
    );

    @Query("""
        SELECT p.missao.id,
               p.missao.titulo,
               p.missao.status,
               p.missao.nivelPerigo,
               COUNT(p) AS totalParticipantes,
               COALESCE(SUM(p.recompensaOuro), 0) AS totalRecompensas
        FROM ParticipacaoMissao p
        WHERE (cast(:dataInicio as java.time.OffsetDateTime) IS NULL OR p.missao.createdAt >= :dataInicio)
        AND (cast(:dataFim as java.time.OffsetDateTime) IS NULL OR p.missao.createdAt <= :dataFim)
        GROUP BY p.missao.id, p.missao.titulo, p.missao.status, p.missao.nivelPerigo
    """)
    List<Object[]> relatorioMissoes(
            @Param("dataInicio") OffsetDateTime dataInicio,
            @Param("dataFim") OffsetDateTime dataFim
    );

    @Query("SELECT COUNT(p) FROM ParticipacaoMissao p WHERE p.aventureiro.id = :aventureiroId")
    long contarPorAventureiro(@Param("aventureiroId") Long aventureiroId);

    @Query("""
        SELECT p.missao FROM ParticipacaoMissao p
        WHERE p.aventureiro.id = :aventureiroId
        ORDER BY p.dataRegistro DESC
        LIMIT 1
    """)
    java.util.Optional<br.com.caioogdev.modules.missao.models.Missao> ultimaMissao(
            @Param("aventureiroId") Long aventureiroId
    );
}
