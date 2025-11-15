package com.joaogontijo.bookshelf.api.controller;

import com.joaogontijo.bookshelf.domain.service.ReportService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports/books-by-author")
public class BooksByAuthorReportController {

    private final ReportService reportService;

    public BooksByAuthorReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> downloadPdf() {
        byte[] pdfBytes = reportService.generateBooksByAuthorPdf();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(
            ContentDisposition
                .inline()
                .filename("books-by-author.pdf")
                .build()
        );

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}
