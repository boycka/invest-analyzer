package org.example.backend.groq.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.backend.analysis.dto.request.AnalysisRequest;
import org.example.backend.analysis.dto.response.AnalysisDimension;
import org.example.backend.analysis.dto.response.AnalysisRecommendation;
import org.example.backend.analysis.dto.response.AnalysisRisk;
import org.example.backend.groq.config.GroqProperties;
import org.example.backend.groq.dto.GroqChatRequest;
import org.example.backend.groq.dto.GroqChatResponse;
import org.example.backend.groq.dto.GroqMessage;
import org.example.backend.groq.dto.GroqResponseFormat;
import org.example.backend.groq.exception.GroqApiException;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GroqAnalysisClient {

    private static final Set<String> EXPECTED_DIMENSIONS = Set.of(
            "FINANCIAL_VIABILITY",
            "MARKET_OPPORTUNITY",
            "OPERATIONAL_FEASIBILITY",
            "REGULATORY_FRAMEWORK",
            "PROJECT_OWNER_PROFILE"
    );

    private final GroqProperties properties;
    private final ObjectMapper objectMapper;
    private final GroqPromptBuilder promptBuilder;

    public GroqAnalysisClient(GroqProperties properties, ObjectMapper objectMapper,
                              GroqPromptBuilder promptBuilder) {
        this.properties = properties;
        this.objectMapper = objectMapper;
        this.promptBuilder = promptBuilder;
    }

    public GroqChatResponse requestAnalysis(AnalysisRequest request) {
        validateConfiguration();

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        Duration timeout = Duration.ofSeconds(properties.getTimeoutSeconds());
        requestFactory.setConnectTimeout(timeout);
        requestFactory.setReadTimeout(timeout);

        GroqChatRequest payload = new GroqChatRequest(
                properties.getModel(),
                List.of(
                        new GroqMessage("system", "Return a strict JSON investment analysis."),
                        new GroqMessage("user", promptBuilder.build(request))
                ),
                0.2,
                new GroqResponseFormat("json_object")
        );

        try {
            GroqChatResponse response = RestClient.builder()
                    .baseUrl(properties.getUrl())
                    .requestFactory(requestFactory)
                    .build()
                    .post()
                    .header("Authorization", "Bearer " + properties.getKey())
                    .header("Content-Type", "application/json")
                    .body(payload)
                    .retrieve()
                    .body(GroqChatResponse.class);

            if (response == null || response.getChoices() == null || response.getChoices().isEmpty()
                    || response.getChoices().get(0).getMessage() == null
                    || !StringUtils.hasText(response.getChoices().get(0).getMessage().getContent())) {
                throw new GroqApiException("Groq returned an empty analysis response");
            }

            return response;
        } catch (RestClientResponseException exception) {
            throw new GroqApiException("Groq request failed with status " + exception.getStatusCode(), exception);
        } catch (RestClientException exception) {
            throw new GroqApiException("Unable to connect to Groq", exception);
        }
    }

    public AnalysisJsonResult parseResult(GroqChatResponse response) {
        String json = removeCodeFences(response.getChoices().get(0).getMessage().getContent());

        try {
            AnalysisJsonResult result = objectMapper.readValue(json, AnalysisJsonResult.class);
            validateResult(result);
            return result;
        } catch (JsonProcessingException exception) {
            throw new GroqApiException("Groq returned invalid JSON", exception);
        }
    }

    private void validateConfiguration() {
        if (!StringUtils.hasText(properties.getKey())) {
            throw new GroqApiException("GROQ_API_KEY is not configured");
        }
        if (!StringUtils.hasText(properties.getModel())) {
            throw new GroqApiException("GROQ_MODEL is not configured");
        }
        if (!StringUtils.hasText(properties.getUrl())) {
            throw new GroqApiException("GROQ_API_URL is not configured");
        }
    }

    private void validateResult(AnalysisJsonResult result) {
        if (result == null || result.dimensions() == null || result.dimensions().size() != 5
                || result.risks() == null || result.risks().size() != 3
                || result.recommendations() == null || result.recommendations().size() != 3) {
            throw new GroqApiException("Groq response does not contain the required analysis structure");
        }

        Set<String> dimensions = result.dimensions().stream()
                .map(AnalysisDimension::getDimension)
                .collect(Collectors.toSet());

        if (!dimensions.equals(EXPECTED_DIMENSIONS)
                || result.dimensions().stream().anyMatch(this::hasInvalidDimension)) {
            throw new GroqApiException("Groq response must contain the five required dimensions");
        }

        if (result.risks().stream().anyMatch(risk -> risk == null
                || !StringUtils.hasText(risk.getDescription())
                || !StringUtils.hasText(risk.getCriticality())
                || !StringUtils.hasText(risk.getMitigation()))) {
            throw new GroqApiException("Groq response contains an invalid risk");
        }

        if (result.recommendations().stream().anyMatch(recommendation -> recommendation == null
                || !StringUtils.hasText(recommendation.getTitle())
                || !StringUtils.hasText(recommendation.getDescription())
                || recommendation.getPriority() == null)) {
            throw new GroqApiException("Groq response contains an invalid recommendation");
        }
    }

    private boolean hasInvalidDimension(AnalysisDimension dimension) {
        return dimension == null || dimension.getScore() == null || dimension.getScore() < 0
                || dimension.getScore() > 100 || !StringUtils.hasText(dimension.getExplanation());
    }

    private String removeCodeFences(String content) {
        return content.trim()
                .replaceFirst("^```json\\s*", "")
                .replaceFirst("^```\\s*", "")
                .replaceFirst("\\s*```$", "")
                .trim();
    }

    public record AnalysisJsonResult(
            List<AnalysisDimension> dimensions,
            List<AnalysisRisk> risks,
            List<AnalysisRecommendation> recommendations
    ) {
    }
}
