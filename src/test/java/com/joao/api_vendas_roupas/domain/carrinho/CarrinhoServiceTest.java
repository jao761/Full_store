package com.joao.api_vendas_roupas.domain.carrinho;

import com.joao.api_vendas_roupas.domain.carrinho.carrinhoProduto.CarrinhoProduto;
import com.joao.api_vendas_roupas.domain.carrinho.carrinhoProduto.CarrinhoProdutoId;
import com.joao.api_vendas_roupas.domain.carrinho.carrinhoProduto.CarrinhoProdutoRepository;
import com.joao.api_vendas_roupas.domain.carrinho.carrinhoProduto.DadosAdicionarProduto;
import com.joao.api_vendas_roupas.domain.produto.Produto;
import com.joao.api_vendas_roupas.domain.produto.ProdutoRepository;
import com.joao.api_vendas_roupas.infra.exception.RegraDeNegocioException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarrinhoServiceTest {

    @InjectMocks
    private CarrinhoService service;

    private DadosAdicionarProduto dados = new DadosAdicionarProduto(1L, 1L, 2);

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private CarrinhoRepository carrinhoRepository;

    @Mock
    private CarrinhoProdutoRepository carrinhoProdutoRepository;

    @Mock
    private Produto produto;

    @Mock
    private Carrinho carrinho;

    @Captor
    private ArgumentCaptor<CarrinhoProduto> carrinhoProdutoCaptor;

    CarrinhoProduto carrinhoProdutoMock = new CarrinhoProduto(dados.quantidadeProduto(), carrinho, produto);
    CarrinhoProdutoId id = new CarrinhoProdutoId(1L, 1L);

    @Test
    public void produtoDeveSerSalvoClasseCarrinhoProduto() {
        // ARRANGE
        when(produtoRepository.findById(dados.produto_id())).thenReturn(Optional.of(produto));
        when(carrinhoRepository.findById(dados.carrinho_id())).thenReturn(Optional.of(carrinho));
        when(produto.getAnuncioQuantidade()).thenReturn(2L);

        CarrinhoProduto carrinhoProdutoMock = new CarrinhoProduto(dados.quantidadeProduto(), carrinho, produto);
        CarrinhoProdutoId id = new CarrinhoProdutoId(1L, 1L);
        carrinhoProdutoMock.setId(id);
        when(carrinhoProdutoRepository.save(Mockito.any())).thenReturn(carrinhoProdutoMock);

        // ACT
        CarrinhoProduto result = service.adicionarProduto(dados);

        // ASSERT
        verify(carrinhoProdutoRepository).save(carrinhoProdutoCaptor.capture());
        CarrinhoProduto capturado = carrinhoProdutoCaptor.getValue();

        assertEquals(2, capturado.getQuantidade());
        assertEquals(1L, result.getId().getProdutoId());
        assertEquals(1L, result.getId().getCarrinhoId());
        assertEquals(2, result.getQuantidade());
    }

    @Test
    public void deveDevolverListagemDeCarrinhoCorretamente() {
        // ARRANGE
        when(carrinhoRepository.findById(dados.carrinho_id())).thenReturn(Optional.of(carrinho));
        when(carrinho.getId()).thenReturn(1L);
        when(carrinho.getItens()).thenReturn(List.of(carrinhoProdutoMock));
        when(carrinho.getValorCompra()).thenReturn(55.55);

        // ACT
        Carrinho result = service.listarCarrinho(1L);

        // ASSERT
        assertEquals(1L, result.getId());
        assertEquals(55.55, result.getValorCompra());
        assertEquals(List.of(carrinhoProdutoMock), result.getItens());
    }

    @Test
    public void deveDevolverRetornarProdutoCarrinhoCorretamente() {
        // ARRANGE
        carrinhoProdutoMock.setId(id);
        when(carrinhoProdutoRepository.findById(id)).thenReturn(Optional.of(carrinhoProdutoMock));

        // ACT
        CarrinhoProduto result = service.detalharProdutoCarrinho(1L, 1L);

        // ASSERT
        assertEquals(1L, result.getId().getProdutoId());
        assertEquals(1L, result.getId().getCarrinhoId());
        assertEquals(2, result.getQuantidade());
    }

    @Test
    public void deveRemoverProdutoDoCarrinho() {
        // ARRANGE
        CarrinhoProdutoId id = new CarrinhoProdutoId(1L, 1L);
        when(carrinhoRepository.findById(1L)).thenReturn(Optional.of(carrinho));
        when(carrinhoProdutoRepository.findById(id)).thenReturn(Optional.of(carrinhoProdutoMock));

        // ACT
        service.removerProdutoCarrinho(1L, 1L);

        // ASSERT
        verify(carrinho).removerItens(carrinhoProdutoMock);
        verify(carrinhoProdutoRepository).delete(carrinhoProdutoMock);
    }

    @Test
    public void deveLancarExcecaoAoRemoverProdutoCarrinhoSeCarrinhoNaoExistir() {
        when(carrinhoRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RegraDeNegocioException.class, () -> {
            service.removerProdutoCarrinho(1L, 1L);
        });

        assertEquals("Carrinho nao encontrado", exception.getMessage());
    }

    @Test
    public void deveLancarExcecaoAoRemoverProdutoCarrinhoSeProdutoNaoExistirNoCarrinho() {
        CarrinhoProdutoId id = new CarrinhoProdutoId(1L, 1L);
        when(carrinhoRepository.findById(1L)).thenReturn(Optional.of(carrinho));
        when(carrinhoProdutoRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RegraDeNegocioException.class, () -> {
            service.removerProdutoCarrinho(1L, 1L);
        });

        assertEquals("Prduto nao encontrado no carrinho", exception.getMessage());
    }

    @Test
    public void deveLimparCarrinho() {
        // ARRANGE
        when(carrinhoRepository.findById(1L)).thenReturn(Optional.of(carrinho));

        // ACT
        service.limparCarrinho(1L);

        // ASSERT
        verify(carrinho).limparCarrinho(1L);
    }

    @Test
    public void deveLancarExcecaoAoLimparCarrinhoSeNaoExistir() {
        when(carrinhoRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RegraDeNegocioException.class, () -> {
            service.limparCarrinho(1L);
        });

        assertEquals("Carrinho nao encontrado", exception.getMessage());
    }

    @Test
    public void deveAdicionarQuantidadeProduto() {
        // ARRANGE
        CarrinhoProdutoId id = new CarrinhoProdutoId(1L, 1L);
        when(carrinhoRepository.findById(1L)).thenReturn(Optional.of(carrinho));
        when(carrinhoProdutoRepository.findById(id)).thenReturn(Optional.of(carrinhoProdutoMock));

        // Simula quantidade inicial
        carrinhoProdutoMock.setQuantidade(2);
        // Simula preço do produto
        when(produto.getAnuncioPreco()).thenReturn(100.0);
        carrinhoProdutoMock.setProduto(produto);

        // ACT
        CarrinhoProduto result = service.adcionarQuantidadeProduto(1L, 1L);

        // ASSERT
        assertEquals(3, result.getQuantidade());
        verify(carrinho).alterarValor(3, 100.0);
    }

    @Test
    public void deveLancarExcecaoAoAdicionarQuantidadeProdutoQuandoProdutoNaoExistirNoCarrinho() {
        CarrinhoProdutoId id = new CarrinhoProdutoId(1L, 1L);
        when(carrinhoRepository.findById(1L)).thenReturn(Optional.of(carrinho));
        when(carrinhoProdutoRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RegraDeNegocioException.class, () -> {
            service.adcionarQuantidadeProduto(1L, 1L);
        });

        assertEquals("Prduto nao encontrado no carrinho", exception.getMessage());
    }

    @Test
    public void deveLancarExcecaoAoAdicionarQuantidadeProdutoQuandoCarrinhoNaoExistir() {
        when(carrinhoRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RegraDeNegocioException.class, () -> {
            service.adcionarQuantidadeProduto(1L, 1L);
        });

        assertEquals("Carrinho nao encontrado", exception.getMessage());
    }

    @Test
    public void deveDiminuirQuantidadeProduto() {
        // ARRANGE
        CarrinhoProdutoId id = new CarrinhoProdutoId(1L, 1L);
        when(carrinhoRepository.findById(1L)).thenReturn(Optional.of(carrinho));
        when(carrinhoProdutoRepository.findById(id)).thenReturn(Optional.of(carrinhoProdutoMock));

        // Simula quantidade inicial
        carrinhoProdutoMock.setQuantidade(3);
        // Simula preço do produto
        when(produto.getAnuncioPreco()).thenReturn(100.0);
        carrinhoProdutoMock.setProduto(produto);

        // ACT
        CarrinhoProduto result = service.diminuirQuantidadeProduto(1L, 1L);

        // ASSERT
        assertEquals(2, result.getQuantidade());
        verify(carrinho).alterarValor(2, 100.0);
    }

    @Test
    public void deveLancarExcecaoAoDiminuirQuantidadeProdutoQuandoProdutoNaoExistirNoCarrinho() {
        CarrinhoProdutoId id = new CarrinhoProdutoId(1L, 1L);
        when(carrinhoRepository.findById(1L)).thenReturn(Optional.of(carrinho));
        when(carrinhoProdutoRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RegraDeNegocioException.class, () -> {
            service.diminuirQuantidadeProduto(1L, 1L);
        });

        assertEquals("Prduto nao encontrado no carrinho", exception.getMessage());
    }

    @Test
    public void deveLancarExcecaoAoDiminuirQuantidadeProdutoQuandoCarrinhoNaoExistir() {
        when(carrinhoRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RegraDeNegocioException.class, () -> {
            service.diminuirQuantidadeProduto(1L, 1L);
        });

        assertEquals("Carrinho nao encontrado", exception.getMessage());
    }
}