package org.example.backend.analysis.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.backend.analysis.dto.request.AnalysisRequest;
import org.example.backend.analysis.dto.response.AnalysisDetailResponse;
import org.example.backend.analysis.dto.response.AnalysisResponse;
import org.example.backend.analysis.entity.Analysis;
import org.example.backend.analysis.repository.AnalysisRepository;
import org.example.backend.common.exception.ResourceNotFoundException;
import org.example.backend.groq.exception.GroqApiException;
import org.example.backend.groq.service.FallbackAnalysisGenerator;
import org.example.backend.groq.service.GroqAnalysisClient;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AnalysisServiceImpl implements AnalysisService {

    private final AnalysisRepository repository;
    private final GroqAnalysisClient groqAnalysisClient;
    private final FallbackAnalysisGenerator fallbackAnalysisGenerator;
    private final ObjectMapper objectMapper;

    @Override
    public AnalysisResponse analyzeProject(AnalysisRequest request) {
        GroqAnalysisClient.AnalysisJsonResult result;
        String generationSource;

        try {
            result = groqAnalysisClient.parseResult(groqAnalysisClient.requestAnalysis(request));
            generationSource = "GROQ";
        } catch (GroqApiException exception) {
            result = fallbackAnalysisGenerator.generate(request);
            generationSource = "FALLBACK";
        }

        double score = calculateGlobalScore(result);
        String recommendation = result.recommendations().get(0).getTitle();
        Analysis analysis = Analysis.builder()
                .sector(request.getSector())
                .region(request.getRegion())
                .description(request.getDescription())
                .stage(request.getStage())
                .initialBudget(request.getInitialBudget())
                .financingSource(request.getFinancingSource())
                .expectedRevenue(request.getExpectedRevenue())
                .expectedRoiMonths(request.getExpectedRoiMonths())
                .experienceLevel(request.getExperienceLevel())
                .competitionLevel(request.getCompetitionLevel())
                .taxAdvantages(request.getTaxAdvantages())
                .freeZoneStatus(request.getFreeZoneStatus())
                .analysisSummary(generateSummary(result, score, generationSource))
                .detailedResultJson(writeResult(result))
                .viabilityScore(score)
                .recommendation(recommendation)
                .generationSource(generationSource)
                .createdAt(LocalDateTime.now())
                .build();

        repository.save(analysis);
        return toResponse(analysis, result);
    }

    @Override
    public AnalysisDetailResponse getAnalysis(Long id) {
        Analysis analysis = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Analysis not found: " + id));
        return new AnalysisDetailResponse(toResponse(analysis, readResult(analysis)), toRequest(analysis));
    }

    private double calculateGlobalScore(GroqAnalysisClient.AnalysisJsonResult result) {
        return result.dimensions().stream()
                .mapToInt(dimension -> dimension.getScore())
                .average()
                .orElse(0);
    }

    private AnalysisResponse toResponse(Analysis analysis, GroqAnalysisClient.AnalysisJsonResult result) {
        return AnalysisResponse.builder()
                .id(analysis.getId())
                .analysisSummary(analysis.getAnalysisSummary())
                .viabilityScore(analysis.getViabilityScore())
                .recommendation(analysis.getRecommendation())
                .dimensions(result.dimensions())
                .risks(result.risks())
                .recommendations(result.recommendations())
                .generationSource(analysis.getGenerationSource())
                .build();
    }

    private GroqAnalysisClient.AnalysisJsonResult readResult(Analysis analysis) {
        if (analysis.getDetailedResultJson() == null || analysis.getDetailedResultJson().isBlank()) {
            return fallbackAnalysisGenerator.generate(toRequest(analysis));
        }
        try {
            return objectMapper.readValue(analysis.getDetailedResultJson(), GroqAnalysisClient.AnalysisJsonResult.class);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Stored analysis result is invalid", exception);
        }
    }

    private String writeResult(GroqAnalysisClient.AnalysisJsonResult result) {
        try {
            return objectMapper.writeValueAsString(result);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Unable to store analysis result", exception);
        }
    }

    private AnalysisRequest toRequest(Analysis analysis) {
        AnalysisRequest request = new AnalysisRequest();
        request.setSector(analysis.getSector());
        request.setRegion(analysis.getRegion());
        request.setDescription(analysis.getDescription());
        request.setStage(analysis.getStage());
        request.setInitialBudget(analysis.getInitialBudget());
        request.setFinancingSource(analysis.getFinancingSource());
        request.setExpectedRevenue(analysis.getExpectedRevenue());
        request.setExpectedRoiMonths(analysis.getExpectedRoiMonths());
        request.setExperienceLevel(analysis.getExperienceLevel());
        request.setCompetitionLevel(analysis.getCompetitionLevel());
        request.setTaxAdvantages(analysis.getTaxAdvantages());
        request.setFreeZoneStatus(analysis.getFreeZoneStatus());
        return request;
    }

    private String generateSummary(GroqAnalysisClient.AnalysisJsonResult result, double score, String source) {
        String prefix = "FALLBACK".equals(source)
                ? "The AI service was temporarily unavailable; a rule-based fallback was used. "
                : "AI analysis completed. ";
        return prefix + "Five dimensions, " + result.risks().size() + " risks, and "
                + result.recommendations().size() + " recommendations. Global viability score: "
                + String.format("%.0f/100", score);
    }
}