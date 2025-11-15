package com.joaogontijo.bookshelf.api.controller;

import com.joaogontijo.bookshelf.api.dto.BooksByAuthorViewResponse;
import com.joaogontijo.bookshelf.domain.service.BooksByAuthorViewService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reports/books-by-author")
public class BooksByAuthorViewController {

    private final BooksByAuthorViewService booksByAuthorViewService;

    public BooksByAuthorViewController(BooksByAuthorViewService booksByAuthorViewService) {
        this.booksByAuthorViewService = booksByAuthorViewService;
    }

    @GetMapping("/view")
    public List<BooksByAuthorViewResponse> listFromView() {
        return booksByAuthorViewService.findAll();
    }
}
