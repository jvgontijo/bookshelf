package com.joaogontijo.bookshelf.domain.service;

import com.joaogontijo.bookshelf.api.dto.BooksByAuthorViewResponse;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    private final BooksByAuthorViewService booksByAuthorViewService;

    public ReportService(BooksByAuthorViewService booksByAuthorViewService) {
        this.booksByAuthorViewService = booksByAuthorViewService;
    }

    public byte[] generateBooksByAuthorPdf() {
        try {
            List<BooksByAuthorViewResponse> data = booksByAuthorViewService.findAll();

            ClassPathResource resource =
                new ClassPathResource("reports/books_by_author.jrxml");

            if (!resource.exists()) {
                throw new IllegalStateException(
                    "Arquivo JRXML não encontrado em classpath: reports/books_by_author.jrxml"
                );
            }

            JasperReport jasperReport;
            try (InputStream is = resource.getInputStream()) {
                jasperReport = JasperCompileManager.compileReport(is);
            }

            JRBeanCollectionDataSource dataSource =
                new JRBeanCollectionDataSource(data);

            Map<String, Object> params = new HashMap<>();
            params.put("REPORT_TITLE", "Livros por autor");

            JasperPrint jasperPrint =
                JasperFillManager.fillReport(jasperReport, params, dataSource);

            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar relatório de livros por autor.", e);
        }
    }
}
