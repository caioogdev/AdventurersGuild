package br.com.caioogdev.modules.audit.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(schema = "audit", name = "organizacoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Organizacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(nullable = false)
    private Boolean ativo;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "organizacao", fetch = FetchType.LAZY)
    private List<Usuario> usuarios;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "organizacao", fetch = FetchType.LAZY)
    private List<Role> roles;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "organizacao", fetch = FetchType.LAZY)
    private List<ApiKey> apiKeys;
}
