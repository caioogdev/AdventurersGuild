package br.com.caioogdev.modules.companheiro.models;

import br.com.caioogdev.modules.aventureiro.models.Aventureiro;
import br.com.caioogdev.shared.enums.Especie;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "aventura", name = "companheiros")
public class Companheiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aventureiro_id", nullable = false, unique = true)
    private Aventureiro aventureiro;

    @Column(nullable = false, length = 120)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Especie especie;

    @Column(nullable = false)
    private Integer lealdade;
}
