package com.joao.api_vendas_roupas.domain.carrinho.carrinhoProduto;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CarrinhoProdutoId implements Serializable {

    private Long carrinhoId;
    private Long produtoId;

    public CarrinhoProdutoId() {}

    public CarrinhoProdutoId(Long carrinhoId, Long produtoId) {
        this.carrinhoId = carrinhoId;
        this.produtoId = produtoId;
    }

    public Long getCarrinhoId() {
        return carrinhoId;
    }

    public void setCarrinhoId(Long carrinhoId) {
        this.carrinhoId = carrinhoId;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        CarrinhoProdutoId that = (CarrinhoProdutoId) object;
        return Objects.equals(carrinhoId, that.carrinhoId) && Objects.equals(produtoId, that.produtoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(carrinhoId, produtoId);
    }
}
