package br.com.caioogdev.modules.audit.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(
    schema = "audit",
    name = "roles",
    uniqueConstraints = @UniqueConstraint(columnNames = {"organizacao_id", "nome"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizacao_id", nullable = false)
    private Organizacao organizacao;

    @Column(nullable = false)
    private String nome;

    private String descricao;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        schema = "audit",
        name = "role_permissions",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private List<Permission> permissions;

    @JsonIgnore
    @ToString.Exclude
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private List<Usuario> usuarios;
}
