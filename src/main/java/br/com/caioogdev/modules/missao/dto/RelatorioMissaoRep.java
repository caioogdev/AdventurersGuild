package br.com.caioogdev.modules.missao.dto;

import br.com.caioogdev.shared.enums.NivelPerigo;
import br.com.caioogdev.shared.enums.StatusMissao;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class RelatorioMissaoRep {
    private Long missaoId;
    private String titulo;
    private StatusMissao status;
    private NivelPerigo nivelPerigo;
    private Long totalParticipantes;
    private BigDecimal totalRecompensas;

    public static RelatorioMissaoRep from(Object[] row) {
        return new RelatorioMissaoRep(
                (Long) row[0],
                (String) row[1],
                (StatusMissao) row[2],
                (NivelPerigo) row[3],
                (Long) row[4],
                row[5] instanceof BigDecimal ? (BigDecimal) row[5] : new BigDecimal(row[5].toString())
        );
    }
}

