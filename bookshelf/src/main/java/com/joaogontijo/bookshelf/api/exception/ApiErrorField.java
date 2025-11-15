package com.joaogontijo.bookshelf.api.exception;

public record ApiErrorField(
    String field,
    String message
) {
}
