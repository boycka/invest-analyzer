package org.example.backend.payment.dto;

import org.example.backend.analysis.dto.response.AnalysisResponse;

public record PaymentConfirmResponse(String orderId, String status, AnalysisResponse analysis) {
}