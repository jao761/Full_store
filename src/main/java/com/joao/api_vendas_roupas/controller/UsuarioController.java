package com.joao.api_vendas_roupas.controller;

import com.joao.api_vendas_roupas.domain.usuario.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("usuario")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<String> cadastrarUsuario(@RequestBody @Valid DadosCadastroUsuarios dados, UriComponentsBuilder uriBuilder) {
        Usuario usuario = service.cadastrarUsuario(dados);
        var uri = uriBuilder.path("/usuario/{id}").buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(uri).body("Usuario Cadastrado com sucesso");
    }

    @GetMapping
    public ResponseEntity<DadosUsuario> detalheUsuario(@AuthenticationPrincipal Usuario logado) {
        return ResponseEntity.ok(new DadosUsuario(logado));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<DadosUsuario> autalizarUsuario(@AuthenticationPrincipal Usuario logado, DadoAtualizarUsuario dados) {
        logado.atualizarInformaoces(dados);
        return ResponseEntity.ok(new DadosUsuario(logado));
    }
}
