package org.example.backend.common.response;

public record ErrorResponse(int status, String message, String timestamp) {
}