package org.example.backend.groq.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "groq.api")
public class GroqProperties {

    private String key;

    private String url = "https://api.groq.com/openai/v1/chat/completions";

    private String model = "llama-3.3-70b-versatile";

    private int timeoutSeconds = 30;
}
