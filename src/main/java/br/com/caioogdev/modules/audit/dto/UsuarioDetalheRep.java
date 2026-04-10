package br.com.caioogdev.modules.audit.dto;

import br.com.caioogdev.modules.audit.models.Usuario;
import lombok.Data;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UsuarioDetalheRep {
    private Long id;
    private String nome;
    private String email;
    private String status;
    private Long organizacaoId;
    private String organizacaoNome;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private List<RoleRep> roles;

    @Data
    public static class RoleRep {
        private Long id;
        private String nome;
        private String descricao;
        private List<PermissaoRep> permissions;
    }

    @Data
    public static class PermissaoRep {
        private Long id;
        private String code;
        private String descricao;
    }

    public static UsuarioDetalheRep from(Usuario u) {
        UsuarioDetalheRep rep = new UsuarioDetalheRep();
        rep.setId(u.getId());
        rep.setNome(u.getNome());
        rep.setEmail(u.getEmail());
        rep.setStatus(u.getStatus());
        rep.setOrganizacaoId(u.getOrganizacao().getId());
        rep.setOrganizacaoNome(u.getOrganizacao().getNome());
        rep.setCreatedAt(u.getCreatedAt());
        rep.setUpdatedAt(u.getUpdatedAt());

        if (u.getRoles() != null) {
            rep.setRoles(u.getRoles().stream().map(r -> {
                RoleRep roleRep = new RoleRep();
                roleRep.setId(r.getId());
                roleRep.setNome(r.getNome());
                roleRep.setDescricao(r.getDescricao());
                if (r.getPermissions() != null) {
                    roleRep.setPermissions(r.getPermissions().stream().map(p -> {
                        PermissaoRep permRep = new PermissaoRep();
                        permRep.setId(p.getId());
                        permRep.setCode(p.getCode());
                        permRep.setDescricao(p.getDescricao());
                        return permRep;
                    }).collect(Collectors.toList()));
                }
                return roleRep;
            }).collect(Collectors.toList()));
        }

        return rep;
    }
}
