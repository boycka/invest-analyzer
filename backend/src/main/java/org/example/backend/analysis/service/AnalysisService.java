package org.example.backend.analysis.service;

import org.example.backend.analysis.dto.request.AnalysisRequest;
import org.example.backend.analysis.dto.response.AnalysisDetailResponse;
import org.example.backend.analysis.dto.response.AnalysisResponse;

public interface AnalysisService {

    AnalysisResponse analyzeProject(AnalysisRequest request);

    AnalysisDetailResponse getAnalysis(Long id);
}