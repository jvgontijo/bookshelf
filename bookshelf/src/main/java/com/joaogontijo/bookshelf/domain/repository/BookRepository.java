package com.joaogontijo.bookshelf.domain.repository;

import com.joaogontijo.bookshelf.domain.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

    boolean existsBySubject_Id(Long subjectId);

    boolean existsByAuthors_Id(Long authorId);

}
