package com.joao.api_vendas_roupas.domain.carrinho;

import com.joao.api_vendas_roupas.domain.carrinho.carrinhoProduto.CarrinhoProduto;
import com.joao.api_vendas_roupas.domain.carrinho.carrinhoProduto.CarrinhoProdutoId;
import com.joao.api_vendas_roupas.domain.carrinho.carrinhoProduto.CarrinhoProdutoRepository;
import com.joao.api_vendas_roupas.domain.carrinho.carrinhoProduto.DadosAdicionarProduto;
import com.joao.api_vendas_roupas.domain.produto.Produto;
import com.joao.api_vendas_roupas.domain.produto.ProdutoRepository;
import com.joao.api_vendas_roupas.infra.exception.RegraDeNegocioException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CarrinhoService {

    public final CarrinhoProdutoRepository carrinhoProdutoRepository;
    public final ProdutoRepository produtoRepository;
    public final CarrinhoRepository carrinhoRepository;

    public CarrinhoService(CarrinhoProdutoRepository carrinhoProdutoRepository, ProdutoRepository produtoRepository, CarrinhoRepository carrinhoRepository) {
        this.carrinhoProdutoRepository = carrinhoProdutoRepository;
        this.produtoRepository = produtoRepository;
        this.carrinhoRepository = carrinhoRepository;
    }

    @Transactional
    public CarrinhoProduto adicionarProduto(DadosAdicionarProduto dados) {
        Produto produto = produtoRepository.findById(dados.produto_id())
                .orElseThrow(() -> new RegraDeNegocioException("Produto nao encontrado"));

        Carrinho carrinho = carrinhoRepository.findById(dados.carrinho_id())
                .orElseThrow(() -> new RegraDeNegocioException("Carrinho nao encontrado"));

        if (dados.quantidadeProduto() > produto.getAnuncioQuantidade())
            throw new RegraDeNegocioException("Quantidade informada ivalida");

        CarrinhoProduto carrinhoProduto = carrinhoProdutoRepository.save(new CarrinhoProduto(dados.quantidadeProduto(), carrinho, produto));
        carrinho.adcionarItens(carrinhoProduto);
        return carrinhoProduto;
    }

    public Carrinho listarCarrinho(Long id) {
        return carrinhoRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Carrinho nao encontrado"));
    }

    public CarrinhoProduto detalharProdutoCarrinho(Long idCarrinho, Long idProduto) {

        CarrinhoProdutoId carrinhoProdutoId = new CarrinhoProdutoId(idCarrinho, idProduto);
        return carrinhoProdutoRepository.findById(carrinhoProdutoId)
                .orElseThrow(() -> new RegraDeNegocioException("Prduto nao encontrado no carrinho"));
    }

    @Transactional
    public void removerProdutoCarrinho(Long idCarrinho, Long idProduto) {

        Carrinho carrinho = carrinhoRepository.findById(idCarrinho)
                .orElseThrow(() -> new RegraDeNegocioException("Carrinho nao encontrado"));

        CarrinhoProdutoId carrinhoProdutoId = new CarrinhoProdutoId(idCarrinho, idProduto);
        CarrinhoProduto carrinhoProduto = carrinhoProdutoRepository.findById(carrinhoProdutoId)
                .orElseThrow(() -> new RegraDeNegocioException("Prduto nao encontrado no carrinho"));

        carrinho.removerItens(carrinhoProduto);
        carrinhoProdutoRepository.delete(carrinhoProduto);
    }

    @Transactional
    public void limparCarrinho(Long idCarrinho) {

        Carrinho carrinho = carrinhoRepository.findById(idCarrinho)
                .orElseThrow(() -> new RegraDeNegocioException("Carrinho nao encontrado"));

        carrinho.limparCarrinho(idCarrinho);
    }

    @Transactional
    public CarrinhoProduto adcionarQuantidadeProduto(Long idCarrinho, Long idProduto) {

        Carrinho carrinho = carrinhoRepository.findById(idCarrinho)
                .orElseThrow(() -> new RegraDeNegocioException("Carrinho nao encontrado"));

        CarrinhoProdutoId carrinhoProdutoId = new CarrinhoProdutoId(idCarrinho, idProduto);
        CarrinhoProduto carrinhoProduto = carrinhoProdutoRepository.findById(carrinhoProdutoId)
                .orElseThrow(() -> new RegraDeNegocioException("Prduto nao encontrado no carrinho"));

        Integer quantidade = carrinhoProduto.getQuantidade();
        carrinhoProduto.setQuantidade(quantidade += 1);
        carrinho.alterarValor(carrinhoProduto.getQuantidade(), carrinhoProduto.getProduto().getAnuncioPreco());
        return carrinhoProduto;
    }

    @Transactional
    public CarrinhoProduto diminuirQuantidadeProduto(Long idCarrinho, Long idProduto) {

        Carrinho carrinho = carrinhoRepository.findById(idCarrinho)
                .orElseThrow(() -> new RegraDeNegocioException("Carrinho nao encontrado"));

        CarrinhoProdutoId carrinhoProdutoId = new CarrinhoProdutoId(idCarrinho, idProduto);
        CarrinhoProduto carrinhoProduto = carrinhoProdutoRepository.findById(carrinhoProdutoId)
                .orElseThrow(() -> new RegraDeNegocioException("Prduto nao encontrado no carrinho"));

        Integer quantidade = carrinhoProduto.getQuantidade();
        carrinhoProduto.setQuantidade(quantidade -= 1);
        carrinho.alterarValor(carrinhoProduto.getQuantidade(), carrinhoProduto.getProduto().getAnuncioPreco());
        return carrinhoProduto;
    }


}
