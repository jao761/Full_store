package com.joao.api_vendas_roupas.controller;

import com.joao.api_vendas_roupas.domain.authentication.DadosLogin;
import com.joao.api_vendas_roupas.domain.authentication.DadosRefreshToken;
import com.joao.api_vendas_roupas.domain.authentication.DadosToken;
import com.joao.api_vendas_roupas.domain.authentication.TokenService;
import com.joao.api_vendas_roupas.domain.usuario.Usuario;
import com.joao.api_vendas_roupas.domain.usuario.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    private final AuthenticationManager manager;
    private final TokenService tokenService;
    private final UsuarioRepository repository;

    public AuthenticationController(AuthenticationManager manager, TokenService tokenService, UsuarioRepository repository) {
        this.manager = manager;
        this.tokenService = tokenService;
        this.repository = repository;
    }

    @PostMapping("/login")
    public ResponseEntity<DadosToken> efetuarLogin(@RequestBody @Valid DadosLogin dados) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());
        var authentication = manager.authenticate(authenticationToken);
        String accessToken = tokenService.gerarAccessToken((Usuario) authentication.getPrincipal());
        String refreshToken = tokenService.gerarRefreshToken ((Usuario) authentication.getPrincipal());
        return ResponseEntity.ok(new DadosToken(accessToken, refreshToken));
    }

    @PostMapping("/atualizar-token")
    public ResponseEntity<DadosToken> atualizarToken(@RequestBody @Valid DadosRefreshToken dados) {
        Long idUsuario = Long.valueOf(tokenService.verificarToken(dados.refrehToken()));

        Usuario usuario = repository.getReferenceById(idUsuario);

        String accessToken = tokenService.gerarAccessToken(usuario);
        String refreshToken = tokenService.gerarRefreshToken (usuario);
        return ResponseEntity.ok(new DadosToken(accessToken, refreshToken));
    }

}
