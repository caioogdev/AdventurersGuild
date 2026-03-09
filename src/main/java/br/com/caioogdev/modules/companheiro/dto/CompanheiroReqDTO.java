package br.com.caioogdev.modules.companheiro.dto;

import br.com.caioogdev.shared.enums.Especie;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CompanheiroReqDTO {
    @NotBlank(message = "nome é obrigatório")
    private String nome;

    @NotNull(message = "especie é obrigatória")
    private Especie especie;

    @NotNull(message = "lealdade é obrigatória")
    @Min(value = 0, message = "lealdade deve estar entre 0 e 100")
    @Max(value = 100, message = "lealdade deve estar entre 0 e 100")
    private Integer lealdade;
}
