ALTER TABLE tb_produto
    MODIFY COLUMN anuncio_foto VARCHAR(100) NOT NULL,
    MODIFY COLUMN anuncio_marca VARCHAR(100) NOT NULL,
    MODIFY COLUMN anuncio_quantidade BIGINT NOT NULL;
