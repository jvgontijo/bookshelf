# Bookshelf Monorepo

Monorepo contendo:

- **Backend**: API REST em Spring Boot para cadastro de autores, assuntos e livros, além de endpoint de relatório baseado em **view do banco de dados**.
- **Frontend**: SPA em Angular (standalone components) com Bootstrap para consumir a API e exibir telas de manutenção e o relatório “Livros por autor”.

---

## Estrutura do repositório

```text
.
├── README.md           # Este arquivo
├── bookshelf           # Backend - Spring Boot
└── bookshelf-ng        # Frontend - Angular
```

### Backend (`bookshelf`)

- Java 17+ (ou superior)
- Spring Boot 3.5.x
  - Spring Web
  - Spring Data JPA
  - Validation (Jakarta Bean Validation)
- PostgreSQL 18.x
- Maven

### Frontend (`bookshelf-ng`)

- Angular 20 (standalone components)
- Angular Router
- HttpClient
- Bootstrap 5 (via CSS global)

---

## Arquitetura do backend

### Pacotes principais

```text
com.joaogontijo.bookshelf
├── api
│   ├── controller      # REST Controllers
│   ├── dto             # DTOs de request/response
│   └── exception       # Tratamento de erros global (ControllerAdvice)
├── config              # Configurações (ex: CORS)
├── domain
│   ├── exception       # Exceções de domínio (BusinessException, etc.)
│   ├── model           # Entidades JPA
│   ├── repository      # Repositórios Spring Data JPA
│   └── service         # Serviços de domínio / regras de negócio
└── BookshelfApplication.java
```

### Modelo de domínio

Entidades principais:

- `Author`
  - `id` (PK)
  - `name`
- `Subject`
  - `id` (PK)
  - `description`
- `Book`
  - `id` (PK)
  - `title`
  - `publicationYear`
  - `price`
  - relacionamentos:
    - muitos-para-muitos com `Author`
    - muitos-para-muitos com `Subject`

Tabelas de junção:

- `book_author` (FK `book_id`, `author_id`)
- `book_subject` (FK `book_id`, `subject_id`)

---

## View para relatório no PostgreSQL

O relatório deve ser baseado em **view de banco**, trazendo dados das 3 tabelas principais (**autor**, **livro**, **assunto**) permitindo **agrupamento por autor**.

Criação sugerida da view `vw_books_by_author`:

```sql
CREATE VIEW vw_books_by_author AS
SELECT
  a.id  AS author_id,
  a.name  AS author_name,
  b.id  AS book_id,
  b.title  AS book_title,
  b.publication_year,
  b.price  AS book_price,
  s.id  AS subject_id,
  s.description AS subject_description
FROM book b
JOIN book_author ba ON ba.book_id = b.id
JOIN author a ON a.id = ba.author_id
JOIN book_subject bs ON bs.book_id = b.id
JOIN subject s ON s.id = bs.subject_id;
```

Essa view é a **fonte única de dados** do relatório “Livros por autor”.

---

## DTO e Service do relatório no backend

### DTO de resposta do relatório

```java
// bookshelf/src/main/java/com/joaogontijo/bookshelf/api/dto/BooksByAuthorViewResponse.java
public record BooksByAuthorViewResponse(
  Long authorId,
  String authorName,
  Long bookId,
  String bookTitle,
  Integer publicationYear,
  java.math.BigDecimal bookPrice,
  Long subjectId,
  String subjectDescription
) {}
```

### Service que consulta a view

```java
// bookshelf/src/main/java/com/joaogontijo/bookshelf/domain/service/BooksByAuthorViewService.java
@Service
public class BooksByAuthorViewService {

  @jakarta.persistence.PersistenceContext
  private jakarta.persistence.EntityManager entityManager;

  @jakarta.transaction.Transactional
  public java.util.List<BooksByAuthorViewResponse> findAll() {
    String sql = """
      SELECT
        author_id,
        author_name,
        book_id,
        book_title,
        publication_year,
        book_price,
        subject_id,
        subject_description
      FROM vw_books_by_author
      ORDER BY author_name, book_title
      """;

    @SuppressWarnings("unchecked")
    java.util.List<Object[]> rows = entityManager
      .createNativeQuery(sql)
      .getResultList();

    return rows.stream()
      .map(this::mapRow)
      .toList();
  }

  private BooksByAuthorViewResponse mapRow(Object[] row) {
    int i = 0;

    Long authorId = ((Number) row[i++]).longValue();
    String authorName = (String) row[i++];
    Long bookId = ((Number) row[i++]).longValue();
    String bookTitle = (String) row[i++];
    Integer publicationYear = row[i] != null ? ((Number) row[i]).intValue() : null;
    i++;
    java.math.BigDecimal bookPrice = (java.math.BigDecimal) row[i++];
    Long subjectId = ((Number) row[i++]).longValue();
    String subjectDescription = (String) row[i];

    return new BooksByAuthorViewResponse(
      authorId,
      authorName,
      bookId,
      bookTitle,
      publicationYear,
      bookPrice,
      subjectId,
      subjectDescription
    );
  }
}
```

