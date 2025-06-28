package com.joao.api_vendas_roupas.controller;

import com.joao.api_vendas_roupas.domain.carrinho.Carrinho;
import com.joao.api_vendas_roupas.domain.carrinho.DadosProdutosCarrinho;
import com.joao.api_vendas_roupas.domain.carrinho.carrinhoProduto.CarrinhoProduto;
import com.joao.api_vendas_roupas.domain.carrinho.CarrinhoService;
import com.joao.api_vendas_roupas.domain.carrinho.carrinhoProduto.DadosAdicionarProduto;
import com.joao.api_vendas_roupas.domain.carrinho.carrinhoProduto.DadosDetalheProdutoCarrinho;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("carrinho")
public class CarrinhoController {

    public final CarrinhoService service;

    public CarrinhoController(CarrinhoService service) {
        this.service = service;
    }

    //adiconar ao carrinho
    @PostMapping
    public ResponseEntity<String> adicionarProduto(@RequestBody @Valid DadosAdicionarProduto dados, UriComponentsBuilder uriBuilder) {
        CarrinhoProduto carrinhoProduto = service.adicionarProduto(dados);
        var uri = uriBuilder.path("/carrinho/{idCarrinho}/produto/{idProduto}").buildAndExpand(
                carrinhoProduto.getId().getCarrinhoId(), carrinhoProduto.getId().getProdutoId()
        ).toUri();
        return ResponseEntity.created(uri).body("Produto adicionado a o carrinho");
    }

    //visualizar produtos no carrinho
    @GetMapping("/{id}")
    public ResponseEntity<DadosProdutosCarrinho> listarCarrinho(@PathVariable Long id) {
        Carrinho carrinho = service.listarCarrinho(id);
        return ResponseEntity.ok(new DadosProdutosCarrinho(carrinho));
    }

    //visualizar produto especifico
    @GetMapping("/{idCarrinho}/produto/{idProduto}")
    public ResponseEntity<DadosDetalheProdutoCarrinho> detalharProdutoCarrinho(@PathVariable Long idCarrinho, @PathVariable Long idProduto) {
        CarrinhoProduto carrinhoProduto = service.detalharProdutoCarrinho(idCarrinho, idProduto);
        return ResponseEntity.ok(new DadosDetalheProdutoCarrinho(carrinhoProduto));
    }

    //remover produto carrinho
    @DeleteMapping("/{idCarrinho}/produto/{idProduto}")
    public ResponseEntity<Void> removerProdutoCarrinho(@PathVariable Long idCarrinho, @PathVariable Long idProduto) {
        service.removerProdutoCarrinho(idCarrinho, idProduto);
        return ResponseEntity.noContent().build();
    }

    //limparCarrinho
    @DeleteMapping("/{idCarrinho}")
    public ResponseEntity<Void> limparCarrinho(@PathVariable Long idCarrinho) {
        service.limparCarrinho(idCarrinho);
        return ResponseEntity.noContent().build();
    }

    //adcionar quantidade produto
    @PatchMapping("/{idCarrinho}/produto/{idProduto}/aumentar")
    public ResponseEntity<DadosDetalheProdutoCarrinho> adcionarQuantidadeProduto(@PathVariable Long idCarrinho, @PathVariable Long idProduto) {
        CarrinhoProduto carrinhoProduto = service.adcionarQuantidadeProduto(idCarrinho, idProduto);
        return ResponseEntity.ok(new DadosDetalheProdutoCarrinho(carrinhoProduto));
    }

    //diminuir quantidade produto
    @PatchMapping("/{idCarrinho}/produto/{idProduto}/diminuir")
    public ResponseEntity<DadosDetalheProdutoCarrinho> diminuirQuantidadeProduto(@PathVariable Long idCarrinho, @PathVariable Long idProduto) {
        CarrinhoProduto carrinhoProduto = service.diminuirQuantidadeProduto(idCarrinho, idProduto);
        return ResponseEntity.ok(new DadosDetalheProdutoCarrinho(carrinhoProduto));
    }

}
