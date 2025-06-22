package com.joao.api_vendas_roupas.domain.produto;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_produto")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String anuncioNome;
    private String anuncioDescricao;
    private Double anuncioPreco;
    private String anuncioFoto;
    private String anuncioMarca;
    private Long anuncioQuantidade;
    private Boolean ativo;

    private Produto() {}

    public Produto(DadosCadastroProduto dados, String nomeFoto) {
        this.anuncioNome = dados.anuncioNome();
        this.anuncioDescricao = dados.anuncioDescricao();
        this.anuncioPreco = dados.anuncioPreco();
        this.anuncioFoto = nomeFoto;
        this.anuncioMarca = dados.anuncioMarca();
        this.anuncioQuantidade = dados.anuncioQuantidade();
        this.ativo = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnuncioNome() {
        return anuncioNome;
    }

    public void setAnuncioNome(String anuncioNome) {
        this.anuncioNome = anuncioNome;
    }

    public String getAnuncioDescricao() {
        return anuncioDescricao;
    }

    public void setAnuncioDescricao(String anuncioDescricao) {
        this.anuncioDescricao = anuncioDescricao;
    }

    public Double getAnuncioPreco() {
        return anuncioPreco;
    }

    public void setAnuncioPreco(Double anuncioPreco) {
        this.anuncioPreco = anuncioPreco;
    }

    public String getAnuncioFoto() {
        return anuncioFoto;
    }

    public void setAnuncioFoto(String anuncioFoto) {
        this.anuncioFoto = anuncioFoto;
    }

    public String getAnuncioMarca() {
        return anuncioMarca;
    }

    public void setAnuncioMarca(String anuncioMarca) {
        this.anuncioMarca = anuncioMarca;
    }

    public Long getAnuncioQuantidade() {
        return anuncioQuantidade;
    }

    public void setAnuncioQuantidade(Long anuncioQuantidade) {
        this.anuncioQuantidade = anuncioQuantidade;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void atualizarProduto(DadosAlteracaoProduto dados, String nomeFoto) {
        this.anuncioNome = dados.anuncioNome() != null ? dados.anuncioNome() : this.anuncioNome;
        this.anuncioDescricao = dados.anuncioDescricao() != null ? dados.anuncioDescricao() : this.anuncioDescricao;
        this.anuncioPreco = dados.anuncioPreco() != null ? dados.anuncioPreco() : this.anuncioPreco;
        this.anuncioFoto = nomeFoto != null ? nomeFoto : this.anuncioFoto;
        this.anuncioMarca = dados.anuncioMarca() != null ? dados.anuncioMarca() : this.anuncioMarca;
        this.anuncioQuantidade = dados.anuncioQuantidade() != null ? dados.anuncioQuantidade() : this.anuncioQuantidade;;
    }

    public void deletarProduto() {
        this.ativo = false;
    }
}
