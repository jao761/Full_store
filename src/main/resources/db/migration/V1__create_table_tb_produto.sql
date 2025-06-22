CREATE TABLE IF NOT EXISTS tb_produto (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    anuncio_nome VARCHAR(255) NOT NULL,
    anuncio_descricao TEXT,
    anuncio_preco DOUBLE NOT NULL,
    anuncio_foto VARCHAR(100),
    anuncio_marca VARCHAR(100),
    anuncio_quantidade BIGINT

);