package br.com.caioogdev.modules.missao.dto;

import br.com.caioogdev.modules.missao.models.Missao;
import br.com.caioogdev.shared.enums.NivelPerigo;
import br.com.caioogdev.shared.enums.StatusMissao;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class MissaoResumoRep {
    private Long id;
    private String titulo;
    private StatusMissao status;
    private NivelPerigo nivelPerigo;
    private OffsetDateTime createdAt;
    private OffsetDateTime dataInicio;
    private OffsetDateTime dataTermino;

    public static MissaoResumoRep from(Missao m) {
        MissaoResumoRep rep = new MissaoResumoRep();
        rep.setId(m.getId());
        rep.setTitulo(m.getTitulo());
        rep.setStatus(m.getStatus());
        rep.setNivelPerigo(m.getNivelPerigo());
        rep.setCreatedAt(m.getCreatedAt());
        rep.setDataInicio(m.getDataInicio());
        rep.setDataTermino(m.getDataTermino());
        return rep;
    }
}
