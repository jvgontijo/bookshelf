package com.joaogontijo.bookshelf.api.controller;

import com.joaogontijo.bookshelf.api.dto.SubjectRequest;
import com.joaogontijo.bookshelf.api.dto.SubjectResponse;
import com.joaogontijo.bookshelf.domain.model.Subject;
import com.joaogontijo.bookshelf.domain.service.SubjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    @GetMapping
    public List<SubjectResponse> list() {
        return subjectService.findAll()
            .stream()
            .map(this::toResponse)
            .toList();
    }

    @GetMapping("/{id}")
    public SubjectResponse getById(@PathVariable Long id) {
        Subject subject = subjectService.findByIdOrThrow(id);
        return toResponse(subject);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SubjectResponse create(@Valid @RequestBody SubjectRequest request) {
        Subject subject = new Subject();
        subject.setDescription(request.description());

        Subject saved = subjectService.create(subject);
        return toResponse(saved);
    }

    @PutMapping("/{id}")
    public SubjectResponse update(@PathVariable Long id, @Valid @RequestBody SubjectRequest request) {
        Subject subject = new Subject();
        subject.setDescription(request.description());

        Subject updated = subjectService.update(id, subject);
        return toResponse(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        subjectService.delete(id);
    }

    private SubjectResponse toResponse(Subject subject) {
        return new SubjectResponse(
            subject.getId(),
            subject.getDescription()
        );
    }
}
