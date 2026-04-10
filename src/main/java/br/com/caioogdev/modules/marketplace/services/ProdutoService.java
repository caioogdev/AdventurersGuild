package br.com.caioogdev.modules.marketplace.services;

import br.com.caioogdev.modules.marketplace.documents.ProdutoDocument;
import br.com.caioogdev.modules.marketplace.dto.AgregacaoItemDTO;
import br.com.caioogdev.modules.marketplace.dto.FaixaPrecoDTO;
import br.com.caioogdev.modules.marketplace.dto.ProdutoDTO;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.AggregationRange;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregation;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ElasticsearchOperations elasticsearchOperations;

    public List<ProdutoDTO> buscarPorNome(String termo) {
        var query = NativeQuery.builder()
                .withQuery(q -> q.match(m -> m.field("nome").query(termo)))
                .build();
        return buscar(query);
    }

    public List<ProdutoDTO> buscarPorDescricao(String termo) {
        var query = NativeQuery.builder()
                .withQuery(q -> q.match(m -> m.field("descricao").query(termo)))
                .build();
        return buscar(query);
    }

    public List<ProdutoDTO> buscarPorFrase(String termo) {
        var query = NativeQuery.builder()
                .withQuery(q -> q.matchPhrase(m -> m.field("descricao").query(termo)))
                .build();
        return buscar(query);
    }

    public List<ProdutoDTO> buscarFuzzy(String termo) {
        var query = NativeQuery.builder()
                .withQuery(q -> q.fuzzy(f -> f.field("nome").value(termo).fuzziness("AUTO")))
                .build();
        return buscar(query);
    }

    public List<ProdutoDTO> buscarMultiCampos(String termo) {
        var query = NativeQuery.builder()
                .withQuery(q -> q.multiMatch(m -> m
                        .fields("nome", "descricao")
                        .query(termo)))
                .build();
        return buscar(query);
    }

    public List<ProdutoDTO> buscarComFiltro(String termo, String categoria) {
        var query = NativeQuery.builder()
                .withQuery(q -> q.bool(b -> b
                        .must(m -> m.match(mt -> mt.field("descricao").query(termo)))
                        .filter(f -> f.term(t -> t.field("categoria").value(categoria)))))
                .build();
        return buscar(query);
    }

    public List<ProdutoDTO> buscarPorFaixaPreco(Double min, Double max) {
        var query = NativeQuery.builder()
                .withQuery(q -> q.range(r -> r
                        .number(n -> n.field("preco").gte(min).lte(max))))
                .build();
        return buscar(query);
    }

    public List<ProdutoDTO> buscarAvancada(String categoria, String raridade, Double min, Double max) {
        var query = NativeQuery.builder()
                .withQuery(q -> q.bool(b -> {
                    if (categoria != null)
                        b.filter(f -> f.term(t -> t.field("categoria").value(categoria)));
                    if (raridade != null)
                        b.filter(f -> f.term(t -> t.field("raridade").value(raridade)));
                    if (min != null && max != null)
                        b.filter(f -> f.range(r -> r.number(n -> n.field("preco").gte(min).lte(max))));
                    return b;
                }))
                .build();
        return buscar(query);
    }

    public List<AgregacaoItemDTO> agregarPorCategoria() {
        var query = NativeQuery.builder()
                .withQuery(q -> q.matchAll(m -> m))
                .withAggregation("por_categoria", Aggregation.of(a -> a
                        .terms(t -> t.field("categoria").size(20))))
                .withMaxResults(0)
                .build();

        SearchHits<ProdutoDocument> hits = elasticsearchOperations.search(query, ProdutoDocument.class);
        ElasticsearchAggregations aggs = (ElasticsearchAggregations) hits.getAggregations();
        ElasticsearchAggregation agg = aggs.aggregations().get(0);

        return agg.aggregation().getAggregate().sterms().buckets().array()
                .stream()
                .map(b -> new AgregacaoItemDTO(b.key().stringValue(), b.docCount()))
                .collect(Collectors.toList());
    }

    public List<AgregacaoItemDTO> agregarPorRaridade() {
        var query = NativeQuery.builder()
                .withQuery(q -> q.matchAll(m -> m))
                .withAggregation("por_raridade", Aggregation.of(a -> a
                        .terms(t -> t.field("raridade").size(20))))
                .withMaxResults(0)
                .build();

        SearchHits<ProdutoDocument> hits = elasticsearchOperations.search(query, ProdutoDocument.class);
        ElasticsearchAggregations aggs = (ElasticsearchAggregations) hits.getAggregations();
        ElasticsearchAggregation agg = aggs.aggregations().get(0);

        return agg.aggregation().getAggregate().sterms().buckets().array()
                .stream()
                .map(b -> new AgregacaoItemDTO(b.key().stringValue(), b.docCount()))
                .collect(Collectors.toList());
    }

    public Double calcularPrecoMedio() {
        var query = NativeQuery.builder()
                .withQuery(q -> q.matchAll(m -> m))
                .withAggregation("preco_medio", Aggregation.of(a -> a
                        .avg(avg -> avg.field("preco"))))
                .withMaxResults(0)
                .build();

        SearchHits<ProdutoDocument> hits = elasticsearchOperations.search(query, ProdutoDocument.class);
        ElasticsearchAggregations aggs = (ElasticsearchAggregations) hits.getAggregations();
        ElasticsearchAggregation agg = aggs.aggregations().get(0);

        return agg.aggregation().getAggregate().avg().value();
    }

    public List<FaixaPrecoDTO> agregarPorFaixaPreco() {
        var query = NativeQuery.builder()
                .withQuery(q -> q.matchAll(m -> m))
                .withAggregation("faixas_preco", Aggregation.of(a -> a
                        .range(r -> r
                                .field("preco")
                                .ranges(
                                        AggregationRange.of(ar -> ar.key("Abaixo de 100").to(100.0)),
                                        AggregationRange.of(ar -> ar.key("100 a 300").from(100.0).to(300.0)),
                                        AggregationRange.of(ar -> ar.key("300 a 700").from(300.0).to(700.0)),
                                        AggregationRange.of(ar -> ar.key("Acima de 700").from(700.0))
                                ))))
                .withMaxResults(0)
                .build();

        SearchHits<ProdutoDocument> hits = elasticsearchOperations.search(query, ProdutoDocument.class);
        ElasticsearchAggregations aggs = (ElasticsearchAggregations) hits.getAggregations();
        ElasticsearchAggregation agg = aggs.aggregations().get(0);

        return agg.aggregation().getAggregate().range().buckets().array()
                .stream()
                .map(b -> new FaixaPrecoDTO(b.key(), b.docCount()))
                .collect(Collectors.toList());
    }

    private List<ProdutoDTO> buscar(Query query) {
        SearchHits<ProdutoDocument> hits = elasticsearchOperations.search(query, ProdutoDocument.class);
        return hits.stream()
                .map(hit -> ProdutoDTO.from(hit.getContent()))
                .collect(Collectors.toList());
    }
}
