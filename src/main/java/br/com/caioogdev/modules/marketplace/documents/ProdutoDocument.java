package br.com.caioogdev.modules.marketplace.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Document(indexName = "guilda_loja", createIndex = false)
public class ProdutoDocument {

    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String nome;

    @Field(type = FieldType.Text)
    private String descricao;

    @Field(type = FieldType.Keyword)
    private String categoria;

    @Field(type = FieldType.Keyword)
    private String raridade;

    @Field(type = FieldType.Float)
    private Float preco;
}

