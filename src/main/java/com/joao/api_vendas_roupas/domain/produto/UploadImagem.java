package com.joao.api_vendas_roupas.domain.produto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Component
public class UploadImagem {

    @Value("${caminho.imagens}")
    private String pasta;

    public String salvarImagem(MultipartFile imagem, String arqruivoAntigo) {

        String nomeFoto = imagem.getOriginalFilename();
        String extensao = nomeFoto.substring(nomeFoto.lastIndexOf("."));
        String novoNome = UUID.randomUUID().toString() + extensao;

        Path destino = Paths.get(pasta, novoNome);

        try {
            if(arqruivoAntigo != null && !arqruivoAntigo.isEmpty()) {
                Path caminhoAntigo = Paths.get(pasta, arqruivoAntigo);
                if (Files.exists(caminhoAntigo)){
                    Files.delete(caminhoAntigo);
                }
            }

            Files.copy(imagem.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return novoNome;
    }

}
