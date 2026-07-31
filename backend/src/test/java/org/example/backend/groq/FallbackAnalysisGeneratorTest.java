package org.example.backend.groq;

import org.example.backend.analysis.dto.request.AnalysisRequest;
import org.example.backend.groq.service.FallbackAnalysisGenerator;
import org.example.backend.groq.service.GroqAnalysisClient;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FallbackAnalysisGeneratorTest {

    private final FallbackAnalysisGenerator generator = new FallbackAnalysisGenerator();

    @Test
    void alwaysReturnsTheExpectedFiveDimensionsRisksAndRecommendations() {
        GroqAnalysisClient.AnalysisJsonResult result = generator.generate(request());

        assertThat(result.dimensions()).hasSize(5);
        assertThat(result.dimensions()).allSatisfy(dimension -> assertThat(dimension.getScore()).isBetween(0, 100));
        assertThat(result.risks()).hasSize(3);
        assertThat(result.recommendations()).hasSize(3);
    }

    private AnalysisRequest request() {
        AnalysisRequest request = new AnalysisRequest();
        request.setSector("Hotels");
        request.setRegion("Djerba");
        request.setDescription("A three-star hotel project for visitors to Djerba.");
        request.setStage("Idea");
        request.setInitialBudget(500000D);
        request.setFinancingSource("Bank loan");
        request.setExpectedRevenue(800000D);
        request.setExpectedRoiMonths(48);
        request.setExperienceLevel("Intermediate");
        request.setCompetitionLevel("Medium competition");
        request.setTaxAdvantages(false);
        request.setFreeZoneStatus(false);
        return request;
    }
}