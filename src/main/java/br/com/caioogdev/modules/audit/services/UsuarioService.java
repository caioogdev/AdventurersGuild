package br.com.caioogdev.modules.audit.services;

import br.com.caioogdev.modules.audit.dto.UsuarioDetalheRep;
import br.com.caioogdev.modules.audit.dto.UsuarioResumoRep;
import br.com.caioogdev.modules.audit.models.Usuario;
import br.com.caioogdev.modules.audit.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public List<UsuarioResumoRep> listar() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioResumoRep::from)
                .collect(Collectors.toList());
    }

    public UsuarioResumoRep buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .map(UsuarioResumoRep::from)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public UsuarioDetalheRep buscarComRolesEPermissions(Long id) {
        return usuarioRepository.findByIdComRolesEPermissions(id)
                .map(UsuarioDetalheRep::from)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public List<UsuarioResumoRep> listarPorOrganizacao(Long orgId) {
        return usuarioRepository.findAllByOrganizacaoId(orgId)
                .stream()
                .map(UsuarioResumoRep::from)
                .collect(Collectors.toList());
    }
}
