package br.com.caioogdev.modules.marketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AgregacaoItemDTO {
    private String chave;
    private Long quantidade;
}
