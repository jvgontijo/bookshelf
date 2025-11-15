package com.joaogontijo.bookshelf.api.dto;

import jakarta.validation.constraints.NotBlank;

public record SubjectRequest(
    @NotBlank(message = "Descrição do assunto é obrigatória.")
    String description
) {
}
