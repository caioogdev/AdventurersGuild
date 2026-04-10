package br.com.caioogdev.modules.operacoes.repositories;

import br.com.caioogdev.modules.operacoes.models.PainelTaticoMissao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PainelTaticoMissaoRepository extends JpaRepository<PainelTaticoMissao, Long> {

    @Query("""
        SELECT p FROM PainelTaticoMissao p
        WHERE p.ultimaAtualizacao >= :dataLimite
        ORDER BY p.indiceProntidao DESC
        LIMIT 10
    """)
    List<PainelTaticoMissao> findTop10UltimosQuinzeDias(@Param("dataLimite") LocalDateTime dataLimite);
}

