package br.com.caioogdev.modules.missao.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class RankingItemRep {
    private Long aventureiroId;
    private String aventureiroNome;
    private Long totalParticipacoes;
    private BigDecimal totalOuro;
    private Long totalMvp;

    public static RankingItemRep from(Object[] row) {
        return new RankingItemRep(
                (Long) row[0],
                (String) row[1],
                (Long) row[2],
                row[3] instanceof BigDecimal ? (BigDecimal) row[3] : new BigDecimal(row[3].toString()),
                ((Number) row[4]).longValue()
        );
    }
}

