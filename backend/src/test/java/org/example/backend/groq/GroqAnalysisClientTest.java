package org.example.backend.groq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.backend.groq.config.GroqProperties;
import org.example.backend.groq.dto.GroqChatResponse;
import org.example.backend.groq.exception.GroqApiException;
import org.example.backend.groq.service.GroqAnalysisClient;
import org.example.backend.groq.service.GroqPromptBuilder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GroqAnalysisClientTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final GroqAnalysisClient client = new GroqAnalysisClient(
            properties(),
            objectMapper,
            new GroqPromptBuilder(objectMapper)
    );

    @Test
    void parsesAndValidatesTheExpectedGroqJson() throws Exception {
        GroqChatResponse response = objectMapper.readValue(validResponse(), GroqChatResponse.class);

        GroqAnalysisClient.AnalysisJsonResult result = client.parseResult(response);

        assertThat(result.dimensions()).hasSize(5);
        assertThat(result.risks()).hasSize(3);
        assertThat(result.recommendations()).hasSize(3);
    }

    @Test
    void rejectsAResponseWithAnInvalidDimensionScore() throws Exception {
        String invalid = validResponse().replace("\\\"score\\\":75", "\\\"score\\\":101");
        GroqChatResponse response = objectMapper.readValue(invalid, GroqChatResponse.class);

        assertThatThrownBy(() -> client.parseResult(response))
                .isInstanceOf(GroqApiException.class)
                .hasMessageContaining("five required dimensions");
    }

    private GroqProperties properties() {
        GroqProperties properties = new GroqProperties();
        properties.setKey("test-key");
        return properties;
    }

    private String validResponse() {
        return """
                {
                  "choices": [{
                    "message": {
                      "content": "{\\"dimensions\\":[{\\"dimension\\":\\"FINANCIAL_VIABILITY\\",\\"score\\":75,\\"explanation\\":\\"Good budget\\"},{\\"dimension\\":\\"MARKET_OPPORTUNITY\\",\\"score\\":70,\\"explanation\\":\\"Demand exists\\"},{\\"dimension\\":\\"OPERATIONAL_FEASIBILITY\\",\\"score\\":65,\\"explanation\\":\\"Plan is feasible\\"},{\\"dimension\\":\\"REGULATORY_FRAMEWORK\\",\\"score\\":60,\\"explanation\\":\\"Permits are needed\\"},{\\"dimension\\":\\"PROJECT_OWNER_PROFILE\\",\\"score\\":80,\\"explanation\\":\\"Relevant profile\\"}],\\"risks\\":[{\\"description\\":\\"Competition\\",\\"criticality\\":\\"HIGH\\",\\"mitigation\\":\\"Differentiate\\"},{\\"description\\":\\"Funding\\",\\"criticality\\":\\"MEDIUM\\",\\"mitigation\\":\\"Secure financing\\"},{\\"description\\":\\"Permits\\",\\"criticality\\":\\"MEDIUM\\",\\"mitigation\\":\\"Start early\\"}],\\"recommendations\\":[{\\"title\\":\\"Validate market\\",\\"description\\":\\"Interview customers\\",\\"priority\\":1},{\\"title\\":\\"Confirm financing\\",\\"description\\":\\"Get commitments\\",\\"priority\\":2},{\\"title\\":\\"Plan permits\\",\\"description\\":\\"Contact authorities\\",\\"priority\\":3}]}"
                    }
                  }]
                }
                """;
    }
}
