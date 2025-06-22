package com.joao.api_vendas_roupas.controller;

import com.joao.api_vendas_roupas.domain.produto.*;
import jakarta.persistence.Id;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("produto")
public class ProdutoController {

    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    //Nesse codigo se usa o form data para subir uma imagem junto com os dados.
    //No futuro com um servidor-cliente talvez nao seje mais nescessario.

    @PostMapping("/cadastrar")
    public ResponseEntity<String> cadastrarProduto(@ModelAttribute @Valid DadosCadastroProduto dados, UriComponentsBuilder uriBuilder) {
        Produto produto = service.cadastrarProduto(dados);
        var uri = uriBuilder.path("/produto/{id}/").buildAndExpand(produto.getId()).toUri();
        return ResponseEntity.created(uri).body("Produto cadastrado com sucesso");
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemProduto>> listarProduto(Pageable paginacao) {
        var pagina = service.listarProduto(paginacao);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalheProduto> detalheProduto(@PathVariable Long id) {
        Produto produto = service.detalheProduto(id);
        return ResponseEntity.ok(new DadosDetalheProduto(produto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DadosAlteracaoProduto> atualizarProduto(@PathVariable Long id, @ModelAttribute DadosAlteracaoProduto dados) {
        Produto produto = service.atualizarProduto(id, dados);
        return ResponseEntity.ok(new DadosAlteracaoProduto(produto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable Long id) {
        service.deletarProduto(id);
        return ResponseEntity.noContent().build();
    }

}
