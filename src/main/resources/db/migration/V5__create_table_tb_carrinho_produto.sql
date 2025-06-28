CREATE TABLE IF NOT EXISTS tb_carrinho_produto (
    carrinho_id BIGINT NOT NULL,
    produto_id BIGINT NOT NULL,
    quantidade INT NOT NULL,

    PRIMARY KEY (carrinho_id, produto_id),

    CONSTRAINT fk_carrinho FOREIGN KEY (carrinho_id) REFERENCES tb_carrinho(id) ON DELETE CASCADE,

    CONSTRAINT fk_produto FOREIGN KEY (produto_id) REFERENCES tb_produto(id) ON DELETE CASCADE
);
