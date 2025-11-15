package com.joaogontijo.bookshelf.domain.service;

import com.joaogontijo.bookshelf.api.dto.BooksByAuthorViewResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BooksByAuthorViewService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public List<BooksByAuthorViewResponse> findAll() {
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
        List<Object[]> rows = entityManager
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
        BigDecimal bookPrice = (BigDecimal) row[i++];
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
