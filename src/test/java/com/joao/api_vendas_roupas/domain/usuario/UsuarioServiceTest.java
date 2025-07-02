package com.joao.api_vendas_roupas.domain.usuario;

import com.joao.api_vendas_roupas.domain.carrinho.CarrinhoRepository;
import com.joao.api_vendas_roupas.infra.exception.RegraDeNegocioException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private CarrinhoRepository carrinhoRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void deveCarregarUsuarioPorEmailComSucesso() {
        // Arrange
        String email = "joao@email.com";
        Usuario usuario = mock(Usuario.class);
        when(usuarioRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.of(usuario));

        // Act
        UserDetails result = usuarioService.loadUserByUsername(email);

        // Assert
        assertNotNull(result);
        verify(usuarioRepository).findByEmailIgnoreCase(email);
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        // Arrange
        String email = "naoexiste@email.com";
        when(usuarioRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.empty());

        // Act & Assert
        RegraDeNegocioException ex = assertThrows(
                RegraDeNegocioException.class,
                () -> usuarioService.loadUserByUsername(email)
        );
        assertEquals("Email nao cadastrado", ex.getMessage());
        verify(usuarioRepository).findByEmailIgnoreCase(email);
    }

    @Test
    void deveCadastrarUsuarioComSenhaCriptografada() {
        // Arrange
        DadosCadastroUsuarios dados = new DadosCadastroUsuarios(
                "João", "joao@email.com", "123456"
        );

        String senhaCriptografada = "senhaCripto123";
        when(passwordEncoder.encode(dados.senha())).thenReturn(senhaCriptografada);
        when(usuarioRepository.save(any(Usuario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0)); // retorna o próprio usuário salvo

        // Act
        Usuario usuarioCriado = usuarioService.cadastrarUsuario(dados);

        // Assert
        assertNotNull(usuarioCriado);
        assertEquals(senhaCriptografada, usuarioCriado.getPassword());

        verify(passwordEncoder).encode("123456");
        verify(usuarioRepository).save(any(Usuario.class));
        verify(carrinhoRepository).save(any());
    }
}
