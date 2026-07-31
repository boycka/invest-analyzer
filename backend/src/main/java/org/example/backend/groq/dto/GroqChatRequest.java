package org.example.backend.groq.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record GroqChatRequest(
        String model,
        List<GroqMessage> messages,
        double temperature,
        @JsonProperty("response_format") GroqResponseFormat responseFormat
) {
}
