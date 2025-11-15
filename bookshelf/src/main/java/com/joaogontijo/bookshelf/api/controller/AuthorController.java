package com.joaogontijo.bookshelf.api.controller;

import com.joaogontijo.bookshelf.api.dto.AuthorRequest;
import com.joaogontijo.bookshelf.api.dto.AuthorResponse;
import com.joaogontijo.bookshelf.domain.model.Author;
import com.joaogontijo.bookshelf.domain.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping
    public List<AuthorResponse> list() {
        return authorService.findAll()
            .stream()
            .map(this::toResponse)
            .toList();
    }

    @GetMapping("/{id}")
    public AuthorResponse getById(@PathVariable Long id) {
        Author author = authorService.findByIdOrThrow(id);
        return toResponse(author);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorResponse create(@Valid @RequestBody AuthorRequest request) {
        Author author = new Author();
        author.setName(request.name());

        Author saved = authorService.create(author);
        return toResponse(saved);
    }

    @PutMapping("/{id}")
    public AuthorResponse update(@PathVariable Long id, @Valid @RequestBody AuthorRequest request) {
        Author author = new Author();
        author.setName(request.name());

        Author updated = authorService.update(id, author);
        return toResponse(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        authorService.delete(id);
    }

    private AuthorResponse toResponse(Author author) {
        return new AuthorResponse(
            author.getId(),
            author.getName()
        );
    }
}
