package com.joao.api_vendas_roupas.domain.carrinho.carrinhoProduto;

import com.joao.api_vendas_roupas.domain.carrinho.Carrinho;
import com.joao.api_vendas_roupas.domain.produto.Produto;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;

@Entity
@Table(name = "tb_carrinho_produto")
public class CarrinhoProduto {

    @EmbeddedId
    private CarrinhoProdutoId id = new CarrinhoProdutoId();

    @ManyToOne
    @MapsId("carrinhoId")
    @JoinColumn(name = "carrinho_id")
    private Carrinho carrinho;

    @ManyToOne
    @MapsId("produtoId")
    @JoinColumn(name = "produto_id")
    private Produto produto;

    private Integer quantidade;

    public CarrinhoProduto() {}

    public CarrinhoProduto(Integer quantidadeInformada, Carrinho carrinho, Produto produto) {
            this.carrinho = carrinho;
            this.produto = produto;
            this.quantidade = quantidadeInformada;
    }

    public CarrinhoProdutoId getId() {
        return id;
    }

    public void setId(CarrinhoProdutoId id) {
        this.id = id;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }
}
