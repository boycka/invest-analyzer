package org.example.backend.pdf.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.RequiredArgsConstructor;
import org.example.backend.analysis.dto.response.AnalysisDetailResponse;
import org.example.backend.analysis.dto.response.AnalysisDimension;
import org.example.backend.analysis.dto.response.AnalysisRecommendation;
import org.example.backend.analysis.dto.response.AnalysisRisk;
import org.example.backend.analysis.service.AnalysisService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class PdfReportService {

    private final AnalysisService analysisService;

    public byte[] generate(Long id) {
        AnalysisDetailResponse detail = analysisService.getAnalysis(id);
        try {
            String template = StreamUtils.copyToString(
                    new ClassPathResource("templates/pdf/analysis-report.html").getInputStream(),
                    StandardCharsets.UTF_8
            );
            String html = template
                    .replace("{{PROJECT_TITLE}}", escape(detail.request().getSector() + " - " + detail.request().getRegion()))
                    .replace("{{CREATED_AT}}", DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").format(LocalDateTime.now()))
                    .replace("{{SCORE}}", String.format("%.0f", detail.result().getViabilityScore()))
                    .replace("{{SOURCE}}", escape(detail.result().getGenerationSource()))
                    .replace("{{SUMMARY}}", escape(detail.result().getAnalysisSummary()))
                    .replace("{{DIMENSIONS}}", dimensions(detail))
                    .replace("{{RISKS}}", risks(detail))
                    .replace("{{RECOMMENDATIONS}}", recommendations(detail));

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            new PdfRendererBuilder().useFastMode().withHtmlContent(html, null).toStream(output).run();
            return output.toByteArray();
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to generate the analysis PDF", exception);
        }
    }

    private String dimensions(AnalysisDetailResponse detail) {
        StringBuilder html = new StringBuilder();
        for (AnalysisDimension dimension : detail.result().getDimensions()) {
            html.append("<div class='dimension'><strong>")
                    .append(escape(dimension.getDimension())).append("</strong><span class='score'>")
                    .append(dimension.getScore()).append("/100</span><p>")
                    .append(escape(dimension.getExplanation())).append("</p></div>");
        }
        return html.toString();
    }

    private String risks(AnalysisDetailResponse detail) {
        StringBuilder html = new StringBuilder();
        for (AnalysisRisk risk : detail.result().getRisks()) {
            html.append("<div class='risk'><strong>").append(escape(risk.getCriticality()))
                    .append(" — ").append(escape(risk.getDescription()))
                    .append("</strong><p><b>Mitigation:</b> ")
                    .append(escape(risk.getMitigation())).append("</p></div>");
        }
        return html.toString();
    }

    private String recommendations(AnalysisDetailResponse detail) {
        StringBuilder html = new StringBuilder();
        for (AnalysisRecommendation recommendation : detail.result().getRecommendations()) {
            html.append("<div class='recommendation'><strong>").append(recommendation.getPriority())
                    .append(". ").append(escape(recommendation.getTitle())).append("</strong><p>")
                    .append(escape(recommendation.getDescription())).append("</p></div>");
        }
        return html.toString();
    }

    private String escape(String value) {
        if (value == null) return "";
        return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;").replace("'", "&#39;");
    }
}