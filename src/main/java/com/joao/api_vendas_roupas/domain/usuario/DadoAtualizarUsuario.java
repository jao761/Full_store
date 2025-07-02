package com.joao.api_vendas_roupas.domain.usuario;

public record DadoAtualizarUsuario(

        String nomeUsuario,
        String email

) {
    public DadoAtualizarUsuario(Usuario usuario) {
        this(usuario.getNomeUsuario(), usuario.getUsername());
    }
}
