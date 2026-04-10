package br.com.caioogdev.modules.audit.dto;

import br.com.caioogdev.modules.audit.models.Usuario;
import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class UsuarioResumoRep {
    private Long id;
    private String nome;
    private String email;
    private String status;
    private Long organizacaoId;
    private String organizacaoNome;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public static UsuarioResumoRep from(Usuario u) {
        UsuarioResumoRep rep = new UsuarioResumoRep();
        rep.setId(u.getId());
        rep.setNome(u.getNome());
        rep.setEmail(u.getEmail());
        rep.setStatus(u.getStatus());
        rep.setOrganizacaoId(u.getOrganizacao().getId());
        rep.setOrganizacaoNome(u.getOrganizacao().getNome());
        rep.setCreatedAt(u.getCreatedAt());
        rep.setUpdatedAt(u.getUpdatedAt());
        return rep;
    }
}

