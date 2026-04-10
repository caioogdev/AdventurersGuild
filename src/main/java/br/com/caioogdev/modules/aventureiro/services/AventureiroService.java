package br.com.caioogdev.modules.aventureiro.services;

import br.com.caioogdev.modules.audit.models.Organizacao;
import br.com.caioogdev.modules.audit.models.Usuario;
import br.com.caioogdev.modules.audit.repositories.OrganizacaoRepository;
import br.com.caioogdev.modules.audit.repositories.UsuarioRepository;
import br.com.caioogdev.modules.aventureiro.dto.AventureiroAtualizarReqDTO;
import br.com.caioogdev.modules.aventureiro.dto.AventureiroCriarReqDTO;
import br.com.caioogdev.modules.aventureiro.dto.AventureiroDetalheRep;
import br.com.caioogdev.modules.aventureiro.dto.AventureiroResumoRep;
import br.com.caioogdev.modules.aventureiro.models.Aventureiro;
import br.com.caioogdev.modules.aventureiro.repositories.AventureiroRepository;
import br.com.caioogdev.modules.companheiro.dto.CompanheiroReqDTO;
import br.com.caioogdev.modules.companheiro.models.Companheiro;
import br.com.caioogdev.modules.missao.repositories.ParticipacaoMissaoRepository;
import br.com.caioogdev.shared.enums.Classe;
import br.com.caioogdev.shared.exception.NaoEscontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AventureiroService {

    private final AventureiroRepository repository;
    private final OrganizacaoRepository organizacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ParticipacaoMissaoRepository participacaoRepository;


    @Transactional
    public AventureiroDetalheRep registrar(AventureiroCriarReqDTO request) {
        Organizacao organizacao = organizacaoRepository.findById(request.getOrganizacaoId())
                .orElseThrow(() -> new NaoEscontradoException("Organização não encontrada"));

        Usuario usuario = usuarioRepository.findById(request.getUsuarioResponsavelId())
                .orElseThrow(() -> new NaoEscontradoException("Usuário não encontrado"));

        Aventureiro aventureiro = Aventureiro.builder()
                .nome(request.getNome())
                .classe(request.getClasse())
                .nivel(request.getNivel())
                .ativo(true)
                .organizacao(organizacao)
                .usuarioResponsavel(usuario)
                .build();

        return AventureiroDetalheRep.from(repository.save(aventureiro));
    }

    public Page<AventureiroResumoRep> listar(
            Classe classe, Boolean ativo, Integer nivelMinimo, int page, int size, String ordenarPor) {

        Sort sort = Sort.by(ordenarPor != null ? ordenarPor : "nome");
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.listarComFiltros(classe, ativo, nivelMinimo, pageable)
                .map(AventureiroResumoRep::from);
    }

    public Page<AventureiroResumoRep> buscarPorNome(String nome, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("nome"));
        return repository.buscarPorNome(nome, pageable).map(AventureiroResumoRep::from);
    }

    public AventureiroDetalheRep buscarPorId(Long id) {
        Aventureiro aventureiro = repository.buscarPerfilCompleto(id)
                .orElseThrow(() -> new NaoEscontradoException(
                        "Aventureiro não encontrado com id: " + id));

        AventureiroDetalheRep response = AventureiroDetalheRep.from(aventureiro);
        response.setTotalMissoes(participacaoRepository.contarPorAventureiro(id));

        participacaoRepository.ultimaMissao(id).ifPresent(missao -> {
            AventureiroDetalheRep.UltimaMissaoDetalhe ultima = new AventureiroDetalheRep.UltimaMissaoDetalhe();
            ultima.setId(missao.getId());
            ultima.setTitulo(missao.getTitulo());
            ultima.setStatus(missao.getStatus());
            ultima.setNivelPerigo(missao.getNivelPerigo());
            response.setUltimaMissao(ultima);
        });

        return response;
    }

    @Transactional
    public AventureiroDetalheRep atualizar(Long id, AventureiroAtualizarReqDTO request) {
        Aventureiro aventureiro = buscarOuLancar(id);
        aventureiro.setNome(request.getNome());
        aventureiro.setClasse(request.getClasse());
        aventureiro.setNivel(request.getNivel());
        return AventureiroDetalheRep.from(repository.save(aventureiro));
    }

    @Transactional
    public void encerrarVinculo(Long id) {
        Aventureiro aventureiro = buscarOuLancar(id);
        aventureiro.setAtivo(false);
        repository.save(aventureiro);
    }

    @Transactional
    public AventureiroDetalheRep recrutar(Long id) {
        Aventureiro aventureiro = buscarOuLancar(id);
        aventureiro.setAtivo(true);
        return AventureiroDetalheRep.from(repository.save(aventureiro));
    }

    @Transactional
    public AventureiroDetalheRep definirCompanheiro(Long id, CompanheiroReqDTO request) {
        Aventureiro aventureiro = buscarOuLancar(id);

        Companheiro companheiro = Companheiro.builder()
                .aventureiro(aventureiro)
                .nome(request.getNome())
                .especie(request.getEspecie())
                .lealdade(request.getLealdade())
                .build();

        aventureiro.setCompanheiro(companheiro);
        return AventureiroDetalheRep.from(repository.save(aventureiro));
    }

    @Transactional
    public void removerCompanheiro(Long id) {
        Aventureiro aventureiro = buscarOuLancar(id);
        aventureiro.setCompanheiro(null);
        repository.save(aventureiro);
    }

    private Aventureiro buscarOuLancar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NaoEscontradoException(
                        "Aventureiro não encontrado com id: " + id));
    }
}
