package com.joao.api_vendas_roupas.domain.produto;

import com.joao.api_vendas_roupas.infra.exception.RegraDeNegocioException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProdutoService {

    private final ProdutoRepository repository;
    private final UploadImagem upload;

    public ProdutoService(ProdutoRepository repository, UploadImagem upload) {
        this.repository = repository;
        this.upload = upload;
    }

    @Transactional
    public Produto cadastrarProduto(DadosCadastroProduto dados) {

        String nomeFoto = upload.salvarImagem(dados.arquivoFoto(), null);

        Produto produto = new Produto(dados, nomeFoto);
        return repository.save(produto);
    }

    public Page<DadosListagemProduto> listarProduto (Pageable paginacao){
        Page<Produto> produtos = repository.findAll(paginacao);

        if(produtos.isEmpty()) {
            throw new RegraDeNegocioException("Sem produtos encotrados");
        }
        return produtos.map(DadosListagemProduto::new);
    }

    public Produto detalheProduto(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Produto nao encontrado"));
    }

    @Transactional
    public Produto atualizarProduto(Long id, DadosAlteracaoProduto dados) {
        Produto produto = repository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Produto nao encontrado"));

        String nomeFoto = null;
        if (dados.arquivoFoto() != null)
            nomeFoto = upload.salvarImagem(dados.arquivoFoto(), produto.getAnuncioFoto());


        produto.atualizarProduto(dados, nomeFoto);
        return produto;
    }

    @Transactional
    public void deletarProduto(Long id) {
        Produto produto = repository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Produto nao encontrado"));
        produto.deletarProduto();
    }
}
