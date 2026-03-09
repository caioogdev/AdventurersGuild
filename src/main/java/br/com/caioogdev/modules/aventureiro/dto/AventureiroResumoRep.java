package br.com.caioogdev.modules.aventureiro.dto;

import br.com.caioogdev.modules.aventureiro.models.Aventureiro;
import br.com.caioogdev.shared.enums.Classe;
import lombok.Data;

@Data
public class AventureiroResumoRep {
    private Long id;
    private String nome;
    private Classe classe;
    private Integer nivel;
    private Boolean ativo;

    public static AventureiroResumoRep from(Aventureiro aventureiro) {
        AventureiroResumoRep response = new AventureiroResumoRep();
        response.setId(aventureiro.getId());
        response.setNome(aventureiro.getNome());
        response.setClasse(aventureiro.getClasse());
        response.setNivel(aventureiro.getNivel());
        response.setAtivo(aventureiro.getAtivo());
        return response;
    }
}
