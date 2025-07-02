
package com.joao.api_vendas_roupas.infra.security;

import com.joao.api_vendas_roupas.domain.authentication.TokenService;
import com.joao.api_vendas_roupas.domain.usuario.Usuario;
import com.joao.api_vendas_roupas.domain.usuario.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class FiltroTokenAcesso extends OncePerRequestFilter {

    private final TokenService service;
    private final UsuarioRepository repository;

    public FiltroTokenAcesso(TokenService service, UsuarioRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        if (path.equals("/login") || path.equals("/cadastrar")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = recuperarTokenRequisicao(request);

        if(token != null) {
            String email = service.verificarToken(token);
            Usuario usuario = repository.findByEmailIgnoreCase(email).orElseThrow();

            Authentication authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String recuperarTokenRequisicao(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }


}
