package br.com.caioogdev.modules.audit.controllers;

import br.com.caioogdev.modules.audit.models.Organizacao;
import br.com.caioogdev.modules.audit.repositories.OrganizacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/organizacoes")
@RequiredArgsConstructor
public class OrganizacaoController {

    private final OrganizacaoRepository organizacaoRepository;

    @GetMapping
    public List<Organizacao> listar() {
        return organizacaoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Organizacao> buscarPorId(@PathVariable Long id) {
        return organizacaoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
