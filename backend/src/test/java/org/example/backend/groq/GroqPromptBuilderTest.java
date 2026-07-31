package org.example.backend.groq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.backend.analysis.dto.request.AnalysisRequest;
import org.example.backend.groq.service.GroqPromptBuilder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GroqPromptBuilderTest {

    private final GroqPromptBuilder promptBuilder = new GroqPromptBuilder(new ObjectMapper());

    @Test
    void includesAllRequiredDimensionsAndJsonRules() {
        String prompt = promptBuilder.build(request());

        assertThat(prompt)
                .contains("FINANCIAL_VIABILITY")
                .contains("MARKET_OPPORTUNITY")
                .contains("OPERATIONAL_FEASIBILITY")
                .contains("REGULATORY_FRAMEWORK")
                .contains("PROJECT_OWNER_PROFILE")
                .contains("return ONLY one valid JSON object")
                .contains("risks must contain exactly three")
                .contains("\"sector\":\"Hotels\"");
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
