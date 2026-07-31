package org.example.backend.analysis.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.analysis.dto.request.AnalysisRequest;
import org.example.backend.analysis.dto.response.AnalysisResponse;
import org.example.backend.analysis.entity.Analysis;
import org.example.backend.analysis.repository.AnalysisRepository;
import org.example.backend.groq.service.GroqAnalysisClient;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AnalysisServiceImpl implements AnalysisService {

    private final AnalysisRepository repository;
    private final GroqAnalysisClient groqAnalysisClient;


    @Override
    public AnalysisResponse analyzeProject(AnalysisRequest request) {
        GroqAnalysisClient.AnalysisJsonResult aiResult = groqAnalysisClient.parseResult(
                groqAnalysisClient.requestAnalysis(request)
        );

        double score = aiResult.dimensions().stream()
                .mapToInt(dimension -> dimension.getScore())
                .average()
                .orElseThrow();
        String recommendation = aiResult.recommendations().get(0).getTitle();

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
                .analysisSummary(generateSummary(aiResult, score))
                .viabilityScore(score)
                .recommendation(recommendation)
                .createdAt(LocalDateTime.now())
                .build();


        repository.save(analysis);


        return AnalysisResponse.builder()
                .id(analysis.getId())
                .analysisSummary(analysis.getAnalysisSummary())
                .viabilityScore(score)
                .recommendation(recommendation)
                .dimensions(aiResult.dimensions())
                .risks(aiResult.risks())
                .recommendations(aiResult.recommendations())
                .generationSource("GROQ")
                .build();
    }

    private String generateSummary(GroqAnalysisClient.AnalysisJsonResult aiResult, double score) {
        return "AI analysis completed with five dimensions, "
                + aiResult.risks().size() + " risks, and "
                + aiResult.recommendations().size()
                + " recommendations. Global viability score: "
                + String.format("%.0f/100", score);
    }
}
