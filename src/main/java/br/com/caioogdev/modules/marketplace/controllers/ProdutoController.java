package br.com.caioogdev.modules.marketplace.controllers;

import br.com.caioogdev.modules.marketplace.dto.AgregacaoItemDTO;
import br.com.caioogdev.modules.marketplace.dto.FaixaPrecoDTO;
import br.com.caioogdev.modules.marketplace.dto.ProdutoDTO;
import br.com.caioogdev.modules.marketplace.services.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService service;

    @GetMapping("/busca/nome")
    public ResponseEntity<List<ProdutoDTO>> buscarPorNome(@RequestParam String termo) {
        return ResponseEntity.ok(service.buscarPorNome(termo));
    }

    @GetMapping("/busca/descricao")
    public ResponseEntity<List<ProdutoDTO>> buscarPorDescricao(@RequestParam String termo) {
        return ResponseEntity.ok(service.buscarPorDescricao(termo));
    }

    @GetMapping("/busca/frase")
    public ResponseEntity<List<ProdutoDTO>> buscarPorFrase(@RequestParam String termo) {
        return ResponseEntity.ok(service.buscarPorFrase(termo));
    }

    @GetMapping("/busca/fuzzy")
    public ResponseEntity<List<ProdutoDTO>> buscarFuzzy(@RequestParam String termo) {
        return ResponseEntity.ok(service.buscarFuzzy(termo));
    }

    @GetMapping("/busca/multicampos")
    public ResponseEntity<List<ProdutoDTO>> buscarMultiCampos(@RequestParam String termo) {
        return ResponseEntity.ok(service.buscarMultiCampos(termo));
    }

    @GetMapping("/busca/com-filtro")
    public ResponseEntity<List<ProdutoDTO>> buscarComFiltro(
            @RequestParam String termo,
            @RequestParam String categoria) {
        return ResponseEntity.ok(service.buscarComFiltro(termo, categoria));
    }

    @GetMapping("/busca/faixa-preco")
    public ResponseEntity<List<ProdutoDTO>> buscarPorFaixaPreco(
            @RequestParam Double min,
            @RequestParam Double max) {
        return ResponseEntity.ok(service.buscarPorFaixaPreco(min, max));
    }

    @GetMapping("/busca/avancada")
    public ResponseEntity<List<ProdutoDTO>> buscarAvancada(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String raridade,
            @RequestParam(required = false) Double min,
            @RequestParam(required = false) Double max) {
        return ResponseEntity.ok(service.buscarAvancada(categoria, raridade, min, max));
    }

    @GetMapping("/agregacoes/por-categoria")
    public ResponseEntity<List<AgregacaoItemDTO>> agregarPorCategoria() {
        return ResponseEntity.ok(service.agregarPorCategoria());
    }

    @GetMapping("/agregacoes/por-raridade")
    public ResponseEntity<List<AgregacaoItemDTO>> agregarPorRaridade() {
        return ResponseEntity.ok(service.agregarPorRaridade());
    }

    @GetMapping("/agregacoes/preco-medio")
    public ResponseEntity<Double> calcularPrecoMedio() {
        return ResponseEntity.ok(service.calcularPrecoMedio());
    }

    @GetMapping("/agregacoes/faixas-preco")
    public ResponseEntity<List<FaixaPrecoDTO>> agregarPorFaixaPreco() {
        return ResponseEntity.ok(service.agregarPorFaixaPreco());
    }
}

