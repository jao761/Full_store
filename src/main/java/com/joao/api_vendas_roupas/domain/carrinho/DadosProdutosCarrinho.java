package com.joao.api_vendas_roupas.domain.carrinho;

import com.joao.api_vendas_roupas.domain.carrinho.carrinhoProduto.DadosCarrinhoProduto;

import java.util.List;

public record DadosProdutosCarrinho(

    Long carrinho_id,
    Double valorCompra,
    List<DadosCarrinhoProduto> produtos

) {
    public DadosProdutosCarrinho(Carrinho carrinho) {
        this(carrinho.getId(), carrinho.getValorCompra(), carrinho.getItens().stream().map(DadosCarrinhoProduto::new).toList());
    }
}
