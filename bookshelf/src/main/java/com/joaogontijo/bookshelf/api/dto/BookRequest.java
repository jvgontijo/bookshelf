package com.joaogontijo.bookshelf.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Set;

public record BookRequest(
    @NotBlank(message = "Título é obrigatório.")
    String title,

    String publisher,

    Integer publicationYear,

    String edition,

    @NotNull(message = "Valor do livro é obrigatório.")
    @DecimalMin(value = "0.0", inclusive = true, message = "Valor do livro deve ser maior ou igual a zero.")
    BigDecimal price,

    @NotNull(message = "Assunto é obrigatório.")
    Long subjectId,

    @NotEmpty(message = "Pelo menos um autor deve ser informado.")
    Set<Long> authorIds
) {
}
