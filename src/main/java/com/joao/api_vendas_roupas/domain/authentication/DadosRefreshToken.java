package com.joao.api_vendas_roupas.domain.authentication;

import jakarta.validation.constraints.NotBlank;

public record DadosRefreshToken(@NotBlank String refrehToken) {
}
