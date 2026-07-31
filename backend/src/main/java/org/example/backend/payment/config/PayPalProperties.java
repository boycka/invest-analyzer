package org.example.backend.payment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "paypal")
public class PayPalProperties {
    private String clientId;
    private String clientSecret;
    private String mode = "sandbox";
    private String baseUrl = "https://api-m.sandbox.paypal.com";
    private String analysisPrice = "20.00";
    private String currency = "USD";
}