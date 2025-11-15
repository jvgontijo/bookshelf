package com.joaogontijo.bookshelf.api.controller;

import com.joaogontijo.bookshelf.api.dto.AuthorSummary;
import com.joaogontijo.bookshelf.api.dto.BookRequest;
import com.joaogontijo.bookshelf.api.dto.BookResponse;
import com.joaogontijo.bookshelf.api.dto.SubjectSummary;
import com.joaogontijo.bookshelf.domain.command.BookCommand;
import com.joaogontijo.bookshelf.domain.model.Author;
import com.joaogontijo.bookshelf.domain.model.Book;
import com.joaogontijo.bookshelf.domain.model.Subject;
import com.joaogontijo.bookshelf.domain.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public List<BookResponse> list() {
        return bookService.findAll()
            .stream()
            .map(this::toResponse)
            .toList();
    }

    @GetMapping("/{id}")
    public BookResponse getById(@PathVariable Long id) {
        Book book = bookService.findByIdOrThrow(id);
        return toResponse(book);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponse create(@Valid @RequestBody BookRequest request) {
        BookCommand command = toCommand(request);
        Book saved = bookService.create(command);
        return toResponse(saved);
    }

    @PutMapping("/{id}")
    public BookResponse update(@PathVariable Long id, @Valid @RequestBody BookRequest request) {
        BookCommand command = toCommand(request);
        Book updated = bookService.update(id, command);
        return toResponse(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        bookService.delete(id);
    }

    private BookCommand toCommand(BookRequest request) {
        return new BookCommand(
            request.title(),
            request.publisher(),
            request.publicationYear(),
            request.edition(),
            request.price(),
            request.subjectId(),
            request.authorIds()
        );
    }

    private BookResponse toResponse(Book book) {
        Subject subject = book.getSubject();
        SubjectSummary subjectSummary = new SubjectSummary(
            subject.getId(),
            subject.getDescription()
        );

        Set<AuthorSummary> authorSummaries = book.getAuthors()
            .stream()
            .map(this::toAuthorSummary)
            .collect(Collectors.toSet());

        return new BookResponse(
            book.getId(),
            book.getTitle(),
            book.getPublisher(),
            book.getPublicationYear(),
            book.getEdition(),
            book.getPrice(),
            subjectSummary,
            authorSummaries
        );
    }

    private AuthorSummary toAuthorSummary(Author author) {
        return new AuthorSummary(
            author.getId(),
            author.getName()
        );
    }
}
