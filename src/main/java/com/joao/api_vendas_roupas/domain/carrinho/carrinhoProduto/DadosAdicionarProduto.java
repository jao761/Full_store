package com.joao.api_vendas_roupas.domain.carrinho.carrinhoProduto;

import jakarta.validation.constraints.NotNull;

public record DadosAdicionarProduto(

        @NotNull Long carrinho_id,
        @NotNull Long produto_id,
        @NotNull Integer quantidadeProduto

) {
}
