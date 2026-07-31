package org.example.backend.groq.service;

import org.example.backend.analysis.dto.request.AnalysisRequest;
import org.example.backend.analysis.dto.response.AnalysisDimension;
import org.example.backend.analysis.dto.response.AnalysisRecommendation;
import org.example.backend.analysis.dto.response.AnalysisRisk;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FallbackAnalysisGenerator {

    public GroqAnalysisClient.AnalysisJsonResult generate(AnalysisRequest request) {
        return new GroqAnalysisClient.AnalysisJsonResult(
                List.of(
                        dimension("FINANCIAL_VIABILITY", financialScore(request),
                                "Rule-based estimate from the declared budget, expected revenue, and ROI period."),
                        dimension("MARKET_OPPORTUNITY", marketScore(request),
                                "Rule-based estimate from the declared sector, region, and competition level."),
                        dimension("OPERATIONAL_FEASIBILITY", operationalScore(request),
                                "Rule-based estimate using the project stage, experience, and declared resources."),
                        dimension("REGULATORY_FRAMEWORK", regulatoryScore(request),
                                "Rule-based estimate using tax advantages and free-zone status declared in the form."),
                        dimension("PROJECT_OWNER_PROFILE", ownerScore(request),
                                "Rule-based estimate using the experience level declared in the form.")
                ),
                List.of(
                        new AnalysisRisk("Market assumptions have not been validated with customers.", "HIGH",
                                "Interview target customers and document demand before investing."),
                        new AnalysisRisk("The financing and cash-flow plan may change the ROI period.", "MEDIUM",
                                "Confirm financing commitments and build a monthly cash-flow forecast."),
                        new AnalysisRisk("Regulatory requirements may vary by sector and region.", "MEDIUM",
                                "Validate permits, taxes, and authorizations with the relevant Tunisian authorities.")
                ),
                List.of(
                        new AnalysisRecommendation("Validate the market", "Run customer interviews and competitor research.", 1),
                        new AnalysisRecommendation("Secure the financing plan", "Obtain written financing commitments and update the ROI model.", 2),
                        new AnalysisRecommendation("Confirm regulatory requirements", "Create a permit and tax checklist before launch.", 3)
                )
        );
    }

    private AnalysisDimension dimension(String name, int score, String explanation) {
        return new AnalysisDimension(name, score, explanation);
    }

    private int financialScore(AnalysisRequest request) {
        int score = request.getInitialBudget() >= 100_000 ? 65 : 45;
        if (request.getExpectedRevenue() >= request.getInitialBudget() * 2) {
            score += 20;
        } else if (request.getExpectedRevenue() >= request.getInitialBudget()) {
            score += 10;
        }
        if (request.getExpectedRoiMonths() <= 36) {
            score += 5;
        }
        return cap(score);
    }

    private int marketScore(AnalysisRequest request) {
        String competition = request.getCompetitionLevel().toLowerCase();
        if (competition.contains("peu") || competition.contains("low")) {
            return 75;
        }
        if (competition.contains("élev") || competition.contains("high")) {
            return 45;
        }
        return 60;
    }

    private int operationalScore(AnalysisRequest request) {
        String stage = request.getStage().toLowerCase();
        int score = stage.contains("struct") || stage.contains("financ") ? 70 : 55;
        if (request.getExperienceLevel().toLowerCase().contains("expert")) {
            score += 15;
        }
        return cap(score);
    }

    private int regulatoryScore(AnalysisRequest request) {
        int score = Boolean.TRUE.equals(request.getTaxAdvantages()) ? 65 : 55;
        if (Boolean.TRUE.equals(request.getFreeZoneStatus())) {
            score += 10;
        }
        return cap(score);
    }

    private int ownerScore(AnalysisRequest request) {
        String experience = request.getExperienceLevel().toLowerCase();
        if (experience.contains("expert")) {
            return 85;
        }
        if (experience.contains("inter")) {
            return 70;
        }
        return 50;
    }

    private int cap(int score) {
        return Math.max(0, Math.min(score, 100));
    }
}