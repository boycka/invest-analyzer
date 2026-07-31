package org.example.backend.groq.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.backend.analysis.dto.request.AnalysisRequest;
import org.example.backend.groq.exception.GroqApiException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroqPromptBuilder {

    private final ObjectMapper objectMapper;

    public String build(AnalysisRequest request) {
        try {
            String requestJson = objectMapper.writeValueAsString(request);

            return """
                    You are a senior investment analyst specialized in Tunisia.
                    Analyze the project data below and return ONLY one valid JSON object.
                    Do not use Markdown, code fences, comments, or text before or after the JSON.

                    The JSON object must have exactly these top-level arrays:
                    {
                      "dimensions": [
                        {
                          "dimension": "FINANCIAL_VIABILITY|MARKET_OPPORTUNITY|OPERATIONAL_FEASIBILITY|REGULATORY_FRAMEWORK|PROJECT_OWNER_PROFILE",
                          "score": 0,
                          "explanation": "Concise evidence-based explanation"
                        }
                      ],
                      "risks": [
                        {
                          "description": "Main project risk",
                          "criticality": "LOW|MEDIUM|HIGH",
                          "mitigation": "Concrete mitigation action"
                        }
                      ],
                      "recommendations": [
                        {
                          "title": "Action title",
                          "description": "Actionable recommendation",
                          "priority": 1
                        }
                      ]
                    }

                    Rules:
                    - dimensions must contain exactly five objects, one for each allowed dimension, with no duplicates.
                    - score must be an integer from 0 to 100.
                    - risks must contain exactly three of the most important risks.
                    - recommendations must contain exactly three prioritized actions, with priority values 1, 2, and 3.
                    - Every text field must be a non-empty string.
                    - Base the analysis only on the submitted project data; do not invent missing facts.

                    Submitted project data:
                    %s
                    """.formatted(requestJson);
        } catch (JsonProcessingException exception) {
            throw new GroqApiException("Unable to serialize the analysis request", exception);
        }
    }
}
