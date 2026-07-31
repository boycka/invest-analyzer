package org.example.backend.analysis.dto.response;

import org.example.backend.analysis.dto.request.AnalysisRequest;

public record AnalysisDetailResponse(
        AnalysisResponse result,
        AnalysisRequest request
) {
}