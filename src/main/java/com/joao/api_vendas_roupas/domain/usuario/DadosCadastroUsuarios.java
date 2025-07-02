package com.joao.api_vendas_roupas.domain.usuario;

import jakarta.validation.constraints.NotBlank;
import org.aspectj.weaver.ast.Not;

public record DadosCadastroUsuarios(

        @NotBlank String nomeUsuario,
        @NotBlank String email,
        @NotBlank String senha

) {
}
