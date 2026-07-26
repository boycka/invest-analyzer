package org.example.backend.analysis.service;

import org.example.backend.analysis.dto.request.AnalysisRequest;
import org.example.backend.analysis.dto.response.AnalysisResponse;

public interface AnalysisService {

    AnalysisResponse analyzeProject(AnalysisRequest request);

}