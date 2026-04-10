package br.com.caioogdev.modules.missao.services;

import br.com.caioogdev.modules.audit.models.Organizacao;
import br.com.caioogdev.modules.audit.repositories.OrganizacaoRepository;
import br.com.caioogdev.modules.aventureiro.models.Aventureiro;
import br.com.caioogdev.modules.aventureiro.repositories.AventureiroRepository;
import br.com.caioogdev.modules.missao.models.Missao;
import br.com.caioogdev.modules.missao.models.ParticipacaoMissao;
import br.com.caioogdev.modules.missao.models.ParticipacaoMissaoId;
import br.com.caioogdev.modules.missao.repositories.MissaoRepository;
import br.com.caioogdev.modules.missao.repositories.ParticipacaoMissaoRepository;
import br.com.caioogdev.shared.enums.NivelPerigo;
import br.com.caioogdev.shared.enums.PapelMissao;
import br.com.caioogdev.shared.enums.StatusMissao;
import br.com.caioogdev.shared.exception.NaoEscontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MissaoService {

    private final MissaoRepository missaoRepository;
    private final ParticipacaoMissaoRepository participacaoRepository;
    private final AventureiroRepository aventureiroRepository;
    private final OrganizacaoRepository organizacaoRepository;

    @Transactional
    public Missao criar(Long organizacaoId, String titulo, NivelPerigo nivelPerigo) {
        Organizacao organizacao = organizacaoRepository.findById(organizacaoId)
                .orElseThrow(() -> new NaoEscontradoException("Organização não encontrada"));

        Missao missao = Missao.builder()
                .organizacao(organizacao)
                .titulo(titulo)
                .nivelPerigo(nivelPerigo)
                .status(StatusMissao.PLANEJADA)
                .build();

        return missaoRepository.save(missao);
    }

    public Page<Missao> listar(StatusMissao status, NivelPerigo nivelPerigo,
                               OffsetDateTime dataInicio, OffsetDateTime dataFim,
                               int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return missaoRepository.listarComFiltros(status, nivelPerigo, dataInicio, dataFim, pageable);
    }

    public Missao buscarComParticipantes(Long id) {
        return missaoRepository.buscarComParticipantes(id)
                .orElseThrow(() -> new NaoEscontradoException("Missão não encontrada com id: " + id));
    }

    @Transactional
    public ParticipacaoMissao adicionarParticipante(Long missaoId, Long aventureiroId,
                                                    PapelMissao papel, BigDecimal recompensa, Boolean mvp) {
        Missao missao = missaoRepository.findById(missaoId)
                .orElseThrow(() -> new NaoEscontradoException("Missão não encontrada"));

        Aventureiro aventureiro = aventureiroRepository.findById(aventureiroId)
                .orElseThrow(() -> new NaoEscontradoException("Aventureiro não encontrado"));

        // REGRA: aventureiro inativo não pode participar
        if (!aventureiro.getAtivo()) {
            throw new IllegalStateException("Aventureiro inativo não pode ser associado a missões");
        }

        // REGRA: missão deve estar em estado compatível
        if (missao.getStatus() != StatusMissao.PLANEJADA && missao.getStatus() != StatusMissao.EM_ANDAMENTO) {
            throw new IllegalStateException("Missão não está em estado compatível para aceitar participantes");
        }

        // REGRA: mesma organização
        if (!missao.getOrganizacao().getId().equals(aventureiro.getOrganizacao().getId())) {
            throw new IllegalStateException("Aventureiro e missão pertencem a organizações diferentes");
        }

        ParticipacaoMissaoId participacaoId = new ParticipacaoMissaoId(missaoId, aventureiroId);

        ParticipacaoMissao participacao = ParticipacaoMissao.builder()
                .id(participacaoId)
                .missao(missao)
                .aventureiro(aventureiro)
                .papelNaMissao(papel)
                .recompensaOuro(recompensa)
                .mvp(mvp != null ? mvp : false)
                .build();

        return participacaoRepository.save(participacao);
    }

    public List<Object[]> ranking(OffsetDateTime dataInicio, OffsetDateTime dataFim, StatusMissao status) {
        return participacaoRepository.ranking(dataInicio, dataFim, status);
    }

    public List<Object[]> relatorioMissoes(OffsetDateTime dataInicio, OffsetDateTime dataFim) {
        return participacaoRepository.relatorioMissoes(dataInicio, dataFim);
    }
}

