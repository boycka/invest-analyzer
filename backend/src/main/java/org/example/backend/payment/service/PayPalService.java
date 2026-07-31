package org.example.backend.payment.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.backend.analysis.dto.request.AnalysisRequest;
import org.example.backend.analysis.dto.response.AnalysisResponse;
import org.example.backend.analysis.service.AnalysisService;
import org.example.backend.payment.config.PayPalProperties;
import org.example.backend.payment.dto.PaymentConfirmResponse;
import org.example.backend.payment.dto.PaymentCreateResponse;
import org.example.backend.payment.exception.PaymentException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PayPalService {

    private final PayPalProperties properties;
    private final ObjectMapper objectMapper;
    private final AnalysisService analysisService;

    public PaymentCreateResponse createOrder(AnalysisRequest request) {
        ensureConfigured();
        String accessToken = accessToken();
        Map<String, Object> body = Map.of(
                "intent", "CAPTURE",
                "purchase_units", List.of(Map.of(
                        "description", "Tunisia Invest project analysis",
                        "amount", Map.of("currency_code", properties.getCurrency(), "value", properties.getAnalysisPrice())
                )),
                "application_context", Map.of("user_action", "PAY_NOW")
        );

        JsonNode response = post("/v2/checkout/orders", accessToken, body);
        String orderId = text(response, "id");
        String approvalUrl = "";
        for (JsonNode link : response.path("links")) {
            if ("approve".equals(link.path("rel").asText())) {
                approvalUrl = link.path("href").asText();
                break;
            }
        }
        if (!StringUtils.hasText(orderId) || !StringUtils.hasText(approvalUrl)) {
            throw new PaymentException("PayPal did not return an approval URL");
        }
        return new PaymentCreateResponse(orderId, approvalUrl, text(response, "status"), properties.getAnalysisPrice(), properties.getCurrency());
    }

    public PaymentConfirmResponse capture(PaymentConfirmRequestData request) {
        ensureConfigured();
        JsonNode response = post("/v2/checkout/orders/" + request.orderId() + "/capture", accessToken(), Map.of());
        String status = text(response, "status");
        if (!"COMPLETED".equalsIgnoreCase(status)) {
            throw new PaymentException("PayPal payment was not completed: " + status);
        }
        AnalysisResponse analysis = analysisService.analyzeProject(request.analysis());
        return new PaymentConfirmResponse(request.orderId(), status, analysis);
    }

    private String accessToken() {
        try {
            String credentials = properties.getClientId() + ":" + properties.getClientSecret();
            String encoded = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
            JsonNode response = RestClient.builder().baseUrl(properties.getBaseUrl()).build().post()
                    .uri("/v1/oauth2/token")
                    .header(HttpHeaders.AUTHORIZATION, "Basic " + encoded)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body("grant_type=client_credentials")
                    .retrieve().body(JsonNode.class);
            String token = response == null ? "" : response.path("access_token").asText();
            if (!StringUtils.hasText(token)) throw new PaymentException("PayPal access token is missing");
            return token;
        } catch (RestClientException exception) {
            throw new PaymentException("Unable to connect to PayPal Sandbox", exception);
        }
    }

    private JsonNode post(String path, String accessToken, Map<String, Object> body) {
        try {
            JsonNode response = RestClient.builder().baseUrl(properties.getBaseUrl()).build().post()
                    .uri(path)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve().body(JsonNode.class);
            if (response == null) throw new PaymentException("PayPal returned an empty response");
            return response;
        } catch (RestClientException exception) {
            throw new PaymentException("PayPal request failed", exception);
        }
    }

    private String text(JsonNode node, String field) { return node.path(field).asText(""); }

    private void ensureConfigured() {
        if (!StringUtils.hasText(properties.getClientId()) || !StringUtils.hasText(properties.getClientSecret())
                || "change_me".equalsIgnoreCase(properties.getClientId())
                || "change_me".equalsIgnoreCase(properties.getClientSecret())) {
            throw new PaymentException("PayPal Sandbox credentials are not configured");
        }
    }

    public record PaymentConfirmRequestData(String orderId, AnalysisRequest analysis) { }
}