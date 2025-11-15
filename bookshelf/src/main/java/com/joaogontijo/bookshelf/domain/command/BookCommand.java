package com.joaogontijo.bookshelf.domain.command;

import java.math.BigDecimal;
import java.util.Set;

public record BookCommand(
    String title,
    String publisher,
    Integer publicationYear,
    String edition,
    BigDecimal price,
    Long subjectId,
    Set<Long> authorIds
) {

}
