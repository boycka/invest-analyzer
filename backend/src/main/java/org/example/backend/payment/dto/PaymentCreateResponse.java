package org.example.backend.payment.dto;

public record PaymentCreateResponse(String orderId, String approvalUrl, String status, String amount, String currency) {
}