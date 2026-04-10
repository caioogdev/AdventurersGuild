package br.com.caioogdev.modules.audit.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(
    schema = "audit",
    name = "api_keys",
    uniqueConstraints = @UniqueConstraint(columnNames = {"organizacao_id", "nome"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizacao_id", nullable = false)
    private Organizacao organizacao;

    @Column(nullable = false)
    private String nome;

    @Column(name = "key_hash", nullable = false)
    private String keyHash;

    @Column(nullable = false)
    private Boolean ativo;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "last_used_at")
    private OffsetDateTime lastUsedAt;
}
