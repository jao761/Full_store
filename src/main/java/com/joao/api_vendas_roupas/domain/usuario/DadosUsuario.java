package com.joao.api_vendas_roupas.domain.usuario;

public record DadosUsuario(

        Long id,
        String nomeUsuario,
        String email

) {
    public DadosUsuario(Usuario usuario) {
        this(usuario.getId(), usuario.getNomeUsuario(), usuario.getUsername());
    }
}