### Controller do relatório (view JSON)

```java
// bookshelf/src/main/java/com/joaogontijo/bookshelf/api/controller/BooksByAuthorViewController.java
@RestController
@RequestMapping("/api/reports/books-by-author")
public class BooksByAuthorViewController {

  private final BooksByAuthorViewService booksByAuthorViewService;

  public BooksByAuthorViewController(BooksByAuthorViewService booksByAuthorViewService) {
    this.booksByAuthorViewService = booksByAuthorViewService;
  }

  @GetMapping("/view")
  public java.util.List<BooksByAuthorViewResponse> listFromView() {
    return booksByAuthorViewService.findAll();
  }
}
```

> O relatório final (visual e impressão/PDF) é montado **no frontend**, consumindo esse endpoint.

---

## Endpoints principais da API

Base URL (dev):  
`http://localhost:8080`

### Autores

- `GET /api/authors`
- `GET /api/authors/{id}`
- `POST /api/authors`
- `PUT /api/authors/{id}`
- `DELETE /api/authors/{id}`

### Assuntos

- `GET /api/subjects`
- `GET /api/subjects/{id}`
- `POST /api/subjects`
- `PUT /api/subjects/{id}`
- `DELETE /api/subjects/{id}`

### Livros

- `GET /api/books`
- `GET /api/books/{id}`
- `POST /api/books`
- `PUT /api/books/{id}`
- `DELETE /api/books/{id}`

`BookRequest` (exemplo) contém:

- `title`
- `publicationYear`
- `price`
- `authorIds` (lista de Long)
- `subjectIds` (lista de Long)

### Relatório

- `GET /api/reports/books-by-author/view`  
  Retorna a lista “flat” da view `vw_books_by_author`.

---

## Tratamento de erros (API)

Há um `@RestControllerAdvice` global (`GlobalExceptionHandler`) que trata, por exemplo:

- `EntityNotFoundException` → `404 Not Found`
- `BusinessException` → `400 Bad Request`
- `MethodArgumentNotValidException` → `400 Bad Request` com lista de campos inválidos
- `Exception` genérica → `500 Internal Server Error` com mensagem amigável

A resposta de erro segue um formato padrão (`ApiError`) com:

- timestamp
- status
- error
- message
- path
- fields (lista de `ApiErrorField` quando for erro de validação)

---

## CORS

Para permitir o frontend em `http://localhost:4200` acessar a API, há uma configuração global de CORS semelhante a:

```java
@Configuration
public class WebConfig implements org.springframework.web.servlet.config.annotation.WebMvcConfigurer {

  @Override
  public void addCorsMappings(org.springframework.web.servlet.config.annotation.WebMvcConfigurer.CorsRegistry registry) {
    registry.addMapping("/**")
      .allowedOrigins("http://localhost:4200")
      .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
      .allowedHeaders("*");
  }
}
```

Ajuste `allowedOrigins` se o front estiver em outra URL.

---

## Arquitetura do frontend (Angular) – `bookshelf-ng`

### Tecnologias

- Angular 20 (standalone components)
- Angular Router
- HttpClient
- Bootstrap 5

### Estrutura geral (simplificada)

```text
bookshelf-ng/
├── src/
│   ├── app/
│   │   ├── core/           # Services, models, etc.
│   │   ├── features/
│   │   │   ├── books/
│   │   │   ├── authors/
│   │   │   ├── subjects/
│   │   │   └── reports/
│   │   ├── shared/         # Componentes compartilhados (Navbar, etc.)
│   │   ├── app.routes.ts   # Rotas principais
│   │   └── app.component.ts
│   └── main.ts
```

### Rotas principais (`app.routes.ts`)

