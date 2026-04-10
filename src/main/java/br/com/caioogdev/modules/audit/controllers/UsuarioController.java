package br.com.caioogdev.modules.audit.controllers;

import br.com.caioogdev.modules.audit.dto.UsuarioDetalheRep;
import br.com.caioogdev.modules.audit.dto.UsuarioResumoRep;
import br.com.caioogdev.modules.audit.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    @GetMapping
    public List<UsuarioResumoRep> listar() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioResumoRep::from)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResumoRep> buscarPorId(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .map(UsuarioResumoRep::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/completo")
    public ResponseEntity<UsuarioDetalheRep> buscarComRolesEPermissions(@PathVariable Long id) {
        return usuarioRepository.findByIdComRolesEPermissions(id)
                .map(UsuarioDetalheRep::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/organizacao/{orgId}")
    public List<UsuarioResumoRep> listarPorOrganizacao(@PathVariable Long orgId) {
        return usuarioRepository.findAllByOrganizacaoId(orgId)
                .stream()
                .map(UsuarioResumoRep::from)
                .collect(Collectors.toList());
    }
}
