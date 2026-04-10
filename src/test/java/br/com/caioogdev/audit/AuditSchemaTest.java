package br.com.caioogdev.audit;

import br.com.caioogdev.modules.audit.models.*;
import br.com.caioogdev.modules.audit.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuditSchemaTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private OrganizacaoRepository organizacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void devePersistirOrganizacaoERecuperarPorId() {
        Organizacao org = Organizacao.builder()
                .nome("Guilda dos Aventureiros " + System.currentTimeMillis())
                .ativo(true)
                .createdAt(OffsetDateTime.now())
                .build();

        Organizacao salva = organizacaoRepository.save(org);
        em.flush();
        em.clear();

        Optional<Organizacao> encontrada = organizacaoRepository.findById(salva.getId());

        assertThat(encontrada).isPresent();
        assertThat(encontrada.get().getNome()).isEqualTo(org.getNome());
        assertThat(encontrada.get().getAtivo()).isTrue();
    }

    @Test
    void deveCarregarUsuarioComOrganizacao() {
        Organizacao org = organizacaoRepository.save(
                Organizacao.builder()
                        .nome("Org Usuario " + System.currentTimeMillis())
                        .ativo(true)
                        .createdAt(OffsetDateTime.now())
                        .build()
        );

        Usuario usuario = usuarioRepository.save(
                Usuario.builder()
                        .organizacao(org)
                        .nome("Thorin Oakenshield")
                        .email("thorin" + System.currentTimeMillis() + "@guilda.com")
                        .senhaHash("hash_secreto")
                        .status("ATIVO")
                        .createdAt(OffsetDateTime.now())
                        .updatedAt(OffsetDateTime.now())
                        .build()
        );
        em.flush();
        em.clear();

        Optional<Usuario> encontrado = usuarioRepository.findById(usuario.getId());

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getNome()).isEqualTo("Thorin Oakenshield");
        assertThat(encontrado.get().getOrganizacao().getId()).isEqualTo(org.getId());
    }

    @Test
    void deveCarregarUsuarioComRolesEPermissions() {
        Organizacao org = organizacaoRepository.save(
                Organizacao.builder()
                        .nome("Org Roles " + System.currentTimeMillis())
                        .ativo(true)
                        .createdAt(OffsetDateTime.now())
                        .build()
        );

        Permission permLeitura = em.persist(
                Permission.builder()
                        .code("LEITURA_" + System.currentTimeMillis())
                        .descricao("Permissão de leitura")
                        .build()
        );

        Permission permEscrita = em.persist(
                Permission.builder()
                        .code("ESCRITA_" + System.currentTimeMillis())
                        .descricao("Permissão de escrita")
                        .build()
        );

        Role role = roleRepository.save(
                Role.builder()
                        .organizacao(org)
                        .nome("ADMIN_" + System.currentTimeMillis())
                        .descricao("Administrador da guilda")
                        .createdAt(OffsetDateTime.now())
                        .permissions(List.of(permLeitura, permEscrita))
                        .build()
        );

        Usuario usuario = usuarioRepository.save(
                Usuario.builder()
                        .organizacao(org)
                        .nome("Gandalf")
                        .email("gandalf" + System.currentTimeMillis() + "@guilda.com")
                        .senhaHash("voce_nao_passara")
                        .status("ATIVO")
                        .createdAt(OffsetDateTime.now())
                        .updatedAt(OffsetDateTime.now())
                        .roles(List.of(role))
                        .build()
        );
        em.flush();
        em.clear();

        Optional<Usuario> encontrado = usuarioRepository.findByIdComRolesEPermissions(usuario.getId());

        assertThat(encontrado).isPresent();

        Usuario u = encontrado.get();
        assertThat(u.getRoles()).isNotEmpty();
        assertThat(u.getRoles().get(0).getNome()).startsWith("ADMIN_");

        List<Permission> permissions = u.getRoles().get(0).getPermissions();
        assertThat(permissions).hasSize(2);
        assertThat(permissions).extracting(Permission::getDescricao)
                .containsExactlyInAnyOrder("Permissão de leitura", "Permissão de escrita");
    }

    @Test
    void deveAcessarPermissoesDaRoleViaUsuario() {
        Organizacao org = organizacaoRepository.save(
                Organizacao.builder()
                        .nome("Org Perm " + System.currentTimeMillis())
                        .ativo(true)
                        .createdAt(OffsetDateTime.now())
                        .build()
        );

        Permission perm = em.persist(
                Permission.builder()
                        .code("MISSAO_VER_" + System.currentTimeMillis())
                        .descricao("Ver missões")
                        .build()
        );

        Role role = roleRepository.save(
                Role.builder()
                        .organizacao(org)
                        .nome("MEMBRO_" + System.currentTimeMillis())
                        .createdAt(OffsetDateTime.now())
                        .permissions(List.of(perm))
                        .build()
        );

        Usuario usuario = usuarioRepository.save(
                Usuario.builder()
                        .organizacao(org)
                        .nome("Frodo Baggins")
                        .email("frodo" + System.currentTimeMillis() + "@shire.com")
                        .senhaHash("um_anel")
                        .status("ATIVO")
                        .createdAt(OffsetDateTime.now())
                        .updatedAt(OffsetDateTime.now())
                        .roles(List.of(role))
                        .build()
        );
        em.flush();
        em.clear();

        Optional<Usuario> encontrado = usuarioRepository.findByIdComRolesEPermissions(usuario.getId());

        assertThat(encontrado).isPresent();
        // Navega usuario → roles → permissions
        long totalPermissions = encontrado.get().getRoles().stream()
                .flatMap(r -> r.getPermissions().stream())
                .count();

        assertThat(totalPermissions).isGreaterThanOrEqualTo(1);
    }
}
