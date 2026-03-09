package br.com.caioogdev.modules.companheiro.models;

import br.com.caioogdev.shared.enums.Especie;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Companheiro {

    private String nome;
    private Especie especie;
    private Integer lealdade;
}
