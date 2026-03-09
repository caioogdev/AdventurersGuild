package br.com.caioogdev.modules.aventureiro.models;


import br.com.caioogdev.modules.companheiro.models.Companheiro;
import br.com.caioogdev.shared.enums.Classe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Aventureiro {
    private Long id;
    private String nome;
    private Classe classe;
    private Integer nivel;
    private Boolean ativo;
    private Companheiro companheiro;
}
