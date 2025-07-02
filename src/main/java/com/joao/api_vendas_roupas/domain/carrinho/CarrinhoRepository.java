package com.joao.api_vendas_roupas.domain.carrinho;

import com.joao.api_vendas_roupas.domain.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.expression.spel.ast.OpAnd;

import java.util.Optional;

public interface CarrinhoRepository extends JpaRepository<Carrinho, Long> {

    Optional<Carrinho> findByUsuario(Usuario usuario);

    @Query("SELECT c.id FROM Carrinho c WHERE c.usuario = :usuario")
    Optional<Long> retornarIdCarrinho(Usuario usuario);
}
