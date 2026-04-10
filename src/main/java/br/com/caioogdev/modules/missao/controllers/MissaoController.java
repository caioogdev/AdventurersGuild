package br.com.caioogdev.modules.missao.controllers;

import br.com.caioogdev.modules.missao.dto.*;
import br.com.caioogdev.modules.missao.models.Missao;
import br.com.caioogdev.modules.missao.services.MissaoService;
import br.com.caioogdev.shared.enums.NivelPerigo;
import br.com.caioogdev.shared.enums.StatusMissao;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/missoes")
@RequiredArgsConstructor
public class MissaoController {

    private final MissaoService service;

    @PostMapping
    public ResponseEntity<MissaoResumoRep> criar(@Valid @RequestBody MissaoCriarReqDTO request) {
        Missao missao = service.criar(request.getOrganizacaoId(), request.getTitulo(), request.getNivelPerigo());
        return ResponseEntity.status(HttpStatus.CREATED).body(MissaoResumoRep.from(missao));
    }

    @GetMapping
    public ResponseEntity<List<MissaoResumoRep>> listar(
            @RequestParam(required = false) StatusMissao status,
            @RequestParam(required = false) NivelPerigo nivelPerigo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime dataFim,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Missao> resultado = service.listar(status, nivelPerigo, dataInicio, dataFim, page, size);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(resultado.getTotalElements()));
        headers.add("X-Page", String.valueOf(page));
        headers.add("X-Size", String.valueOf(size));
        headers.add("X-Total-Pages", String.valueOf(resultado.getTotalPages()));

        return ResponseEntity.ok().headers(headers)
                .body(resultado.getContent().stream().map(MissaoResumoRep::from).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MissaoDetalheRep> buscarPorId(@PathVariable Long id) {
        Missao missao = service.buscarComParticipantes(id);
        return ResponseEntity.ok(MissaoDetalheRep.from(missao));
    }

    @PostMapping("/{id}/participantes")
    public ResponseEntity<Void> adicionarParticipante(
            @PathVariable Long id,
            @Valid @RequestBody AdicionarParticipanteReqDTO request) {

        service.adicionarParticipante(
                id,
                request.getAventureiroId(),
                request.getPapelNaMissao(),
                request.getRecompensaOuro(),
                request.getMvp());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/ranking")
    public ResponseEntity<List<RankingItemRep>> ranking(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime dataFim,
            @RequestParam(required = false) StatusMissao status) {

        List<RankingItemRep> resultado = service.ranking(dataInicio, dataFim, status)
                .stream().map(RankingItemRep::from).collect(Collectors.toList());
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/relatorio")
    public ResponseEntity<List<RelatorioMissaoRep>> relatorio(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime dataFim) {

        List<RelatorioMissaoRep> resultado = service.relatorioMissoes(dataInicio, dataFim)
                .stream().map(RelatorioMissaoRep::from).collect(Collectors.toList());
        return ResponseEntity.ok(resultado);
    }
}
