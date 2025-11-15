package com.joaogontijo.bookshelf.api.exception;

import com.joaogontijo.bookshelf.domain.exception.BusinessException;
import com.joaogontijo.bookshelf.domain.exception.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.util.HtmlUtils;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleEntityNotFound(
        EntityNotFoundException ex,
        HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        ApiError body = new ApiError(
            Instant.now(),
            status.value(),
            status.getReasonPhrase(),
            sanitize(ex.getMessage()),
            sanitize(request.getRequestURI()),
            Collections.emptyList()
        );

        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> handleBusiness(
        BusinessException ex,
        HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ApiError body = new ApiError(
            Instant.now(),
            status.value(),
            status.getReasonPhrase(),
            sanitize(ex.getMessage()),
            sanitize(request.getRequestURI()),
            Collections.emptyList()
        );

        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        List<ApiErrorField> fields = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(this::toField)
            .toList();

        ApiError body = new ApiError(
            Instant.now(),
            status.value(),
            status.getReasonPhrase(),
            "Um ou mais campos estão inválidos. Corrija e tente novamente.",
            sanitize(request.getRequestURI()),
            fields
        );

        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpected(
        Exception ex,
        HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        ApiError body = new ApiError(
            Instant.now(),
            status.value(),
            status.getReasonPhrase(),
            "Ocorreu um erro inesperado. Se o problema persistir, contate o suporte.",
            sanitize(request.getRequestURI()),
            Collections.emptyList()
        );

        ex.printStackTrace();

        return ResponseEntity.status(status).body(body);
    }

    private ApiErrorField toField(FieldError fieldError) {
        return new ApiErrorField(
            sanitize(fieldError.getField()),
            sanitize(fieldError.getDefaultMessage())
        );
    }

    private String sanitize(String value) {
        if (value == null) {
            return null;
        }
        return HtmlUtils.htmlEscape(value);
    }
}
