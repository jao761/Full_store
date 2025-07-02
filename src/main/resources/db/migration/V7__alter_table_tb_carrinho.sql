ALTER TABLE tb_carrinho
    ADD COLUMN usuario_id BIGINT,
    ADD CONSTRAINT fk_carrinho_usuario FOREIGN KEY (usuario_id) REFERENCES tb_usuario(id);