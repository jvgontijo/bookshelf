package com.joaogontijo.bookshelf.domain.service;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Collections;
import java.util.Map;

@Service
public class ReportService {

    private final DataSource dataSource;

    public ReportService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public byte[] generateBooksByAuthorPdf() {
        try (
            Connection connection = dataSource.getConnection();
            InputStream reportStream = new ClassPathResource("reports/books_by_author.jasper").getInputStream()
        ) {
            Map<String, Object> parameters = Collections.emptyMap();

            JasperPrint jasperPrint = JasperFillManager.fillReport(
                reportStream,
                parameters,
                connection
            );

            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (Exception ex) {
            // Aqui você pode criar uma BusinessException específica para relatório, se quiser.
            throw new RuntimeException("Erro ao gerar relatório de livros por autor.", ex);
        }
    }
}
