package com.joao.api_vendas_roupas.domain.produto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record DadosDetalheProduto(

        String anuncioNome,
        String anuncioDescricao,
        Double anuncioPreco,
        String arquivoFoto,
        String anuncioMarca,
        Long anuncioQuantidade

) {
    public DadosDetalheProduto(Produto produto) {
        this(produto.getAnuncioNome(), produto.getAnuncioDescricao(), produto.getAnuncioPreco(),
                produto.getAnuncioFoto(), produto.getAnuncioMarca(), produto.getAnuncioQuantidade());
    }
}
