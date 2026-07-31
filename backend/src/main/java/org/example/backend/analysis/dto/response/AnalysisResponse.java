package org.example.backend.analysis.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisResponse {

    private Long id;

    private String analysisSummary;

    private Double viabilityScore;

    private String recommendation;

    private List<AnalysisDimension> dimensions;

    private List<AnalysisRisk> risks;

    private List<AnalysisRecommendation> recommendations;

    private String generationSource;

}
