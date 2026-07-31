package org.example.backend.payment.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.backend.analysis.dto.request.AnalysisRequest;

public record PaymentConfirmRequest(
        @NotBlank String orderId,
        @Valid @NotNull AnalysisRequest analysis
) {
}