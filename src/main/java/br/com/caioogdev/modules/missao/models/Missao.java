package br.com.caioogdev.modules.missao.models;

import br.com.caioogdev.modules.audit.models.Organizacao;
import br.com.caioogdev.shared.enums.NivelPerigo;
import br.com.caioogdev.shared.enums.StatusMissao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(schema = "aventura", name = "missoes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Missao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizacao_id", nullable = false)
    private Organizacao organizacao;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_perigo", nullable = false)
    private NivelPerigo nivelPerigo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusMissao status;

    @Column(nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    private OffsetDateTime dataInicio;

    private OffsetDateTime dataTermino;

    @OneToMany(mappedBy = "missao", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ParticipacaoMissao> participacoes;

    @PrePersist
    public void prePersist() {
        this.createdAt = OffsetDateTime.now();
        if (this.status == null) {
            this.status = StatusMissao.PLANEJADA;
        }
    }
}

