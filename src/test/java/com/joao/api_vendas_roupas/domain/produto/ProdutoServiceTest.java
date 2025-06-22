package com.joao.api_vendas_roupas.domain.produto;

import com.joao.api_vendas_roupas.infra.exception.RegraDeNegocioException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @InjectMocks
    private ProdutoService service;

    @Mock
    private UploadImagem upload;

    @Mock
    private Produto produto;

    @Mock
    private ProdutoRepository repository;

    @Captor
    private ArgumentCaptor<Produto> produtoCaptor;

    @Mock
    private Pageable paginacao;

    @Mock
    private Page<Produto> produtos;

    MockMultipartFile arquivoFoto = new MockMultipartFile("arquivoFoto", "foto.jpg",
            "image/jpeg", "conteudo da imagem".getBytes());

    private DadosCadastroProduto dadosCadastroProduto = new DadosCadastroProduto(
            "Camiseta Polo",
            "Camiseta 100% algodão",
            89.90,
            arquivoFoto,
            "MarcaXYZ",
            50L
    );

    @Test
    public void cadastroDeveSerExecutadoCorretamente() {

        //arrenge
        when(upload.salvarImagem(dadosCadastroProduto.arquivoFoto(), null)).thenReturn("foto_salva.jpg");

        //act
        service.cadastrarProduto(dadosCadastroProduto);

        //assert
        verify(repository).save(produtoCaptor.capture());
        Produto produtoSalvo = produtoCaptor.getValue();

        assertEquals("Camiseta Polo", produtoSalvo.getAnuncioNome());
        assertEquals("MarcaXYZ", produtoSalvo.getAnuncioMarca());
        assertEquals(89.90, produtoSalvo.getAnuncioPreco());
        assertEquals("foto_salva.jpg", produtoSalvo.getAnuncioFoto());
        assertEquals(50, produtoSalvo.getAnuncioQuantidade());
    }


    @Test
    public void listageDeProdutosDeveSerExecutadaCorretamente() {
        // arrange
        Produto produto = new Produto(
                dadosCadastroProduto, "foto.jpg"
        );

        List<Produto> lista = List.of(produto);
        Page<Produto> page = new org.springframework.data.domain.PageImpl<>(lista);
        Pageable pageable = org.springframework.data.domain.PageRequest.of(0, 10);

        when(repository.findAll(pageable)).thenReturn(page);

        // act
        Page<DadosListagemProduto> resultado = service.listarProduto(pageable);

        // assert
        assertEquals(1, resultado.getTotalElements());
        DadosListagemProduto produtoRetornado = resultado.getContent().get(0);
        assertEquals("Camiseta Polo", produtoRetornado.anuncioNome());
        assertEquals(89.90, produtoRetornado.anuncioPreco());

        verify(repository).findAll(pageable);
    }

    @Test
    public void detalhamentoDoProdutosDeveSerExecutadoCorretamente() {
        // arrange
        Produto produto = new Produto(dadosCadastroProduto, "foto.jpg");
        produto.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(produto));

        // act
        Produto resultado = service.detalheProduto(1L);
        DadosDetalheProduto produtoRetornado = new DadosDetalheProduto(resultado);

        // assert
        assertEquals("Camiseta Polo", produtoRetornado.anuncioNome());
        assertEquals("Camiseta 100% algodão", produtoRetornado.anuncioDescricao());
        assertEquals(89.90, produtoRetornado.anuncioPreco());
        assertEquals("foto.jpg", produtoRetornado.arquivoFoto());
        assertEquals("MarcaXYZ", produtoRetornado.anuncioMarca());
        assertEquals(50, produtoRetornado.anuncioQuantidade());
    }

    @Test
    public void detalheProdutoDeveLancarExcecaoSeNaoEncontrado() {
        // arrange
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // act + assert
        RegraDeNegocioException ex = assertThrows(RegraDeNegocioException.class, () -> {
            service.detalheProduto(99L);
        });

        assertEquals("Produto nao encontrado", ex.getMessage());
    }


    @Test
    public void atualizacaoDoProdutosDeveSerExecutadoCorretamente() {
        // arrange
        DadosAlteracaoProduto dadosAlteracao = new DadosAlteracaoProduto(
                "Camiseta Polo",
                "Camiseta 100% algodão",
                89.90,
                arquivoFoto,
                "MarcaXYZ",
                50L
        );

        Produto produto = new Produto(dadosCadastroProduto, "foto.jpg");
        produto.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(produto));
        when(upload.salvarImagem(arquivoFoto, "foto.jpg")).thenReturn("foto.jpg");

        // act
        Produto resultado = service.atualizarProduto(1L, dadosAlteracao);
        DadosDetalheProduto produtoRetornado = new DadosDetalheProduto(resultado);

        // assert
        assertEquals("Camiseta Polo", produtoRetornado.anuncioNome());
        assertEquals("Camiseta 100% algodão", produtoRetornado.anuncioDescricao());
        assertEquals(89.90, produtoRetornado.anuncioPreco());
        assertEquals("foto.jpg", produtoRetornado.arquivoFoto());
        assertEquals("MarcaXYZ", produtoRetornado.anuncioMarca());
        assertEquals(50, produtoRetornado.anuncioQuantidade());
    }

    @Test
    public void atualizarProdutoDeveLancarExcecaoSeNaoEncontrado() {
        // arrange
        DadosAlteracaoProduto dados = new DadosAlteracaoProduto(
                "Novo Nome", "Nova Descrição", 100.0, arquivoFoto, "NovaMarca", 20L
        );

        when(repository.findById(42L)).thenReturn(Optional.empty());

        // act + assert
        RegraDeNegocioException ex = assertThrows(RegraDeNegocioException.class, () -> {
            service.atualizarProduto(42L, dados);
        });

        assertEquals("Produto nao encontrado", ex.getMessage());
    }

    @Test
    public void atualizarProdutoNaoDeveAtualizarImagemSeNaoForInformada() {
        // arrange
        DadosAlteracaoProduto dados = new DadosAlteracaoProduto(
                "Camiseta Slim",
                "Nova descrição",
                99.99,
                null, // Sem nova imagem
                "NovaMarca",
                30L
        );

        Produto produto = new Produto(dadosCadastroProduto, "foto_antiga.jpg");
        produto.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(produto));

        // act
        Produto resultado = service.atualizarProduto(1L, dados);

        // assert
        assertEquals("foto_antiga.jpg", resultado.getAnuncioFoto()); // Imagem não alterada
    }

    @Test
    public void deletarProdutoDeveDesativarProdutoCorretamente() {
        // arrange
        Produto produto = new Produto(dadosCadastroProduto, "foto.jpg");
        produto.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(produto));

        // act
        service.deletarProduto(1L);

        // assert
        assertFalse(produto.getAtivo()); // Produto deve estar desativado (ativo = false)
    }

    @Test
    public void deletarProdutoDeveLancarExcecaoSeNaoEncontrado() {
        // arrange
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // act + assert
        RegraDeNegocioException ex = assertThrows(RegraDeNegocioException.class, () -> {
            service.deletarProduto(99L);
        });

        assertEquals("Produto nao encontrado", ex.getMessage());
    }


}