package br.com.caioogdev.aventura;

import br.com.caioogdev.modules.audit.models.Organizacao;
import br.com.caioogdev.modules.audit.models.Usuario;
import br.com.caioogdev.modules.audit.repositories.OrganizacaoRepository;
import br.com.caioogdev.modules.audit.repositories.UsuarioRepository;
import br.com.caioogdev.modules.aventureiro.models.Aventureiro;
import br.com.caioogdev.modules.aventureiro.repositories.AventureiroRepository;
import br.com.caioogdev.modules.missao.models.Missao;
import br.com.caioogdev.modules.missao.models.ParticipacaoMissao;
import br.com.caioogdev.modules.missao.models.ParticipacaoMissaoId;
import br.com.caioogdev.modules.missao.repositories.MissaoRepository;
import br.com.caioogdev.modules.missao.repositories.ParticipacaoMissaoRepository;
import br.com.caioogdev.shared.enums.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.time.OffsetDateTime;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AventuraConsultasTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private AventureiroRepository aventureiroRepository;

    @Autowired
    private MissaoRepository missaoRepository;

    @Autowired
    private ParticipacaoMissaoRepository participacaoRepository;

    @Autowired
    private OrganizacaoRepository organizacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Organizacao org;
    private Usuario usuario;
    private Aventureiro aventureiro1;
    private Aventureiro aventureiro2;
    private Missao missao;

    @BeforeEach
    void setup() {
        org = em.persist(Organizacao.builder()
                .nome("Guilda Teste " + System.currentTimeMillis())
                .ativo(true)
                .createdAt(OffsetDateTime.now())
                .build());

        usuario = em.persist(Usuario.builder()
                .organizacao(org)
                .nome("Admin Teste")
                .email("admin" + System.currentTimeMillis() + "@teste.com")
                .senhaHash("hash")
                .status("ATIVO")
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build());


        aventureiro1 = em.persist(Aventureiro.builder()
                .organizacao(org)
                .usuarioResponsavel(usuario)
                .nome("Aragorn")
                .classe(Classe.GUERREIRO)
                .nivel(10)
                .ativo(true)
                .build());

        aventureiro2 = em.persist(Aventureiro.builder()
                .organizacao(org)
                .usuarioResponsavel(usuario)
                .nome("Gandalf")
                .classe(Classe.MAGO)
                .nivel(5)
                .ativo(false)
                .build());

        missao = em.persist(Missao.builder()
                .organizacao(org)
                .titulo("Destruir o Anel")
                .nivelPerigo(NivelPerigo.EXTREMO)
                .status(StatusMissao.EM_ANDAMENTO)
                .build());

        ParticipacaoMissaoId pid = new ParticipacaoMissaoId(missao.getId(), aventureiro1.getId());
        em.persist(ParticipacaoMissao.builder()
                .id(pid)
                .missao(missao)
                .aventureiro(aventureiro1)
                .papelNaMissao(PapelMissao.LIDER)
                .recompensaOuro(new BigDecimal("500.00"))
                .mvp(true)
                .build());

        em.flush();
        em.clear();
    }

    @Test
    void deveListarAventureirosComFiltroDeClasse() {
        Page<Aventureiro> result = aventureiroRepository.listarComFiltros(
                Classe.GUERREIRO, null, null, PageRequest.of(0, 10));

        assertThat(result.getContent()).isNotEmpty();
        assertThat(result.getContent()).allMatch(a -> a.getClasse() == Classe.GUERREIRO);
    }

    @Test
    void deveListarAventureirosComFiltroDeAtivo() {
        Page<Aventureiro> result = aventureiroRepository.listarComFiltros(
                null, true, null, PageRequest.of(0, 10));

        assertThat(result.getContent()).isNotEmpty();
        assertThat(result.getContent()).allMatch(Aventureiro::getAtivo);
    }

    @Test
    void deveListarAventureirosComFiltroDeNivelMinimo() {
        Page<Aventureiro> result = aventureiroRepository.listarComFiltros(
                null, null, 8, PageRequest.of(0, 10));

        assertThat(result.getContent()).isNotEmpty();
        assertThat(result.getContent()).allMatch(a -> a.getNivel() >= 8);
    }

    @Test
    void deveBuscarAventureiroPorNomeParcial() {
        Page<Aventureiro> result = aventureiroRepository.buscarPorNome("ara", PageRequest.of(0, 10));

        assertThat(result.getContent()).isNotEmpty();
        assertThat(result.getContent().get(0).getNome()).containsIgnoringCase("ara");
    }

    @Test
    void deveBuscarPerfilCompletoDoAventureiro() {
        Optional<Aventureiro> result = aventureiroRepository.buscarPerfilCompleto(aventureiro1.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getNome()).isEqualTo("Aragorn");

        long total = participacaoRepository.contarPorAventureiro(aventureiro1.getId());
        assertThat(total).isEqualTo(1);

        Optional<?> ultima = participacaoRepository.ultimaMissao(aventureiro1.getId());
        assertThat(ultima).isPresent();
    }

    @Test
    void deveListarMissoesComFiltros() {
        Page<Missao> result = missaoRepository.listarComFiltros(
                StatusMissao.EM_ANDAMENTO, null, null, null, PageRequest.of(0, 10));

        assertThat(result.getContent()).isNotEmpty();
        assertThat(result.getContent()).allMatch(m -> m.getStatus() == StatusMissao.EM_ANDAMENTO);
    }

    @Test
    void deveBuscarMissaoComParticipantes() {
        Optional<Missao> result = missaoRepository.buscarComParticipantes(missao.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getParticipacoes()).isNotEmpty();
        assertThat(result.get().getParticipacoes().get(0).getAventureiro().getNome()).isEqualTo("Aragorn");
    }

    @Test
    void deveGerarRankingDeParticipacao() {
        List<Object[]> ranking = participacaoRepository.ranking(null, null, null);

        assertThat(ranking).isNotEmpty();
        Object[] primeiro = ranking.get(0);
        assertThat(primeiro[2]).isNotNull();
    }

    @Test
    void deveGerarRelatorioMissoes() {
        List<Object[]> relatorio = participacaoRepository.relatorioMissoes(null, null);

        assertThat(relatorio).isNotEmpty();
        Object[] linha = relatorio.get(0);
        assertThat(linha[4]).isNotNull();
        assertThat(linha[5]).isNotNull();
    }
}
