INSERT INTO authors (id, name) VALUES
    (1, 'Robert C. Martin'),
    (2, 'Martin Fowler'),
    (3, 'J. K. Rowling'),
    (4, 'Autor Conjunto');

INSERT INTO subjects (id, description) VALUES
    (1, 'Programação'),
    (2, 'Design de Software'),
    (3, 'Literatura'),
    (4, 'Banco de Dados');

INSERT INTO books (id, title, isbn, publication_year, price, subject_id) VALUES
    (1, 'Clean Code', '9780132350884', 2008, 120.00, 1),
    (2, 'Refactoring', '9780201485677', 1999, 150.50, 2),
    (3, 'Clean Architecture', '9780134494166', 2017, 140.00, 2),
    (4, 'Domain-Driven Sample Book', '1111111111111', 2020,  99.90, 1),
    (5, 'Harry Potter and the Philosopher''s Stone', '9780747532699', 1997, 80.00, 3);

INSERT INTO book_author (book_id, author_id) VALUES
    (1, 1), -- Clean Code - Uncle Bob
    (2, 2), -- Refactoring - Fowler
    (3, 1), -- Clean Architecture - Uncle Bob
    (4, 1), -- Sample Book - Bob
    (4, 2), -- Sample Book - Fowler (dois autores)
    (5, 3); -- Harry Potter - Rowling
