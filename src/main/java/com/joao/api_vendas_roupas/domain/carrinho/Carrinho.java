package com.joao.api_vendas_roupas.domain.carrinho;

import com.joao.api_vendas_roupas.domain.carrinho.carrinhoProduto.CarrinhoProduto;
import com.joao.api_vendas_roupas.domain.usuario.Usuario;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_carrinho")
public class Carrinho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double valorCompra;

    @OneToMany(mappedBy = "carrinho", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarrinhoProduto> itens = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public Carrinho(Usuario usuario) {
        this.valorCompra = 0.0;
        this.usuario = usuario;
    }

    protected Carrinho() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getValorCompra() {
        return valorCompra;
    }

    public List<CarrinhoProduto> getItens() {
        return itens;
    }

    public void setItens(List<CarrinhoProduto> itens) {
        this.itens = itens;
    }

    public void adcionarItens(CarrinhoProduto carrinhoProduto) {
        this.itens.add(carrinhoProduto);
        this.valorCompra += carrinhoProduto.getProduto().getAnuncioPreco() * carrinhoProduto.getQuantidade();
    }

    public void removerItens(CarrinhoProduto carrinhoProduto) {
        var valorRemovido = this.valorCompra += carrinhoProduto.getProduto().getAnuncioPreco() * carrinhoProduto.getQuantidade();
        this.valorCompra -= valorRemovido;
    }

    public void limparCarrinho() {
        this.itens.clear();
        this.valorCompra = 0.0;
    }

    public void alterarValor(Integer quantidade, Double precoUnitario) {
        this.valorCompra = quantidade * precoUnitario;
    }

}
