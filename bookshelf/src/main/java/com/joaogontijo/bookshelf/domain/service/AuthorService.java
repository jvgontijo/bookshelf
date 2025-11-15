package com.joaogontijo.bookshelf.domain.service;

import com.joaogontijo.bookshelf.domain.exception.BusinessException;
import com.joaogontijo.bookshelf.domain.exception.EntityNotFoundException;
import com.joaogontijo.bookshelf.domain.model.Author;
import com.joaogontijo.bookshelf.domain.repository.AuthorRepository;
import com.joaogontijo.bookshelf.domain.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Author findByIdOrThrow(Long id) {
        return authorRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Autor não encontrado com id: " + id));
    }

    @Transactional
    public Author create(Author author) {
        author.setId(null);
        return authorRepository.save(author);
    }

    @Transactional
    public Author update(Long id, Author input) {
        Author existing = findByIdOrThrow(id);
        existing.setName(input.getName());
        return authorRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        Author author = findByIdOrThrow(id);

        boolean hasBooks = bookRepository.existsByAuthors_Id(author.getId());
        if (hasBooks) {
            throw new BusinessException("Não é possível excluir o autor, pois há livros associados a ele.");
        }

        authorRepository.delete(author);
    }

}
