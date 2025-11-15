CREATE TABLE authors (
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL
);


CREATE TABLE subjects (
    id          BIGSERIAL PRIMARY KEY,
    description VARCHAR(200) NOT NULL
);


CREATE TABLE books (
    id                BIGSERIAL PRIMARY KEY,
    title             VARCHAR(255) NOT NULL,
    isbn              VARCHAR(20)  NOT NULL UNIQUE,
    publication_year  INTEGER      NOT NULL,
    price             NUMERIC(10,2) NOT NULL,
    subject_id        BIGINT       NOT NULL,
    CONSTRAINT fk_books_subject
        FOREIGN KEY (subject_id)
        REFERENCES subjects (id)
);


CREATE TABLE book_author (
    book_id   BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    PRIMARY KEY (book_id, author_id),
    CONSTRAINT fk_book_author_book
        FOREIGN KEY (book_id)
        REFERENCES books (id)
        ON DELETE CASCADE,
    CONSTRAINT fk_book_author_author
        FOREIGN KEY (author_id)
        REFERENCES authors (id)
        ON DELETE CASCADE
);


CREATE INDEX idx_books_subject_id ON books (subject_id);
CREATE INDEX idx_book_author_author_id ON book_author (author_id);
CREATE INDEX idx_book_author_book_id ON book_author (book_id);


CREATE OR REPLACE VIEW vw_books_by_author AS
SELECT
    a.id   AS author_id,
    a.name AS author_name,

    b.id               AS book_id,
    b.title            AS book_title,
    b.isbn             AS book_isbn,
    b.publication_year AS publication_year,
    b.price            AS book_price,

    s.id          AS subject_id,
    s.description AS subject_description
FROM books b
JOIN book_author ba ON ba.book_id = b.id
JOIN authors a      ON a.id = ba.author_id
LEFT JOIN subjects s ON s.id = b.subject_id;

