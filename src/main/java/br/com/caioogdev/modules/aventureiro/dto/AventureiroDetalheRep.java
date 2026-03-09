package br.com.caioogdev.modules.aventureiro.dto;

import br.com.caioogdev.modules.aventureiro.models.Aventureiro;
import br.com.caioogdev.shared.enums.Classe;
import lombok.Data;

@Data
public class AventureiroDetalheRep {

    private Long id;
    private String nome;
    private Classe classe;
    private Integer nivel;
    private Boolean ativo;
    private CompanheiroDetalhe companheiro;

    @Data
    public static class CompanheiroDetalhe {
        private String nome;
        private String especie;
        private Integer lealdade;
    }

    public static AventureiroDetalheRep from(Aventureiro aventureiro) {
        AventureiroDetalheRep response = new AventureiroDetalheRep();
        response.setId(aventureiro.getId());
        response.setNome(aventureiro.getNome());
        response.setClasse(aventureiro.getClasse());
        response.setNivel(aventureiro.getNivel());
        response.setAtivo(aventureiro.getAtivo());

        if (aventureiro.getCompanheiro() != null) {
            CompanheiroDetalhe comp = new CompanheiroDetalhe();
            comp.setNome(aventureiro.getCompanheiro().getNome());
            comp.setEspecie(aventureiro.getCompanheiro().getEspecie().name());
            comp.setLealdade(aventureiro.getCompanheiro().getLealdade());
            response.setCompanheiro(comp);
        }

        return response;
    }
}
