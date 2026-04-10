package br.com.caioogdev.modules.missao.dto;

import br.com.caioogdev.shared.enums.NivelPerigo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MissaoCriarReqDTO {

    @NotNull(message = "organizacaoId é obrigatório")
    private Long organizacaoId;

    @NotBlank(message = "titulo é obrigatório")
    @Size(max = 150, message = "titulo deve ter no máximo 150 caracteres")
    private String titulo;

    @NotNull(message = "nivelPerigo é obrigatório")
    private NivelPerigo nivelPerigo;
}
