-- =========================
-- Tabela AUTOR
-- =========================
CREATE TABLE autor (
    cod_au SERIAL PRIMARY KEY,
    nome   VARCHAR(40) NOT NULL
);

-- =========================
-- Tabela ASSUNTO
-- =========================
CREATE TABLE assunto (
    cod_as    SERIAL PRIMARY KEY,
    descricao VARCHAR(20) NOT NULL
);

-- =========================
-- Tabela LIVRO
-- =========================
CREATE TABLE livro (
    cod_l          SERIAL PRIMARY KEY,
    titulo         VARCHAR(40) NOT NULL,
    editora        VARCHAR(40),
    edicao         INTEGER,
    ano_publicacao CHAR(4),
    valor          NUMERIC(10,2) NOT NULL  -- valor em R$
);

-- =========================
-- Relação LIVRO x AUTOR (N:N)
-- =========================
CREATE TABLE livro_autor (
    livro_cod_l  INTEGER NOT NULL,
    autor_cod_au INTEGER NOT NULL,

    PRIMARY KEY (livro_cod_l, autor_cod_au),

    CONSTRAINT fk_livro_autor_livro
        FOREIGN KEY (livro_cod_l)
        REFERENCES livro (cod_l),

    CONSTRAINT fk_livro_autor_autor
        FOREIGN KEY (autor_cod_au)
        REFERENCES autor (cod_au)
);

CREATE INDEX idx_livro_autor_autor ON livro_autor (autor_cod_au);
CREATE INDEX idx_livro_autor_livro ON livro_autor (livro_cod_l);

-- =========================
-- Relação LIVRO x ASSUNTO (N:N)
-- =========================
CREATE TABLE livro_assunto (
    livro_cod_l    INTEGER NOT NULL,
    assunto_cod_as INTEGER NOT NULL,

    PRIMARY KEY (livro_cod_l, assunto_cod_as),

    CONSTRAINT fk_livro_assunto_livro
        FOREIGN KEY (livro_cod_l)
        REFERENCES livro (cod_l),

    CONSTRAINT fk_livro_assunto_assunto
        FOREIGN KEY (assunto_cod_as)
        REFERENCES assunto (cod_as)
);

CREATE INDEX idx_livro_assunto_assunto ON livro_assunto (assunto_cod_as);
CREATE INDEX idx_livro_assunto_livro   ON livro_assunto (livro_cod_l);


CREATE OR REPLACE VIEW vw_relatorio_livros AS
SELECT
    a.cod_au,
    a.nome          AS nome_autor,
    l.cod_l,
    l.titulo,
    l.editora,
    l.edicao,
    l.ano_publicacao,
    l.valor,
    s.cod_as,
    s.descricao     AS descricao_assunto
FROM livro l
JOIN livro_autor la
    ON la.livro_cod_l = l.cod_l
JOIN autor a
    ON a.cod_au = la.autor_cod_au
LEFT JOIN livro_assunto las
    ON las.livro_cod_l = l.cod_l
LEFT JOIN assunto s
    ON s.cod_as = las.assunto_cod_as;
