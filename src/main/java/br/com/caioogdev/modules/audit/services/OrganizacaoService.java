package br.com.caioogdev.modules.audit.services;

import br.com.caioogdev.modules.audit.dto.OrganizacaoResumoRep;
import br.com.caioogdev.modules.audit.repositories.OrganizacaoRepository;
import br.com.caioogdev.shared.exception.NaoEscontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrganizacaoService {

    private final OrganizacaoRepository organizacaoRepository;

    public List<OrganizacaoResumoRep> listar(){
        return organizacaoRepository.findAll()
                .stream()
                .map(OrganizacaoResumoRep::from)
                .collect(Collectors.toList());
    }


    public OrganizacaoResumoRep buscardPorId(Long id) {
        return organizacaoRepository.findById(id)
                .map(OrganizacaoResumoRep::from)
                .orElseThrow(() -> new NaoEscontradoException("Organização nao encontrada"));
    }

}
