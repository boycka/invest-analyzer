package org.example.backend.analysis.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.analysis.dto.request.AnalysisRequest;
import org.example.backend.analysis.dto.response.AnalysisResponse;
import org.example.backend.analysis.service.AnalysisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analysis")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AnalysisController {

    private final AnalysisService analysisService;

    @PostMapping
    public ResponseEntity<AnalysisResponse> analyze(
            @RequestBody AnalysisRequest request
    ) {

        return ResponseEntity.ok(
                analysisService.analyzeProject(request)
        );
    }
    @GetMapping("/{id}")
    public ResponseEntity<AnalysisResponse> getById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                analysisService.getById(id)
        );
    }

}