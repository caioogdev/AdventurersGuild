package br.com.caioogdev.modules.audit.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(
    schema = "audit",
    name = "usuarios",
    uniqueConstraints = @UniqueConstraint(columnNames = {"organizacao_id", "email"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizacao_id", nullable = false)
    private Organizacao organizacao;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String email;

    @Column(name = "senha_hash")
    private String senhaHash;

    @Column(nullable = false)
    private String status;

    @Column(name = "ultimo_login_em")
    private OffsetDateTime ultimoLoginEm;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        schema = "audit",
        name = "user_roles",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;
}
