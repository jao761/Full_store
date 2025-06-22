package com.joao.api_vendas_roupas.domain.produto;

import org.springframework.web.multipart.MultipartFile;

public record DadosAlteracaoProduto(

        String anuncioNome,
        String anuncioDescricao,
        Double anuncioPreco,
        MultipartFile arquivoFoto,
        String anuncioMarca,
        Long anuncioQuantidade

) {
    public DadosAlteracaoProduto(Produto produto) {
        this(produto.getAnuncioNome(), produto.getAnuncioDescricao(), produto.getAnuncioPreco(), null, produto.getAnuncioMarca(), produto.getAnuncioQuantidade());
    }
}
