package com.joaogontijo.bookshelf.domain.service;

import com.joaogontijo.bookshelf.domain.command.BookCommand;
import com.joaogontijo.bookshelf.domain.exception.BusinessException;
import com.joaogontijo.bookshelf.domain.exception.EntityNotFoundException;
import com.joaogontijo.bookshelf.domain.model.Author;
import com.joaogontijo.bookshelf.domain.model.Book;
import com.joaogontijo.bookshelf.domain.model.Subject;
import com.joaogontijo.bookshelf.domain.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final SubjectService subjectService;
    private final AuthorService authorService;

    @Transactional(readOnly = true)
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Book findByIdOrThrow(Long id) {
        return bookRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Livro não encontrado com id: " + id));
    }

    @Transactional
    public Book create(BookCommand command) {
        validateCommand(command);

        Subject subject = subjectService.findByIdOrThrow(command.subjectId());
        Set<Author> authors = loadAuthors(command.authorIds());

        Book book = new Book();
        book.setTitle(command.title());
        book.setPublisher(command.publisher());
        book.setPublicationYear(command.publicationYear());
        book.setEdition(command.edition());
        book.setPrice(command.price());
        book.setSubject(subject);
        book.setAuthors(authors);

        return bookRepository.save(book);
    }

    @Transactional
    public Book update(Long id, BookCommand command) {
        validateCommand(command);

        Book existing = findByIdOrThrow(id);

        Subject subject = subjectService.findByIdOrThrow(command.subjectId());
        Set<Author> authors = loadAuthors(command.authorIds());

        existing.setTitle(command.title());
        existing.setPublisher(command.publisher());
        existing.setPublicationYear(command.publicationYear());
        existing.setEdition(command.edition());
        existing.setPrice(command.price());
        existing.setSubject(subject);
        existing.setAuthors(authors);

        return bookRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        Book book = findByIdOrThrow(id);
        bookRepository.delete(book);
    }

    private void validateCommand(BookCommand command) {
        if (command.title() == null || command.title().isBlank()) {
            throw new BusinessException("Título do livro é obrigatório.");
        }

        if (command.price() == null || command.price().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("O valor do livro deve ser maior ou igual a zero.");
        }

        if (command.subjectId() == null) {
            throw new BusinessException("O assunto do livro é obrigatório.");
        }

        if (command.authorIds() == null || command.authorIds().isEmpty()) {
            throw new BusinessException("O livro deve possuir pelo menos um autor.");
        }
    }

    private Set<Author> loadAuthors(Set<Long> authorIds) {
        Set<Author> authors = new HashSet<>();

        for (Long authorId : authorIds) {
            Author author = authorService.findByIdOrThrow(authorId);
            authors.add(author);
        }

        return authors;
    }

}
