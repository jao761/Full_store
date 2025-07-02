package com.joao.api_vendas_roupas.domain.usuario;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "tb_usuario")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nomeUsuario;
    private String email;
    private String senha;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario() {}

    public Usuario(DadosCadastroUsuarios dados, String senhaCriptografada) {
        this.nomeUsuario = dados.nomeUsuario();
        this.email = dados.email();
        this.senha = senhaCriptografada;

    }

    public void atualizarInformaoces(DadoAtualizarUsuario dados) {
        this.nomeUsuario = dados.nomeUsuario() != null ? dados.nomeUsuario() : this.nomeUsuario;
        this.email = dados.email() != null ? dados.email() : this.email;
    }
}
