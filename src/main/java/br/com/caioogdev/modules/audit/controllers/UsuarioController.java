package br.com.caioogdev.modules.audit.controllers;

import br.com.caioogdev.modules.audit.dto.UsuarioDetalheRep;
import br.com.caioogdev.modules.audit.dto.UsuarioResumoRep;
import br.com.caioogdev.modules.audit.repositories.UsuarioRepository;
import br.com.caioogdev.modules.audit.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioResumoRep>> listar() {
        return ResponseEntity.ok(usuarioService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResumoRep> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @GetMapping("/{id}/completo")
    public ResponseEntity<UsuarioDetalheRep> buscarComRolesEPermissions(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarComRolesEPermissions(id));
    }

    @GetMapping("/organizacao/{orgId}")
    public ResponseEntity<List<UsuarioResumoRep>> listarPorOrganizacao(@PathVariable Long orgId) {
        return ResponseEntity.ok(usuarioService.listarPorOrganizacao(orgId));
    }
}
