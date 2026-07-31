package org.example.backend.history.dto;

import java.time.LocalDateTime;

public record HistoryResponse(

        Long id,

        String sector,

        Double viabilityScore,

        String recommendation,

        LocalDateTime createdAt

) {}