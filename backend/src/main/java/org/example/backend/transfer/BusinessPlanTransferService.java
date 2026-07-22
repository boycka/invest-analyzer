package org.example.backend.transfer;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BusinessPlanTransferService {

    private static final String HMAC_ALGORITHM = "HmacSHA256";

    private final SecretKeySpec signingKey;
    private final String businessPlanBaseUrl;
    private final Duration tokenValidity;

    public BusinessPlanTransferService(
            @Value("${jwt.secret}") String jwtSecret,
            @Value("${business-plan.base-url}") String businessPlanBaseUrl,
            @Value("${jwt.expiration}") long tokenExpirationMillis) {
        this.signingKey = new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM);
        this.businessPlanBaseUrl = businessPlanBaseUrl;
        this.tokenValidity = Duration.ofMillis(tokenExpirationMillis);
    }

    public BusinessPlanTransferResponse createRedirect(BusinessPlanTransferRequest request) {
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(tokenValidity);

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("projectName", request.projectName());
        payload.put("companyName", request.companyName());
        payload.put("sector", request.sector());
        payload.put("analysisSummary", request.analysisSummary());
        payload.put("investmentAmount", request.investmentAmount());
        payload.put("targetReturn", request.targetReturn());
        payload.put("riskLevel", request.riskLevel());
        payload.put("issuedAt", issuedAt.toEpochMilli());
        payload.put("expiresAt", expiresAt.toEpochMilli());

        String token = createToken(payload);
        String redirectUrl = businessPlanBaseUrl + "?token=" + token;

        return new BusinessPlanTransferResponse(redirectUrl, token);
    }

    private String createToken(Map<String, Object> payload) {
        try {
            String headerJson = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
            String payloadJson = toJson(payload);

            String encodedHeader = base64Url(headerJson.getBytes(StandardCharsets.UTF_8));
            String encodedPayload = base64Url(payloadJson.getBytes(StandardCharsets.UTF_8));
            String signingInput = encodedHeader + "." + encodedPayload;
            String signature = base64Url(sign(signingInput.getBytes(StandardCharsets.UTF_8)));

            return signingInput + "." + signature;
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to create business plan token", exception);
        }
    }

    private byte[] sign(byte[] data) throws Exception {
        Mac mac = Mac.getInstance(HMAC_ALGORITHM);
        mac.init(signingKey);
        return mac.doFinal(data);
    }

    private String base64Url(byte[] value) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(value);
    }

    private String toJson(Map<String, Object> values) {
        StringBuilder builder = new StringBuilder("{");
        boolean first = true;

        for (Map.Entry<String, Object> entry : values.entrySet()) {
            if (!first) {
                builder.append(',');
            }

            builder.append('"').append(escapeJson(entry.getKey())).append('"')
                    .append(':')
                    .append(formatJsonValue(entry.getValue()));
            first = false;
        }

        builder.append('}');
        return builder.toString();
    }

    private String formatJsonValue(Object value) {
        if (value instanceof Number || value instanceof Boolean) {
            return String.valueOf(value);
        }

        return '"' + escapeJson(String.valueOf(value)) + '"';
    }

    private String escapeJson(String value) {
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}