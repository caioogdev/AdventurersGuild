package br.com.caioogdev.modules.audit.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(schema = "audit", name = "audit_entries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizacao_id", nullable = false)
    private Organizacao organizacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_user_id")
    private Usuario actorUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_api_key_id")
    private ApiKey actorApiKey;

    @Column(nullable = false)
    private String action;

    @Column(name = "entity_schema")
    private String entitySchema;

    @Column(name = "entity_name")
    private String entityName;

    @Column(name = "entity_id")
    private String entityId;

    @Column(name = "occurred_at", nullable = false)
    private OffsetDateTime occurredAt;

    @Column(columnDefinition = "inet")
    private String ip;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(columnDefinition = "jsonb")
    private String diff;

    @Column(columnDefinition = "jsonb")
    private String metadata;

    @Column(nullable = false)
    private Boolean success;
}
