package com.joao.api_vendas_roupas.domain.produto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Query("SELECT p FROM Produto p WHERE p.ativo = true AND p.anuncioQuantidade > 0")
    Page<Produto> findAll(Pageable pageable);
}
