package com.joao.api_vendas_roupas.domain.carrinho.carrinhoProduto;

import com.joao.api_vendas_roupas.domain.produto.DadosDetalheProduto;

public record DadosDetalheProdutoCarrinho(

        Integer quantidade,
        DadosDetalheProduto dados


) {
    public DadosDetalheProdutoCarrinho(CarrinhoProduto carrinhoProduto) {
        this(carrinhoProduto.getQuantidade(), new DadosDetalheProduto(carrinhoProduto.getProduto()));
    }
}