```ts
import { Routes } from '@angular/router';
import { HomeComponent } from './features/home/home.component';
import { BookListComponent } from './features/books/book-list/book-list.component';
import { BookFormComponent } from './features/books/book-form/book-form.component';
import { AuthorListComponent } from './features/authors/author-list/author-list.component';
import { AuthorFormComponent } from './features/authors/author-form/author-form.component';
import { SubjectListComponent } from './features/subjects/subject-list/subject-list.component';
import { SubjectFormComponent } from './features/subjects/subject-form/subject-form.component';
import { BooksByAuthorReportComponent } from './features/reports/books-by-author-report/books-by-author-report.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },

  { path: 'books', component: BookListComponent },
  { path: 'books/new', component: BookFormComponent },
  { path: 'books/:id/edit', component: BookFormComponent },

  { path: 'authors', component: AuthorListComponent },
  { path: 'authors/new', component: AuthorFormComponent },
  { path: 'authors/:id/edit', component: AuthorFormComponent },

  { path: 'subjects', component: SubjectListComponent },
  { path: 'subjects/new', component: SubjectFormComponent },
  { path: 'subjects/:id/edit', component: SubjectFormComponent },

  { path: 'reports/books-by-author', component: BooksByAuthorReportComponent }
];
```

### Modelo do relatório no front

```ts
// bookshelf-ng/src/app/core/models/books-by-author-view-response.model.ts
export interface BooksByAuthorViewResponse {
  authorId: number;
  authorName: string;
  bookId: number;
  bookTitle: string;
  publicationYear: number | null;
  bookPrice: number;
  subjectId: number;
  subjectDescription: string;
}
```

### Comportamento do componente de relatório

`BooksByAuthorReportComponent`:

1. Chama `GET /api/reports/books-by-author/view`.
2. Agrupa os dados por `authorId`/`authorName`.
3. Renderiza para cada autor:
   - Um card com nome do autor.
   - Uma tabela com livros + ano + preço + assunto.
4. Disponibiliza botão para **imprimir/salvar em PDF** via `window.print()` (relatório simples, mas atende o requisito do teste).

---

## Como rodar o projeto (dev)

### 1. Pré-requisitos

- JDK 17+
- Maven
- Node.js + npm
- PostgreSQL

### 2. Banco de dados

Crie o banco:

```sql
CREATE DATABASE bookshelf;
```

Crie as tabelas (exemplo):

```sql
CREATE TABLE author (
  id    BIGSERIAL PRIMARY KEY,
  name  VARCHAR(255) NOT NULL
);

CREATE TABLE subject (
  id      BIGSERIAL PRIMARY KEY,
  description VARCHAR(255) NOT NULL
);

CREATE TABLE book (
  id        BIGSERIAL PRIMARY KEY,
  title       VARCHAR(255) NOT NULL,
  publication_year INTEGER,
  price       NUMERIC(10,2) NOT NULL
);

CREATE TABLE book_author (
  book_id   BIGINT NOT NULL REFERENCES book(id),
  author_id BIGINT NOT NULL REFERENCES author(id),
  PRIMARY KEY (book_id, author_id)
);

CREATE TABLE book_subject (
  book_id   BIGINT NOT NULL REFERENCES book(id),
  subject_id  BIGINT NOT NULL REFERENCES subject(id),
  PRIMARY KEY (book_id, subject_id)
);
```

Crie a view do relatório:

```sql
CREATE VIEW vw_books_by_author AS
SELECT
  a.id  AS author_id,
  a.name  AS author_name,
  b.id  AS book_id,
  b.title  AS book_title,
  b.publication_year,
  b.price  AS book_price,
  s.id  AS subject_id,
  s.description AS subject_description
FROM book b
JOIN book_author ba ON ba.book_id = b.id
JOIN author a ON a.id = ba.author_id
JOIN book_subject bs ON bs.book_id = b.id
JOIN subject s ON s.id = bs.subject_id;
```

### 3. Configurar o backend

No arquivo `bookshelf/src/main/resources/application.properties` (ou `.yml`):

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/bookshelf
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA

spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

### 4. Rodar o backend

```bash
cd bookshelf
mvn spring-boot:run
```

API disponível em:  
`http://localhost:8080`

### 5. Rodar o frontend

```bash
cd bookshelf-ng
yarn install
yarn start
# ou
ng serve
```

Frontend em:  
`http://localhost:4200`