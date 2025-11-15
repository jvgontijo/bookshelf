package com.joaogontijo.bookshelf.api.dto;

import java.math.BigDecimal;
import java.util.Set;

public record BookResponse(
    Long id,
    String title,
    String publisher,
    Integer publicationYear,
    String edition,
    BigDecimal price,
    SubjectSummary subject,
    Set<AuthorSummary> authors
) {
}
