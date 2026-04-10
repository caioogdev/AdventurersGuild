package br.com.caioogdev.modules.aventureiro.models;


import br.com.caioogdev.modules.audit.models.Organizacao;
import br.com.caioogdev.modules.audit.models.Usuario;
import br.com.caioogdev.modules.companheiro.models.Companheiro;
import br.com.caioogdev.shared.enums.Classe;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Table(schema = "aventura", name = "aventureiros")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Aventureiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizacao_id", nullable = false)
    private Organizacao organizacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_responsavel_id", nullable = false)
    private Usuario usuarioResponsavel;

    @Column(nullable = false, length = 120)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Classe classe;

    @Column(nullable = false)
    private Integer nivel;

    @Column(nullable = false)
    private Boolean ativo;

    @OneToOne(mappedBy = "aventureiro", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Companheiro companheiro;

    @Column(nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(nullable = false)
    private OffsetDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }
}
