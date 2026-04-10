package br.com.caioogdev.modules.missao.models;

import br.com.caioogdev.modules.aventureiro.models.Aventureiro;
import br.com.caioogdev.shared.enums.PapelMissao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(schema = "aventura", name = "participacoes_missao")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipacaoMissao {

    @EmbeddedId
    private ParticipacaoMissaoId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("missaoId")
    @JoinColumn(name = "missao_id", nullable = false)
    private Missao missao;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("aventureiroId")
    @JoinColumn(name = "aventureiro_id", nullable = false)
    private Aventureiro aventureiro;

    @Enumerated(EnumType.STRING)
    @Column(name = "papel_na_missao", nullable = false)
    private PapelMissao papelNaMissao;

    @Column(name = "recompensa_ouro")
    private BigDecimal recompensaOuro;

    @Column(nullable = false)
    private Boolean mvp;

    @Column(nullable = false, updatable = false)
    private OffsetDateTime dataRegistro;

    @PrePersist
    public void prePersist() {
        this.dataRegistro = OffsetDateTime.now();
    }
}

