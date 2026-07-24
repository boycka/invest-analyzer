package org.example.backend.security;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private static final String HMAC_ALGORITHM = "HmacSHA256";

    private final SecretKeySpec signingKey;

    public JwtService(@Value("${jwt.secret}") String jwtSecret) {
        this.signingKey = new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM);
    }

    public String generateToken(String subject) {
        try {
            String header = base64Url("{\"alg\":\"HS256\",\"typ\":\"JWT\"}".getBytes(StandardCharsets.UTF_8));
            String payload = base64Url(("{\"sub\":\"" + escape(subject) + "\"}").getBytes(StandardCharsets.UTF_8));
            String signingInput = header + "." + payload;
            String signature = base64Url(sign(signingInput.getBytes(StandardCharsets.UTF_8)));
            return signingInput + "." + signature;
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to generate token", exception);
        }
    }

    public String extractUsername(String token) {
        String[] parts = token.split("\\.");
        if (parts.length < 2) {
            return "";
        }
        String payload = new String(Base64.getUrlDecoder().decode(pad(parts[1])), StandardCharsets.UTF_8);
        return payload.replaceAll(".*\\\"sub\\\":\\\"([^\\\"]*)\\\".*", "$1");
    }

    public boolean validateToken(String token, String subject) {
        return subject != null && subject.equals(extractUsername(token));
    }

    private byte[] sign(byte[] data) throws Exception {
        Mac mac = Mac.getInstance(HMAC_ALGORITHM);
        mac.init(signingKey);
        return mac.doFinal(data);
    }

    private String base64Url(byte[] value) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(value);
    }

    private String pad(String value) {
        return value + "=".repeat((4 - (value.length() % 4)) % 4);
    }

    private String escape(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}