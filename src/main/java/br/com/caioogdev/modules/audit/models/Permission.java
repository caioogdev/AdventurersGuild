package br.com.caioogdev.modules.audit.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(schema = "audit", name = "permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(name = "descricao")
    private String descricao;
}
