package br.com.caioogdev.modules.aventureiro.services;

import br.com.caioogdev.modules.aventureiro.dto.AventureiroAtualizarReqDTO;
import br.com.caioogdev.modules.aventureiro.dto.AventureiroCriarReqDTO;
import br.com.caioogdev.modules.aventureiro.dto.AventureiroDetalheRep;
import br.com.caioogdev.modules.aventureiro.dto.AventureiroResumoRep;
import br.com.caioogdev.modules.aventureiro.models.Aventureiro;
import br.com.caioogdev.modules.companheiro.dto.CompanheiroReqDTO;
import br.com.caioogdev.modules.companheiro.models.Companheiro;
import br.com.caioogdev.modulos.aventureiro.repository.AventureiroRepository;
import br.com.caioogdev.shared.enums.Classe;
import br.com.caioogdev.shared.exception.NaoEscontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AventureiroService {

    private final AventureiroRepository repository;

    public AventureiroDetalheRep registrar(AventureiroCriarReqDTO request) {
        Aventureiro aventureiro = Aventureiro.builder()
                .nome(request.getNome())
                .classe(request.getClasse())
                .nivel(request.getNivel())
                .ativo(true)
                .build();

        return AventureiroDetalheRep.from(repository.salvar(aventureiro));
    }

    public List<AventureiroResumoRep> listar(
            Classe classe, Boolean ativo, Integer nivelMinimo, int page, int size) {

        return repository.buscarTodos().stream()
                .filter(a -> classe == null || a.getClasse() == classe)
                .filter(a -> ativo == null || a.getAtivo().equals(ativo))
                .filter(a -> nivelMinimo == null || a.getNivel() >= nivelMinimo)
                .sorted(Comparator.comparingLong(Aventureiro::getId))
                .skip((long) page * size)
                .limit(size)
                .map(AventureiroResumoRep::from)
                .collect(Collectors.toList());
    }

    public long contarComFiltros(Classe classe, Boolean ativo, Integer nivelMinimo) {
        return repository.buscarTodos().stream()
                .filter(a -> classe == null || a.getClasse() == classe)
                .filter(a -> ativo == null || a.getAtivo().equals(ativo))
                .filter(a -> nivelMinimo == null || a.getNivel() >= nivelMinimo)
                .count();
    }

    public AventureiroDetalheRep buscarPorId(Long id) {
        return AventureiroDetalheRep.from(buscarOuLancar(id));
    }

    public AventureiroDetalheRep atualizar(Long id, AventureiroAtualizarReqDTO request) {
        Aventureiro aventureiro = buscarOuLancar(id);
        aventureiro.setNome(request.getNome());
        aventureiro.setClasse(request.getClasse());
        aventureiro.setNivel(request.getNivel());
        return AventureiroDetalheRep.from(repository.salvar(aventureiro));
    }

    public void encerrarVinculo(Long id) {
        Aventureiro aventureiro = buscarOuLancar(id);
        aventureiro.setAtivo(false);
        repository.salvar(aventureiro);
    }

    public AventureiroDetalheRep recrutar(Long id) {
        Aventureiro aventureiro = buscarOuLancar(id);
        aventureiro.setAtivo(true);
        return AventureiroDetalheRep.from(repository.salvar(aventureiro));
    }

    public AventureiroDetalheRep definirCompanheiro(Long id, CompanheiroReqDTO request) {
        Aventureiro aventureiro = buscarOuLancar(id);

        Companheiro companheiro = Companheiro.builder()
                .nome(request.getNome())
                .especie(request.getEspecie())
                .lealdade(request.getLealdade())
                .build();

        aventureiro.setCompanheiro(companheiro);
        return AventureiroDetalheRep.from(repository.salvar(aventureiro));
    }

    public void removerCompanheiro(Long id) {
        Aventureiro aventureiro = buscarOuLancar(id);
        aventureiro.setCompanheiro(null);
        repository.salvar(aventureiro);
    }

    private Aventureiro buscarOuLancar(Long id) {
        return repository.buscarPorId(id)
                .orElseThrow(() -> new NaoEscontradoException(
                        "Aventureiro não encontrado com id: " + id));
    }
}
