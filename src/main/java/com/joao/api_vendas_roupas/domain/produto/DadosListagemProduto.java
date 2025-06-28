package com.joao.api_vendas_roupas.domain.produto;

public record DadosListagemProduto(

        Long id,
        String anuncioNome,
        Double anuncioPreco

) {
    public DadosListagemProduto(Produto produto) {
        this(produto.getId(), produto.getAnuncioNome(), produto.getAnuncioPreco());
    }
}
