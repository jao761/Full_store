package com.joao.api_vendas_roupas.domain.produto;

public record DadosListagemProduto(

        String anuncioNome,
        Double anuncioPreco

) {
    public DadosListagemProduto(Produto produto) {
        this(produto.getAnuncioNome(), produto.getAnuncioPreco());
    }
}
