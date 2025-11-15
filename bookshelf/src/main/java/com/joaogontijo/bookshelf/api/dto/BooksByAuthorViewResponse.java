package com.joaogontijo.bookshelf.api.dto;

import java.math.BigDecimal;

public record BooksByAuthorViewResponse(
    Long authorId,
    String authorName,
    Long bookId,
    String bookTitle,
    Integer publicationYear,
    BigDecimal bookPrice,
    Long subjectId,
    String subjectDescription
) {
}
