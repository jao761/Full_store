package com.joao.api_vendas_roupas.domain.carrinho.carrinhoProduto;

public record DadosCarrinhoProduto(

        Long produtoId,
        String nomeProduto,
        Double PrecoUnitario,
        Integer quantidade

) {
    public DadosCarrinhoProduto(CarrinhoProduto carrinhoProduto) {
        this(carrinhoProduto.getId().getProdutoId(), carrinhoProduto.getProduto().getAnuncioNome(),
                carrinhoProduto.getProduto().getAnuncioPreco(), carrinhoProduto.getQuantidade());
    }
}
