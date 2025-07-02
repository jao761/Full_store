package com.joao.api_vendas_roupas.domain.usuario;

import com.joao.api_vendas_roupas.domain.carrinho.Carrinho;
import com.joao.api_vendas_roupas.domain.carrinho.CarrinhoRepository;
import com.joao.api_vendas_roupas.infra.exception.RegraDeNegocioException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository repository;
    private final CarrinhoRepository carrinhoRepository;
    private final PasswordEncoder encoder;

    public UsuarioService(UsuarioRepository repository, CarrinhoRepository carrinhoRepository, PasswordEncoder encoder) {
        this.repository = repository;
        this.carrinhoRepository = carrinhoRepository;
        this.encoder = encoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new RegraDeNegocioException("Email nao cadastrado"));
    }

    @Transactional
    public Usuario cadastrarUsuario(@Valid DadosCadastroUsuarios dados) {

        String senhaCriptografada = encoder.encode(dados.senha());
        Usuario usuario = repository.save(new Usuario(dados, senhaCriptografada));
        carrinhoRepository.save(new Carrinho(usuario));
        return usuario;
    }
}
