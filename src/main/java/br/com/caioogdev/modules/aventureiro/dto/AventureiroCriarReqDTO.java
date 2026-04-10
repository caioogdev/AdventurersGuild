package br.com.caioogdev.modules.aventureiro.dto;

import br.com.caioogdev.shared.enums.Classe;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AventureiroCriarReqDTO {
    @NotBlank(message = "nome é obrigatório")
    private String nome;

    @NotNull(message = "classe é obrigatória")
    private Classe classe;

    @NotNull(message = "nivel é obrigatório")
    @Min(value = 1, message = "nivel deve ser maior ou igual a 1")
    private Integer nivel;

    @NotNull
    private Long organizacaoId;

    @NotNull
    private Long usuarioResponsavelId;
}
