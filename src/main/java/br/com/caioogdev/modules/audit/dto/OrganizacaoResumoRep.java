package br.com.caioogdev.modules.audit.dto;

import br.com.caioogdev.modules.audit.models.Organizacao;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class OrganizacaoResumoRep {
    private Long id;
    private String nome;
    private Boolean ativo;
    private OffsetDateTime createdAt;

    public static OrganizacaoResumoRep from(Organizacao org) {
        OrganizacaoResumoRep dto = new OrganizacaoResumoRep();
        dto.id = org.getId();
        dto.nome = org.getNome();
        dto.ativo = org.getAtivo();
        dto.createdAt = org.getCreatedAt();
        return dto;
    }
}
