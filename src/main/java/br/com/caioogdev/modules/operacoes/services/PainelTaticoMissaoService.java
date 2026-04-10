package br.com.caioogdev.modules.operacoes.services;

import br.com.caioogdev.modules.operacoes.models.PainelTaticoMissao;
import br.com.caioogdev.modules.operacoes.repositories.PainelTaticoMissaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PainelTaticoMissaoService {

    private final PainelTaticoMissaoRepository repository;

    @Cacheable("top15dias")
    public List<PainelTaticoMissao> buscarTop10UltimosQuinzeDias() {
        LocalDateTime dataLimite = LocalDateTime.now().minusDays(15);
        return repository.findTop10UltimosQuinzeDias(dataLimite);
    }
}

