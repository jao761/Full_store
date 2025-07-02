package com.joao.api_vendas_roupas.domain.authentication;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.joao.api_vendas_roupas.domain.usuario.DadosCadastroUsuarios;
import com.joao.api_vendas_roupas.domain.usuario.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @InjectMocks
    private TokenService service;

    DadosCadastroUsuarios dados = new DadosCadastroUsuarios(
            "Joao",
            "joao@email.com",
            "teste123"
    );

    Usuario usuario = new Usuario(dados, dados.senha());



    @Test
    void deveGerarAccessToken() {
        String token = service.gerarAccessToken(usuario);
        assertEquals("joao@email.com", service.verificarToken(token));
    }

    @Test
    void deveGerarRefreshToken() {
        usuario.setId(1L);
        String token = service.gerarRefreshToken(usuario);
        assertEquals("1", service.verificarToken(token));
    }

    @Test
    void verificarToken() {
        String token = service.gerarAccessToken(usuario);
        String getSubect = service.verificarToken(token);
        assertEquals("joao@email.com", getSubect);
    }

    @Test
    void deveLancarExcecaoParaTokenInvalido() {
        String tokenInvalido = "token.invalido.fake";
        assertThrows(Exception.class, () -> service.verificarToken(tokenInvalido));
    }


    @Test
    void tokenTemFormatoJwtValido() {
        String token = service.gerarAccessToken(usuario);
        assertTrue(token.split("\\.").length == 3, "Token JWT deve ter três partes separadas por ponto.");
    }


}