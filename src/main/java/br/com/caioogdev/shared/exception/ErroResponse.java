package br.com.caioogdev.shared.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ErroResponse {

    private String mensagem;
    private List<String> detalhes;
}
