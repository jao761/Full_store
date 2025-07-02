package com.joao.api_vendas_roupas.domain.carrinho;

import com.joao.api_vendas_roupas.domain.carrinho.carrinhoProduto.CarrinhoProduto;
import com.joao.api_vendas_roupas.domain.carrinho.carrinhoProduto.CarrinhoProdutoId;
import com.joao.api_vendas_roupas.domain.carrinho.carrinhoProduto.CarrinhoProdutoRepository;
import com.joao.api_vendas_roupas.domain.carrinho.carrinhoProduto.DadosAdicionarProduto;
import com.joao.api_vendas_roupas.domain.produto.Produto;
import com.joao.api_vendas_roupas.domain.produto.ProdutoRepository;
import com.joao.api_vendas_roupas.domain.usuario.Usuario;
import com.joao.api_vendas_roupas.infra.exception.RegraDeNegocioException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarrinhoServiceTest {

    @InjectMocks
    private CarrinhoService service;

    private final DadosAdicionarProduto dados = new DadosAdicionarProduto(1L, 2);

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

    private Usuario mockUsuario() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        return usuario;
    }

    CarrinhoProduto carrinhoProdutoMock = new CarrinhoProduto(dados.quantidadeProduto(), carrinho, produto);
    CarrinhoProdutoId id = new CarrinhoProdutoId(1L, 1L);

    @Test
    public void produtoDeveSerSalvoClasseCarrinhoProduto() {
        Usuario logado = mockUsuario();
        when(produtoRepository.findById(dados.produto_id())).thenReturn(Optional.of(produto));
        when(carrinhoRepository.findByUsuario(logado)).thenReturn(Optional.of(carrinho));
        when(produto.getAnuncioQuantidade()).thenReturn(5L);
        when(carrinhoProdutoRepository.save(any())).thenReturn(carrinhoProdutoMock);

        CarrinhoProduto result = service.adicionarProduto(dados, logado);

        assertEquals(2, result.getQuantidade());
    }

    @Test
    public void deveDevolverListagemDeCarrinhoCorretamente() {
        Usuario logado = mockUsuario();
        when(carrinhoRepository.findByUsuario(logado)).thenReturn(Optional.of(carrinho));
        when(carrinho.getId()).thenReturn(1L);
        when(carrinho.getItens()).thenReturn(List.of(carrinhoProdutoMock));
        when(carrinho.getValorCompra()).thenReturn(55.55);

        Carrinho result = service.listarCarrinho(logado);

        assertEquals(1L, result.getId());
        assertEquals(55.55, result.getValorCompra());
        assertEquals(List.of(carrinhoProdutoMock), result.getItens());
    }

    @Test
    public void deveDevolverRetornarProdutoCarrinhoCorretamente() {
        Usuario logado = mockUsuario();
        when(carrinhoRepository.retornarIdCarrinho(logado)).thenReturn(Optional.of(1L));
        carrinhoProdutoMock.setId(id);
        when(carrinhoProdutoRepository.findById(id)).thenReturn(Optional.of(carrinhoProdutoMock));

        CarrinhoProduto result = service.detalharProdutoCarrinho(logado, 1L);

        assertEquals(1L, result.getId().getProdutoId());
        assertEquals(1L, result.getId().getCarrinhoId());
    }

    @Test
    public void deveRemoverProdutoDoCarrinho() {
        Usuario logado = mockUsuario();
        when(carrinhoRepository.findByUsuario(logado)).thenReturn(Optional.of(carrinho));
        when(carrinhoProdutoRepository.findById(id)).thenReturn(Optional.of(carrinhoProdutoMock));

        service.removerProdutoCarrinho(logado, 1L);

        verify(carrinho).removerItens(carrinhoProdutoMock);
        verify(carrinhoProdutoRepository).delete(carrinhoProdutoMock);
    }

    @Test
    public void deveLancarExcecaoAoRemoverProdutoCarrinhoSeCarrinhoNaoExistir() {
        Usuario logado = mockUsuario();
        when(carrinhoRepository.findByUsuario(logado)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RegraDeNegocioException.class, () -> {
            service.removerProdutoCarrinho(logado, 1L);
        });

        assertEquals("Carrinho nao encontrado", exception.getMessage());
    }

    @Test
    public void deveLancarExcecaoAoRemoverProdutoCarrinhoSeProdutoNaoExistirNoCarrinho() {
        Usuario logado = mockUsuario();
        when(carrinhoRepository.findByUsuario(logado)).thenReturn(Optional.of(carrinho));
        when(carrinhoProdutoRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RegraDeNegocioException.class, () -> {
            service.removerProdutoCarrinho(logado, 1L);
        });

        assertEquals("Prduto nao encontrado no carrinho", exception.getMessage());
    }

    @Test
    public void deveLimparCarrinho() {
        Usuario logado = mockUsuario();
        when(carrinhoRepository.findByUsuario(logado)).thenReturn(Optional.of(carrinho));

        service.limparCarrinho(logado);

        verify(carrinho).limparCarrinho();
    }

    @Test
    public void deveLancarExcecaoAoLimparCarrinhoSeNaoExistir() {
        Usuario logado = mockUsuario();
        when(carrinhoRepository.findByUsuario(logado)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RegraDeNegocioException.class, () -> {
            service.limparCarrinho(logado);
        });

        assertEquals("Carrinho nao encontrado", exception.getMessage());
    }

    @Test
    public void deveAdicionarQuantidadeProduto() {
        Usuario logado = mockUsuario();
        when(carrinhoRepository.findByUsuario(logado)).thenReturn(Optional.of(carrinho));
        when(carrinhoProdutoRepository.findById(any(CarrinhoProdutoId.class))).thenReturn(Optional.of(carrinhoProdutoMock));

        carrinhoProdutoMock.setQuantidade(2);
        when(produto.getAnuncioPreco()).thenReturn(100.0);
        carrinhoProdutoMock.setProduto(produto);

        CarrinhoProduto result = service.adcionarQuantidadeProduto(logado, 1L);

        assertEquals(3, result.getQuantidade());
        verify(carrinho).alterarValor(3, 100.0);
    }

    @Test
    public void deveLancarExcecaoAoAdicionarQuantidadeProdutoQuandoProdutoNaoExistirNoCarrinho() {
        Usuario logado = mockUsuario();
        when(carrinhoRepository.findByUsuario(logado)).thenReturn(Optional.of(carrinho));
        when(carrinhoProdutoRepository.findById(any(CarrinhoProdutoId.class)))
                .thenReturn(Optional.empty()); // <- corrigido aqui!

        Exception exception = assertThrows(RegraDeNegocioException.class, () -> {
            service.adcionarQuantidadeProduto(logado, 1L);
        });

        assertEquals("Prduto nao encontrado no carrinho", exception.getMessage());
    }

    @Test
    public void deveLancarExcecaoAoAdicionarQuantidadeProdutoQuandoCarrinhoNaoExistir() {
        Usuario logado = mockUsuario();
        when(carrinhoRepository.findByUsuario(logado)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RegraDeNegocioException.class, () -> {
            service.adcionarQuantidadeProduto(logado, 1L);
        });

        assertEquals("Carrinho nao encontrado", exception.getMessage());
    }

    @Test
    public void deveDiminuirQuantidadeProduto() {
        CarrinhoProdutoId id = new CarrinhoProdutoId(1L, 1L);
        Usuario logado = mockUsuario();
        when(carrinhoRepository.findByUsuario(logado)).thenReturn(Optional.of(carrinho));
        when(carrinhoProdutoRepository.findById(any(CarrinhoProdutoId.class))).thenReturn(Optional.of(carrinhoProdutoMock));

        carrinhoProdutoMock.setQuantidade(3);
        when(produto.getAnuncioPreco()).thenReturn(100.0);
        carrinhoProdutoMock.setProduto(produto);

        CarrinhoProduto result = service.diminuirQuantidadeProduto(logado, 1L);

        assertEquals(2, result.getQuantidade());
        verify(carrinho).alterarValor(2, 100.0);
    }

    @Test
    public void deveLancarExcecaoAoDiminuirQuantidadeProdutoQuandoProdutoNaoExistirNoCarrinho() {
        Usuario logado = mockUsuario();
        when(carrinhoRepository.findByUsuario(logado)).thenReturn(Optional.of(carrinho));
        when(carrinhoProdutoRepository.findById(any(CarrinhoProdutoId.class))).thenReturn(Optional.empty());

        Exception exception = assertThrows(RegraDeNegocioException.class, () -> {
            service.diminuirQuantidadeProduto(logado, 1L);
        });

        assertEquals("Prduto nao encontrado no carrinho", exception.getMessage());
    }

    @Test
    public void deveLancarExcecaoAoDiminuirQuantidadeProdutoQuandoCarrinhoNaoExistir() {
        Usuario logado = mockUsuario();
        when(carrinhoRepository.findByUsuario(logado)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RegraDeNegocioException.class, () -> {
            service.diminuirQuantidadeProduto(logado, 1L);
        });

        assertEquals("Carrinho nao encontrado", exception.getMessage());
    }
}