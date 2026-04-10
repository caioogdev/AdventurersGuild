package br.com.caioogdev.modules.operacoes.controllers;

import br.com.caioogdev.modules.operacoes.models.PainelTaticoMissao;
import br.com.caioogdev.modules.operacoes.services.PainelTaticoMissaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/missoes")
@RequiredArgsConstructor
public class OperacoesController {

    private final PainelTaticoMissaoService service;

    @GetMapping("/top15dias")
    public ResponseEntity<List<PainelTaticoMissao>> buscarTop10UltimosQuinzeDias() {
        return ResponseEntity.ok(service.buscarTop10UltimosQuinzeDias());
    }
}
