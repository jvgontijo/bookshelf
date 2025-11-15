package com.joaogontijo.bookshelf.api.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthorRequest(
    @NotBlank(message = "Nome do autor é obrigatório.")
    String name
) {
}
