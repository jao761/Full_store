package com.joao.api_vendas_roupas.domain.produto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record DadosCadastroProduto(

        @NotBlank String anuncioNome,
        String anuncioDescricao,
        @NotNull Double anuncioPreco,
        @NotNull MultipartFile arquivoFoto,
        @NotBlank String anuncioMarca,
        @NotNull Long anuncioQuantidade

) {
}
