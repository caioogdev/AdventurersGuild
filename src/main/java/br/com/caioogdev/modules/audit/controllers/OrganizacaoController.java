package br.com.caioogdev.modules.audit.controllers;

import br.com.caioogdev.modules.audit.dto.OrganizacaoResumoRep;
import br.com.caioogdev.modules.audit.models.Organizacao;
import br.com.caioogdev.modules.audit.repositories.OrganizacaoRepository;
import br.com.caioogdev.modules.audit.services.OrganizacaoService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/organizacoes")
@RequiredArgsConstructor
public class OrganizacaoController {

    private final OrganizacaoService organizacaoService;

    @GetMapping
    public ResponseEntity<List<OrganizacaoResumoRep>> listar() {
        return ResponseEntity.ok(organizacaoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizacaoResumoRep> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(organizacaoService.buscardPorId(id));
    }
}
