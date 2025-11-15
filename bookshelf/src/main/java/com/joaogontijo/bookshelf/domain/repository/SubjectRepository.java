package com.joaogontijo.bookshelf.domain.repository;

import com.joaogontijo.bookshelf.domain.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

}
