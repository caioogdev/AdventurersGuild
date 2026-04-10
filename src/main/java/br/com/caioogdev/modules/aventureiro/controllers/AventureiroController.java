package br.com.caioogdev.modules.aventureiro.controllers;

import br.com.caioogdev.modules.aventureiro.dto.AventureiroAtualizarReqDTO;
import br.com.caioogdev.modules.aventureiro.dto.AventureiroCriarReqDTO;
import br.com.caioogdev.modules.aventureiro.dto.AventureiroDetalheRep;
import br.com.caioogdev.modules.aventureiro.dto.AventureiroResumoRep;
import br.com.caioogdev.modules.aventureiro.services.AventureiroService;
import br.com.caioogdev.modules.companheiro.dto.CompanheiroReqDTO;
import br.com.caioogdev.shared.enums.Classe;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/aventureiros")
@RequiredArgsConstructor
@Validated
public class AventureiroController {

    private final AventureiroService service;

    @PostMapping
    public ResponseEntity<AventureiroDetalheRep> registrar(
            @Valid @RequestBody AventureiroCriarReqDTO request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.registrar(request));
    }

    @GetMapping
    public ResponseEntity<List<AventureiroResumoRep>> listar(
            @RequestParam(required = false) Classe classe,
            @RequestParam(required = false) Boolean ativo,
            @RequestParam(required = false) Integer nivelMinimo,
            @RequestParam(required = false) String ordenarPor,
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "page não pode ser negativo") int page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "size deve estar entre 1 e 50") @Max(value = 50, message = "size deve estar entre 1 e 50") int size) {

        Page<AventureiroResumoRep> resultado = service.listar(classe, ativo, nivelMinimo, page, size, ordenarPor);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(resultado.getTotalElements()));
        headers.add("X-Page", String.valueOf(page));
        headers.add("X-Size", String.valueOf(size));
        headers.add("X-Total-Pages", String.valueOf(resultado.getTotalPages()));

        return ResponseEntity.ok().headers(headers).body(resultado.getContent());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<AventureiroResumoRep>> buscarPorNome(
            @RequestParam String nome,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<AventureiroResumoRep> resultado = service.buscarPorNome(nome, page, size);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(resultado.getTotalElements()));
        return ResponseEntity.ok().headers(headers).body(resultado.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AventureiroDetalheRep> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AventureiroDetalheRep> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AventureiroAtualizarReqDTO request) {

        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @DeleteMapping("/{id}/vinculo")
    public ResponseEntity<Void> encerrarVinculo(@PathVariable Long id) {
        service.encerrarVinculo(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/recrutar")
    public ResponseEntity<AventureiroDetalheRep> recrutar(@PathVariable Long id) {
        return ResponseEntity.ok(service.recrutar(id));
    }

    @PutMapping("/{id}/companheiro")
    public ResponseEntity<AventureiroDetalheRep> definirCompanheiro(
            @PathVariable Long id,
            @Valid @RequestBody CompanheiroReqDTO request) {

        return ResponseEntity.ok(service.definirCompanheiro(id, request));
    }

    @DeleteMapping("/{id}/companheiro")
    public ResponseEntity<Void> removerCompanheiro(@PathVariable Long id) {
        service.removerCompanheiro(id);
        return ResponseEntity.noContent().build();
    }
}
