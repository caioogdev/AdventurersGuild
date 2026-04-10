package br.com.caioogdev.modules.missao.dto;

import br.com.caioogdev.shared.enums.PapelMissao;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AdicionarParticipanteReqDTO {

    @NotNull(message = "aventureiroId é obrigatório")
    private Long aventureiroId;

    @NotNull(message = "papelNaMissao é obrigatório")
    private PapelMissao papelNaMissao;

    @DecimalMin(value = "0", message = "recompensaOuro deve ser maior ou igual a 0")
    private BigDecimal recompensaOuro;

    private Boolean mvp;
}
