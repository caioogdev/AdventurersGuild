package br.com.caioogdev.modules.missao.dto;

import br.com.caioogdev.modules.missao.models.Missao;
import br.com.caioogdev.modules.missao.models.ParticipacaoMissao;
import br.com.caioogdev.shared.enums.NivelPerigo;
import br.com.caioogdev.shared.enums.PapelMissao;
import br.com.caioogdev.shared.enums.StatusMissao;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class MissaoDetalheRep {
    private Long id;
    private String titulo;
    private StatusMissao status;
    private NivelPerigo nivelPerigo;
    private OffsetDateTime createdAt;
    private OffsetDateTime dataInicio;
    private OffsetDateTime dataTermino;
    private List<ParticipanteRep> participantes;

    @Data
    public static class ParticipanteRep {
        private Long aventureiroId;
        private String aventureiroNome;
        private PapelMissao papelNaMissao;
        private BigDecimal recompensaOuro;
        private Boolean mvp;

        public static ParticipanteRep from(ParticipacaoMissao p) {
            ParticipanteRep rep = new ParticipanteRep();
            rep.setAventureiroId(p.getAventureiro().getId());
            rep.setAventureiroNome(p.getAventureiro().getNome());
            rep.setPapelNaMissao(p.getPapelNaMissao());
            rep.setRecompensaOuro(p.getRecompensaOuro());
            rep.setMvp(p.getMvp());
            return rep;
        }
    }

    public static MissaoDetalheRep from(Missao m) {
        MissaoDetalheRep rep = new MissaoDetalheRep();
        rep.setId(m.getId());
        rep.setTitulo(m.getTitulo());
        rep.setStatus(m.getStatus());
        rep.setNivelPerigo(m.getNivelPerigo());
        rep.setCreatedAt(m.getCreatedAt());
        rep.setDataInicio(m.getDataInicio());
        rep.setDataTermino(m.getDataTermino());
        rep.setParticipantes(
                m.getParticipacoes() == null ? List.of() :
                        m.getParticipacoes().stream()
                                .map(ParticipanteRep::from)
                                .collect(Collectors.toList())
        );
        return rep;
    }
}
