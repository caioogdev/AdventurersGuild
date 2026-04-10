package br.com.caioogdev.modules.marketplace.dto;

import br.com.caioogdev.modules.marketplace.documents.ProdutoDocument;
import lombok.Data;

@Data
public class ProdutoDTO {
    private String id;
    private String nome;
    private String descricao;
    private String categoria;
    private String raridade;
    private Float preco;

    public static ProdutoDTO from(ProdutoDocument doc) {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setId(doc.getId());
        dto.setNome(doc.getNome());
        dto.setDescricao(doc.getDescricao());
        dto.setCategoria(doc.getCategoria());
        dto.setRaridade(doc.getRaridade());
        dto.setPreco(doc.getPreco());
        return dto;
    }
}

