package com.joaogontijo.bookshelf.domain.service;

import com.joaogontijo.bookshelf.domain.exception.BusinessException;
import com.joaogontijo.bookshelf.domain.exception.EntityNotFoundException;
import com.joaogontijo.bookshelf.domain.model.Subject;
import com.joaogontijo.bookshelf.domain.repository.BookRepository;
import com.joaogontijo.bookshelf.domain.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    public List<Subject> findAll() {
        return subjectRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Subject findByIdOrThrow(Long id) {
        return subjectRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Assunto não encontrado com id: " + id));
    }

    @Transactional
    public Subject create(Subject subject) {
        subject.setId(null);
        return subjectRepository.save(subject);
    }

    @Transactional
    public Subject update(Long id, Subject input) {
        Subject existing = findByIdOrThrow(id);
        existing.setDescription(input.getDescription());
        return subjectRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        Subject subject = findByIdOrThrow(id);

        boolean hasBooks = bookRepository.existsBySubject_Id(subject.getId());
        if (hasBooks) {
            throw new BusinessException("Não é possível excluir o assunto, pois há livros associados a ele.");
        }

        subjectRepository.delete(subject);
    }

}
