package br.com.caioogdev.modules.marketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FaixaPrecoDTO {
    private String faixa;
    private Long quantidade;
}

