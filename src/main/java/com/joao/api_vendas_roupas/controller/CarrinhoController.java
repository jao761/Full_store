package com.joao.api_vendas_roupas.controller;

import com.joao.api_vendas_roupas.domain.carrinho.Carrinho;
import com.joao.api_vendas_roupas.domain.carrinho.CarrinhoService;
import com.joao.api_vendas_roupas.domain.carrinho.DadosProdutosCarrinho;
import com.joao.api_vendas_roupas.domain.carrinho.carrinhoProduto.CarrinhoProduto;
import com.joao.api_vendas_roupas.domain.carrinho.carrinhoProduto.DadosAdicionarProduto;
import com.joao.api_vendas_roupas.domain.carrinho.carrinhoProduto.DadosDetalheProdutoCarrinho;
import com.joao.api_vendas_roupas.domain.usuario.Usuario;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<String> adicionarProduto(@RequestBody @Valid DadosAdicionarProduto dados, @AuthenticationPrincipal Usuario logado,  UriComponentsBuilder uriBuilder) {
        CarrinhoProduto carrinhoProduto = service.adicionarProduto(dados, logado);
        var uri = uriBuilder.path("/carrinho/produto/{idProduto}").buildAndExpand(
                carrinhoProduto.getId().getCarrinhoId(), carrinhoProduto.getId().getProdutoId()
        ).toUri();
        return ResponseEntity.created(uri).body("Produto adicionado a o carrinho");
    }

    //visualizar produtos no carrinho
    @GetMapping
    public ResponseEntity<DadosProdutosCarrinho> listarCarrinho(@AuthenticationPrincipal Usuario logado) {
        Carrinho carrinho = service.listarCarrinho(logado);
        return ResponseEntity.ok(new DadosProdutosCarrinho(carrinho));
    }

    //visualizar produto especifico
    @GetMapping("/produto/{idProduto}")
    public ResponseEntity<DadosDetalheProdutoCarrinho> detalharProdutoCarrinho(@AuthenticationPrincipal Usuario logado, @PathVariable Long idProduto) {
        CarrinhoProduto carrinhoProduto = service.detalharProdutoCarrinho(logado, idProduto);
        return ResponseEntity.ok(new DadosDetalheProdutoCarrinho(carrinhoProduto));
    }

    //remover produto carrinho
    @DeleteMapping("/produto/{idProduto}")
    public ResponseEntity<Void> removerProdutoCarrinho(@AuthenticationPrincipal Usuario logado, @PathVariable Long idCarrinho, @PathVariable Long idProduto) {
        service.removerProdutoCarrinho(logado, idProduto);
        return ResponseEntity.noContent().build();
    }

    //limparCarrinho
    @DeleteMapping()
    public ResponseEntity<Void> limparCarrinho(@AuthenticationPrincipal Usuario logado) {
        service.limparCarrinho(logado);
        return ResponseEntity.noContent().build();
    }

    //adcionar quantidade produto
    @PatchMapping("/produto/{idProduto}/aumentar")
    public ResponseEntity<DadosDetalheProdutoCarrinho> adcionarQuantidadeProduto(@AuthenticationPrincipal Usuario logado, @PathVariable Long idCarrinho, @PathVariable Long idProduto) {
        CarrinhoProduto carrinhoProduto = service.adcionarQuantidadeProduto(logado, idProduto);
        return ResponseEntity.ok(new DadosDetalheProdutoCarrinho(carrinhoProduto));
    }

    //diminuir quantidade produto
    @PatchMapping("/{idCarrinho}/produto/{idProduto}/diminuir")
    public ResponseEntity<DadosDetalheProdutoCarrinho> diminuirQuantidadeProduto(@AuthenticationPrincipal Usuario logado ,@PathVariable Long idCarrinho, @PathVariable Long idProduto) {
        CarrinhoProduto carrinhoProduto = service.diminuirQuantidadeProduto(logado, idProduto);
        return ResponseEntity.ok(new DadosDetalheProdutoCarrinho(carrinhoProduto));
    }

}
