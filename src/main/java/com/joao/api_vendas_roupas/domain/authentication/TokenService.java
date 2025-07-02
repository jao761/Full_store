package com.joao.api_vendas_roupas.domain.authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.joao.api_vendas_roupas.domain.usuario.Usuario;
import com.joao.api_vendas_roupas.infra.exception.RegraDeNegocioException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Service
public class TokenService {

    @Value("${chave.secreta.token}")
    private String chaveSecreta;

    public String gerarAccessToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("123456");

            return JWT.create()
                    .withIssuer("Full store")
                    .withSubject(usuario.getUsername())
                    .withExpiresAt(Date.from(expiracao(30)))
                    .sign(algorithm);
        } catch (JWTCreationException ex) {
            throw new RegraDeNegocioException("Erro ao gerar token JWT");
        }
    }

    public String gerarRefreshToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("123456");

            return JWT.create()
                    .withIssuer("Full store")
                    .withSubject(usuario.getId().toString())
                    .withExpiresAt(Date.from(expiracao(30)))
                    .sign(algorithm);
        } catch (JWTCreationException ex) {
            throw new RegraDeNegocioException("Erro ao gerar token JWT");
        }
    }

    public String verificarToken(String token) {
        DecodedJWT decodedJWT;
        try {
            Algorithm algorithm = Algorithm.HMAC256("123456");
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("Full store")
                    .build();

            decodedJWT = verifier.verify(token);
            return decodedJWT.getSubject();
        } catch (JWTVerificationException ex) {
            throw new RegraDeNegocioException("Erro a o validar token");
        }
    }

    private Instant expiracao(Integer min) {
        return Instant.now().plus(Duration.ofMinutes(min));
    }

}
